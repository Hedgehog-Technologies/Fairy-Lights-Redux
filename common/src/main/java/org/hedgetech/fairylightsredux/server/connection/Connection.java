package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.network.InteractionConnectionPayload;
import org.hedgetech.fairylightsredux.platform.Services;
import org.hedgetech.fairylightsredux.server.Fastener.Fastener;
import org.hedgetech.fairylightsredux.server.Fastener.FastenerType;
import org.hedgetech.fairylightsredux.server.Fastener.FenceFastener;
import org.hedgetech.fairylightsredux.server.Fastener.accessor.IFastenerAccessor;
import org.hedgetech.fairylightsredux.server.collision.CollidableList;
import org.hedgetech.fairylightsredux.server.collision.FeatureCollisionTree;
import org.hedgetech.fairylightsredux.server.collision.ICollidable;
import org.hedgetech.fairylightsredux.server.collision.Intersection;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.server.feature.IFeature;
import org.hedgetech.fairylightsredux.server.item.ConnectionItem;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;
import org.hedgetech.fairylightsredux.util.Catenary;
import org.hedgetech.fairylightsredux.util.CubicBezier;
import org.hedgetech.fairylightsredux.util.Curve3d;
import org.hedgetech.fairylightsredux.util.ICurve;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class Connection {
    public static final int MAX_LENGTH = 32;
    public static final double PULL_RANGE = 5;

    private static final CubicBezier SLACK_CURVE = new CubicBezier(0.495F, 0.505F, 0.495F, 0.505F);
    private static final float MAX_SLACK = 3;

    private final ConnectionType<?> type;
    private final UUID uuid;
    protected final Fastener<?> fastener;

    private IFastenerAccessor destination;
    private IFastenerAccessor prevDestination;
    private ICollidable collision = ICollidable.empty();
    private boolean updateCatenary;
    private int prevStretchStage;
    private boolean removed;
    private boolean drop;
    private ICurve catenary;
    protected ICurve prevCatenary;
    protected Level world;
    protected float slack = 1;

    public Connection(final ConnectionType<?> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
        this.type = type;
        this.world = world;
        this.fastener = fastener;
        this.uuid = uuid;
        this.computeCatenary();
    }

    public ConnectionType<?> getType() {
        return this.type;
    }

    public final ICurve getCatenary() {
        return this.catenary;
    }

    public final ICurve getPrevCatenary() {
        return this.prevCatenary == null
                ? this.catenary
                : this.prevCatenary;
    }

    public void setWorld(final Level world) {
        this.world = world;
    }

    public final Level getWorld() {
        return this.world;
    }

    public final ICollidable getCollision() {
        return this.collision;
    }

    public final Fastener<?> getFastener() {
        return this.fastener;
    }

    public final UUID getUUID() {
        return this.uuid;
    }

    public final void setDestination(final Fastener<?> destination) {
        this.prevDestination = this.destination;
        this.destination = destination.createAccessor();
        this.computeCatenary();
    }

    public final IFastenerAccessor getDestination() {
        return this.destination;
    }

    public boolean isDestination(final IFastenerAccessor location) {
        return this.destination.equals(location);
    }

    public void setDrop() {
        this.drop = true;
    }

    public void noDrop() {
        this.drop = false;
    }

    public boolean shouldDrop() {
        return this.drop;
    }

    public ItemStack getItemStack() {
        return new ItemStack(this.getType().getItem());
    }

    public boolean matches(final ItemStack stack) {
        return stack.is(this.getType().getItem());
    }

    public float getRadius() {
        return 0.0625F;
    }

    public final boolean isDynamic() {
        return this.fastener.isMoving() || this.destination.get(this.world, false).filter(Fastener::isMoving).isPresent();
    }

    public final boolean isModifiable(final Player player) {
        return this.world.mayInteract(player, this.fastener.getPos());
    }

    public final void remove() {
        if (!this.removed) {
            this.removed = true;
            this.onRemove();
        }
    }

    public final boolean isRemoved() {
        return this.removed;
    }

    public void computeCatenary() {
        this.updateCatenary = true;
    }

    public void processClientAction(final Player player, final PlayerAction action, final Intersection intersection) {
        Services.PLATFORM.sendToServer(new InteractionConnectionPayload(this.saveFastener(), this.uuid, action, intersection.result(), intersection.featureType(), intersection.feature().getId()));
    }

    public final CompoundTag saveFastener() {
        final TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.world.registryAccess());
        FastenerType.save(output, this.fastener.createAccessor());
        return output.buildResult();
    }

    public void disconnect(final Vec3 hit) {
        this.destination.get(this.world).ifPresent(destination -> {
            this.fastener.removeConnection(this);
            destination.removeConnection(this.uuid);

            if (this.shouldDrop()) {
                final ItemEntity item = new ItemEntity(this.world, hit.x, hit.y, hit.z, this.getItemStack());
                final float scale = 0.05F;
                item.setDeltaMovement(
                        this.world.random.nextGaussian() * scale,
                        this.world.random.nextGaussian() * scale + 0.2F,
                        this.world.random.nextGaussian() * scale
                );
                this.world.addFreshEntity(item);
            }

            this.world.playSound(null, hit.x, hit.y, hit.z, FLRSounds.CORD_DISCONNECT, SoundSource.BLOCKS, 1, 1);
        });
    }

    public boolean reconnect(final Fastener<?> destination) {
        return this.fastener.reconnect(this.world, this, destination);
    }

    public boolean interact(final Player player, final Vec3 hit, final FeatureType featureType, final int feature, final ItemStack heldStack, final InteractionHand hand) {
        if (heldStack.getItem() instanceof ConnectionItem && !this.matches(heldStack)) {
            this.replace(player, hit, heldStack);
        } else if (heldStack.is(FLRCraftingRecipes.STRINGS)) {
            return this.slacken(hit, 0.2F);
        } else if (heldStack.is(Items.STICK)) {
            return this.slacken(hit, -0.2F);
        }

        return false;
    }

    private boolean replace(final Player player, final Vec3 hit, final ItemStack heldStack) {
        return this.destination.get(this.world).map(destination -> {
            this.fastener.removeConnection(this);
            destination.removeConnection(this.uuid);
            if (this.shouldDrop()) player.getInventory().placeItemBackInInventory(this.getItemStack());

            final ConnectionType<?> type = ((ConnectionItem) heldStack.getItem()).getConnectionType();
            final Connection connection = this.fastener.connect(this.world, destination, type, heldStack, true);
            connection.slack = this.slack;
            connection.onConnect(player, heldStack);
            heldStack.shrink(1);
            this.world.playSound(null, hit.x, hit.y, hit.z, FLRSounds.CORD_CONNECT, SoundSource.BLOCKS, 1, 1);
            return true;
        }).orElse(false);
    }

    private boolean slacken(final Vec3 hit, final float amount) {
        if (this.slack <= 0 && amount < 0 || this.slack >= MAX_SLACK && amount > 0) return true;

        this.slack = Mth.clamp(this.slack + amount, 0, MAX_SLACK);
        if (this.slack < 1e-2F) this.slack = 0;

        this.computeCatenary();
        this.world.playSound(null, hit.x, hit.y, hit.z, FLRSounds.CORD_STRETCH, SoundSource.BLOCKS, 1, 0.8F + (MAX_SLACK - this.slack) * 0.4F);
        return true;
    }

    public void onConnect(final Player user, final ItemStack heldStack) {}

    protected void onRemove() {}

    protected void onUpdate() {}

    protected void onCalculateCatenary(final boolean relocated) {}

    public final boolean update(final Vec3 from) {
        this.prevCatenary = this.catenary;
        final boolean changed = this.destination.get(this.world, false).map(dest -> {
            final Vec3 point = dest.getConnectionPoint();
            final boolean c = this.updateCatenary(from, dest, point);
            this.onUpdate();
            final double dist = point.distanceTo(from);
            final double pull = dist - MAX_LENGTH + PULL_RANGE;

            if (pull > 0) {
                final int stage = (int) (pull + 0.1F);

                if (stage > this.prevStretchStage) {
                    this.world.playSound(null, point.x, point.y, point.z, FLRSounds.CORD_STRETCH, SoundSource.BLOCKS, 0.25F, 0.5F + stage / 8F);
                }

                this.prevStretchStage = stage;
            }

            if (dist > MAX_LENGTH + PULL_RANGE) {
                this.world.playSound(null, point.x, point.y, point.z, FLRSounds.CORD_SNAP, SoundSource.BLOCKS, 0.75F, 0.8F + this.world.random.nextFloat() * 0.3F);
                this.remove();
            } else if (dest.isMoving()) {
                dest.resistSnap(from);
            }

            return c;
        }).orElse(false);

        if (this.destination.isGone(this.world)) this.remove();

        return changed;
    }

    private boolean updateCatenary(final Vec3 from, final Fastener<?> dest, final Vec3 point) {
        if (this.updateCatenary || this.isDynamic()) {
            final Vec3 vec = point.subtract(from);

            if (vec.length() > 1e-6) {
                final Direction facing = this.fastener.getFacing();

                if (this.fastener instanceof FenceFastener && dest instanceof FenceFastener && vec.horizontalDistance() < 1e-2) {
                    this.catenary = this.verticalHelix(vec);
                } else {
                    this.catenary = Catenary.from(vec, facing.getAxis() == Direction.Axis.Y ? 0.0F : (float) Math.toRadians(90.0F + facing.toYRot()), SLACK_CURVE, this.slack);
                }

                this.onCalculateCatenary(!this.destination.equals(this.prevDestination));
                final List<ICollidable> collision = new ArrayList<>();
                this.addCollision(collision, from);
                this.collision = new CollidableList(collision);
            }

            this.updateCatenary = false;
            this.prevDestination = this.destination;
            return true;
        }

        return false;
    }

    private ICurve verticalHelix(final Vec3 vec) {
        final float length = (float) vec.length();
        final float height = (float) vec.y;
        final float stepSize = 0.25F;
        final float loopsPerBlock = 1.0F;
        final float radius = 0.33F;
        final int steps = (int) (Mth.TWO_PI * radius * loopsPerBlock * length / stepSize);
        final float rad = -Mth.TWO_PI * (loopsPerBlock * length);
        final float[] x = new float[steps];
        final float[] y = new float[steps];
        final float[] z = new float[steps];
        float helixLength = 0.0F;

        for (int i = 0; i < steps; i++) {
            float t = (float) i / (steps - 1);
            x[i] = radius * Mth.cos(t * rad);
            y[i] = t * height;
            z[i] = radius * Mth.sin(t * rad);
            if (i > 0) {
                helixLength += Mth.sqrt(
                        Mth.square(x[i] - x[i - 1]) +
                        Mth.square(y[i] - y[i - 1]) +
                        Mth.square(z[i] - z[i - 1])
                );
            }
        }

        return new Curve3d(steps, x, y, z, helixLength);
    }

    // The first and last segments are left out so the fasteners at either end stay targetable
    protected void addCollision(final List<ICollidable> collision, final Vec3 origin) {
        final int count = this.catenary.getCount();
        if (count <= 3) return;

        final float r = this.getRadius();
        final ICurve.ISegmentIterator it = this.catenary.iterator();
        final AABB[] bounds = new AABB[count - 1];
        int index = 0;

        while (it.next()) {
            bounds[index++] = new AABB(
                    origin.x + it.getX(0.0F), origin.y + it.getY(0.0F), origin.z + it.getZ(0.0F),
                    origin.x + it.getX(1.0F), origin.y + it.getY(1.0F), origin.z + it.getZ(1.0F)
            ).inflate(r);
        }

        collision.add(FeatureCollisionTree.build(FeatureType.CORD, i -> Segment.INSTANCE, i -> bounds[i], 1, bounds.length - 2));
    }

    public void setup(final Fastener<?> destination, final ItemStack stack, final boolean drop) {
        this.destination = destination.createAccessor();
        this.drop = drop;
        this.readItem(stack);
    }

    public void save(final ValueOutput output) {
        FastenerType.save(output.child("destination"), this.destination);
        this.saveLogic(output.child("logic"));
        output.putFloat("slack", this.slack);
        if (!this.drop) output.putBoolean("drop", false);
    }

    public boolean load(final ValueInput input) {
        final Optional<IFastenerAccessor> destination = FastenerType.load(input.childOrEmpty("destination"));
        if (destination.isEmpty()) return false;

        this.destination = destination.get();
        this.loadLogic(input.childOrEmpty("logic"));
        this.slack = input.getFloatOr("slack", 1);
        this.drop = input.getBooleanOr("drop", true);
        this.updateCatenary = true;
        return true;
    }

    protected void readItem(final ItemStack stack) {}

    protected void saveLogic(final ValueOutput output) {}

    protected void loadLogic(final ValueInput input) {}

    static final class Segment implements IFeature {
        static final Segment INSTANCE = new Segment();

        @Override
        public int getId() {
            return 0;
        }
    }
}
