package org.hedgetech.fairylightsredux.server.Fastener;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.Fastener.accessor.IFastenerAccessor;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.connection.ConnectionType;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.hedgetech.fairylightsredux.util.AABBBuilder;
import org.hedgetech.fairylightsredux.util.ICurve;

import java.util.*;

public abstract class Fastener<F extends IFastenerAccessor> {
    private final Map<UUID, Connection> outgoing = new HashMap<>();
    private final Map<UUID, Incoming> incoming = new HashMap<>();

    private Level world;
    private boolean dirty;
    protected AABB bounds = new AABB(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);

    public Optional<Connection> get(final UUID id) {
        return Optional.ofNullable(this.outgoing.get(id));
    }

    public List<Connection> getOwnConnections() {
        return ImmutableList.copyOf(this.outgoing.values());
    }

    public List<Connection> getAllConnections() {
        final ImmutableList.Builder<Connection> list = new ImmutableList.Builder<>();
        list.addAll(this.outgoing.values());

        if (this.world != null) this.incoming.values().forEach(i -> i.get(this.world).ifPresent(list::add));

        return list.build();
    }

    public Optional<Connection> getFirstConnection() {
        return this.getAllConnections().stream().findFirst();
    }

    public AABB getBounds() {
        return this.bounds;
    }

    public abstract Vec3 getConnectionPoint();

    public abstract BlockPos getPos();

    public abstract Direction getFacing();

    public abstract F createAccessor();

    public abstract boolean isMoving();

    public void resistSnap(final Vec3 from) {}

    public void setWorld(final Level world) {
        this.world = world;
        this.outgoing.values().forEach(c -> c.setWorld(world));
    }

    public Level getWorld() {
        return this.world;
    }

    public boolean update() {
        final Iterator<Connection> it = this.outgoing.values().iterator();
        final Vec3 fromOffset = this.getConnectionPoint();
        boolean dirty = this.dirty;
        this.dirty = false;

        while (it.hasNext()) {
            final Connection connection = it.next();
            if (connection.update(fromOffset)) dirty = true;

            if (connection.isRemoved()) {
                dirty = true;
                it.remove();
                this.incoming.remove(connection.getUUID());
                if (this.world != null) this.drop(this.world, this.getPos(), connection);
            }
        }

        if (this.world != null) this.incoming.values().removeIf(incoming -> incoming.gone(this.world));
        if (dirty) this.calculateBoundingBox();

        return dirty;
    }

    public void setDirty() {
        this.dirty = true;
    }

    protected void calculateBoundingBox() {
        if (this.outgoing.isEmpty()) {
            this.bounds = new AABB(this.getPos());
            return;
        }

        final AABBBuilder builder = new AABBBuilder();
        for (final Connection connection : this.outgoing.values()) {
            final ICurve catenary = connection.getCatenary();
            if (catenary == null) continue;

            final ICurve.ISegmentIterator it = catenary.iterator();
            while (it.next()) {
                builder.include(it.getX(0.0F), it.getY(0.0F), it.getZ(0.0F));
                if (!it.hasNext()) {
                    builder.include(it.getX(1.0F), it.getY(1.0F), it.getZ(1.0F));
                }
            }
        }

        this.bounds = builder.add(this.getConnectionPoint()).build();
    }

    public void dropItems(final Level world, final BlockPos pos) {
        for (final Connection connection : this.getAllConnections()) {
            this.drop(world, pos, connection);
        }
    }

    private void drop(final Level world, final BlockPos pos, final Connection connection) {
        if (!connection.shouldDrop()) return;

        final float offsetX = world.random.nextFloat() * 0.8F + 0.1F;
        final float offsetY = world.random.nextFloat() * 0.8F + 0.1F;
        final float offsetZ = world.random.nextFloat() * 0.8F + 0.1F;
        final ItemStack stack = connection.getItemStack();
        final ItemEntity entityItem = new ItemEntity(world, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ, stack);
        final float scale = 0.05F;

        entityItem.setDeltaMovement(
                world.random.nextGaussian() * scale,
                world.random.nextGaussian() * scale + 0.2F,
                world.random.nextGaussian() * scale
        );
        world.addFreshEntity(entityItem);
        connection.noDrop();
    }

    public void remove() {
        this.outgoing.values().forEach(Connection::remove);
    }

    public boolean hasNoConnections() {
        return this.outgoing.isEmpty() && this.incoming.isEmpty();
    }

    public boolean hasConnectionWith(final Fastener<?> fastener) {
        return this.getConnectionTo(fastener.createAccessor()) != null;
    }

    public Connection getConnectionTo(final IFastenerAccessor destination) {
        for (final Connection connection : this.outgoing.values()) {
            if (connection.isDestination(destination)) {
                return connection;
            }
        }
        return null;
    }

    public boolean removeConnection(final UUID uuid) {
        final Connection connection = this.outgoing.remove(uuid);

        if (connection != null) {
            connection.remove();
            this.setDirty();
            return true;
        } else if (this.incoming.remove(uuid) != null) {
            this.setDirty();
            return true;
        }

        return false;
    }

    public boolean removeConnection(final Connection connection) {
        return this.removeConnection(connection.getUUID());
    }

    public boolean reconnect(final Level world, final Connection connection, final Fastener<?> newDestination) {
        if (this.equals(newDestination) || newDestination.hasConnectionWith(this)) return false;

        final UUID uuid = connection.getUUID();

        if (connection.getDestination().get(world, false).filter(t -> {
            t.removeConnection(uuid);
            return true;
        }).isPresent()) {
            connection.setDestination(newDestination);
            connection.setDrop();
            newDestination.createIncomingConnection(uuid, this);
            this.setDirty();
            return true;
        }

        return false;
    }

    public Connection connect(final Level world, final Fastener<?> destination, final ConnectionType<?> type, final ItemStack stack, final boolean drop) {
        final UUID uuid = Mth.createInsecureUUID();
        final Connection connection = this.createOutgoingConnection(world, uuid, destination, type, stack, drop);
        destination.createIncomingConnection(uuid, this);
        return connection;
    }

    public Connection createOutgoingConnection(final Level world, final UUID uuid, final Fastener<?> destination, final ConnectionType<?> type, final ItemStack stack, final boolean drop) {
        final Connection c = type.create(world, this, uuid);
        c.setup(destination, stack, drop);
        this.outgoing.put(uuid, c);
        this.setDirty();
        return c;
    }

    public void createIncomingConnection(final UUID uuid, final Fastener<?> destination) {
        this.incoming.put(uuid, new Incoming(destination.createAccessor(), uuid));
        this.setDirty();
    }

    public void save(final ValueOutput output) {
        final ValueOutput.ValueOutputList outgoing = output.childrenList("outgoing");
        for (final Map.Entry<UUID, Connection> entry : this.outgoing.entrySet()) {
            final Connection connection = entry.getValue();
            final ValueOutput tag = outgoing.addChild();
            tag.store("uuid", UUIDUtil.CODEC, entry.getKey());
            tag.putString("type", ConnectionTypes.getId(connection.getType()).toString());
            connection.save(tag.child("connection"));
        }

        final ValueOutput.ValueOutputList incoming = output.childrenList("incoming");
        for (final Map.Entry<UUID, Incoming> entry : this.incoming.entrySet()) {
            final ValueOutput tag = incoming.addChild();
            tag.store("uuid", UUIDUtil.CODEC, entry.getKey());
            FastenerType.save(tag.child("fastener"), entry.getValue().fastener);
        }
    }

    public void load(final ValueInput input) {
        final List<UUID> uuids = new ArrayList<>();

        for (final ValueInput tag : input.childrenListOrEmpty("outgoing")) {
            final UUID uuid = tag.read("uuid", UUIDUtil.CODEC).orElseGet(Mth::createInsecureUUID);
            final Connection existing = this.outgoing.get(uuid);

            if (existing != null) {
                if (existing.load(tag.childOrEmpty("connection"))) {
                    uuids.add(uuid);
                }
                continue;
            }

            final ConnectionType<?> type = tag.getString("type").map(ResourceLocation::tryParse).map(ConnectionTypes::get).orElse(null);
            if (type != null) {
                final Connection connection = type.create(this.world, this, uuid);
                if (connection.load(tag.childOrEmpty("connection"))) {
                    this.outgoing.put(uuid, connection);
                    uuids.add(uuid);
                }
            }
        }

        final Iterator<Map.Entry<UUID, Connection>> connections = this.outgoing.entrySet().iterator();
        while (connections.hasNext()) {
            final Map.Entry<UUID, Connection> connection = connections.next();
            if (!uuids.contains(connection.getKey())) {
                connections.remove();
                connection.getValue().remove();
            }
        }

        this.incoming.clear();
        for (final ValueInput tag : input.childrenListOrEmpty("incoming")) {
            final Optional<UUID> uuid = tag.read("uuid", UUIDUtil.CODEC);
            final Optional<IFastenerAccessor> fastener = FastenerType.load(tag.childOrEmpty("fastener"));

            if (uuid.isPresent() && fastener.isPresent()) {
                this.incoming.put(uuid.get(), new Incoming(fastener.get(), uuid.get()));
            }
        }

        this.setDirty();
    }

    record Incoming(IFastenerAccessor fastener, UUID id) {
        boolean gone(final Level world) {
            return this.fastener.isGone(world);
        }

        Optional<Connection> get(final Level world) {
            return this.fastener.get(world, false).flatMap(f -> f.get(this.id));
        }
    }
}
