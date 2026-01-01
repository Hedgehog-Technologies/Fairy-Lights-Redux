package org.hedgetech.fairylightsredux.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.capability.CapabilityHandler;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.item.ConnectionItem;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.io.IOException;

public final class FenceFastenerEntity extends HangingEntity implements IEntityAdditionalSpawnData {
    private int surfaceCheckTime;

    public FenceFastenerEntity(final EntityType<? extends FenceFastenerEntity> type, final Level world) {
        super(type, world);
    }

    public FenceFastenerEntity(final Level world) {
        this(FLREntities.FASTENER.get(), world);
    }

    public FenceFastenerEntity(final Level world, final BlockPos pos) {
        this(world);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

//    @Override
//    public int getWidth() {
//        return 9;
//    }

//    @Override
//    public int getHeight() {
//        return 9;
//    }

//    @Override
//    public float getEyeHeight(final Pose pose, final EntityDimensions size) {
//        /*
//         * Because this entity is inside of a block when
//         * EntityLivingBase#canEntityBeSeen performs its
//         * raytracing it will always return false during
//         * NetHandlerPlayServer#processUseEntity, making
//         * the player reach distance be limited at three
//         * blocks as opposed to the standard six blocks.
//         * EntityLivingBase#canEntityBeSeen will add the
//         * value given by getEyeHeight to the y position
//         * of the entity to calculate the end point from
//         * which to raytrace to. Returning one lets most
//         * interactions with a player succeed, typically
//         * for breaking the connection or creating a new
//         * connection. I hope you enjoy my line lengths.
//         */
//        return 1;
//    }

    @Override
    public boolean shouldRenderAtSqrDistance(final double distance) {
        return distance < 4096;
    }

    @Override
    public boolean ignoreExplosion(@NonNull Explosion explosion) {
        return true;
    }

    @Override
    public boolean survives() {
        return !this.level().isLoaded(this.pos) || ConnectionItem.isFence(this.level().getBlockState(this.pos));
    }

    @Override
    public void remove(final @NonNull RemovalReason reason) {
        this.getFastener().ifPresent(Fastener::remove);
        super.remove(reason);
    }

    // Copy from super (BlockAttachedEntity) but remove() moved to after onBroken()
    @Override
    public boolean hurtServer(final @NonNull ServerLevel level, final @NonNull DamageSource source, final float amount) {
        if (this.isInvulnerableToBase(source)) {
            return false;
        }
        if (!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && source.getEntity() instanceof Mob) {
            return false;
        }
        if (!this.isRemoved()) {
            this.markHurt();
            this.dropItem(level, source.getEntity());
            this.kill(level);
        }
        return true;
    }

    @Override
    public boolean canUsePortal(boolean isEnd) {
        return false;
    }

    @Override
    public boolean canTeleport(@NonNull Level from, @NonNull Level to) {
        return false;
    }

    @Override
    public void dropItem(final @NonNull ServerLevel level, @Nullable final Entity breaker) {
        this.getFastener().ifPresent(fastener -> fastener.dropItems(level(), this.pos));
        if (breaker != null) {
            level.levelEvent(2001, this.pos, Block.getId(FLRBlocks.FASTENER.get().defaultBlockState()));
        }
    }

    @Override
    public void playPlacementSound() {
        final SoundType sound = FLRBlocks.FASTENER.get().getSoundType(FLRBlocks.FASTENER.get().defaultBlockState(), this.level(), this.getPos(), null);
        this.playSound(sound.getPlaceSound(), (sound.getVolume() + 1) / 2, sound.getPitch() * 0.8F);
    }

    @Override
    public @NonNull SoundSource getSoundSource() {
        return SoundSource.BLOCKS;
    }

    @Override
    public void setPos(final double x, final double y, final double z) {
        super.setPos(Mth.floor(x) + 0.5, Mth.floor(y) + 0.f, Math.floor(z) + 0.5);
    }

    @Override
    protected void recalculateBoundingBox() {
        BlockPos pos = this.blockPosition();

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;

        this.setPosRaw(x, y, z);
        this.setBoundingBox(calculateBoundingBox(pos, Direction.UP));
    }

    @Override
    protected @NonNull AABB calculateBoundingBox(@NonNull BlockPos pos, @NonNull Direction dir) {
        final double x = pos.getX() + 0.5;
        final double y = pos.getY() + 0.5;
        final double z = pos.getZ() + 0.5;

        final float w = 3 / 16F;
        final float h = 3 / 16F;

        return new AABB(
                x - w, y - h, z - w,
                x + w, y + h, z + w
        );
    }

    // @TODO - this is now handled by EntityRenderer class, which will need a new custom class
//    @Override
//    public AABB getBoundingBoxForCulling() {
//
//    }

    @Override
    public void tick() {
        this.getFastener().ifPresent(fastener -> {
            if (!this.level().isClientSide() && (fastener.hasNoConnections() || this.checkSurface())) {
                this.dropItem((ServerLevel) this.level(), null);
                this.remove(RemovalReason.DISCARDED);
            } else if (fastener.update() && !this.level().isClientSide()) {
                // @TODO - CompoundTag update
//                final UpdateEntityFastenerMessage msg = new UpdateEntityFastenerMessage(this, fastener.serializeNBT());
//                ServerProxy.sentToPlayersWatchingEntity(msg, this);
            }
        });
    }

    private boolean checkSurface() {
        if (this.surfaceCheckTime++ == 100) {
            this.surfaceCheckTime = 0;
            return !this.survives();
        }
        return false;
    }

    @Override
    public @NonNull InteractionResult interact(final @NonNull Player player, final @NonNull InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ConnectionItem ci) {
            if (this.level().isClientSide()) {
                player.swing(hand);
            } else {
                this.getFastener().ifPresent(fastener -> ci.connect(stack, player, this.level(), fastener));
            }
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    // @TODO - CompoundTag update
//    @Override
//    public void addAdditionalSaveData(final CompoundTag compound) {
//        compound.put("pos", NbtHelpers.writeBlockPos(this.pos));
//    }

    // @TODO - CompoundTag update
//    @Override
//    public void readAdditionalSaveData(final CompoundTag compound) {
//        this.pos = NbtHelpers.readBlockPos(compound.getCompound("pos"));
//    }

    // @TODO - CompoundTag update
    @Override
    public void writeSpawnData(final FriendlyByteBuf buf) {
        this.getFastener().ifPresent(fastener -> {
            try {
                NbtIo.write(fastener.serializeNBT(), new ByteBufOutputStream(buf));
            } catch(final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // @TODO - CompoundTag update
    @Override
    public void readSpawnData(final FriendlyByteBuf buf) {
        this.getFastener().ifPresent(fastener -> {
            try {
                NbtIo.deserializeNBT(NbtIo.read(new ByteBufInputStream(buf), new NbtAccounter(0x200000)));
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // @TODO - Packet update
//    @Override
//    public Packet<ClientGamePacketListener> getAddEntityPacket() {
//        NetworkHooks.getEntitySpawningPacket(this);
//    }

    private LazyOptional<Fastener<?>> getFastener() {
        return this.getCapability(CapabilityHandler.FASTENER_CAP);
    }

    public static FenceFastenerEntity create(final Level world, final BlockPos fence) {
        final FenceFastenerEntity fastener = new FenceFastenerEntity(world, fence);
//        fastener.forceSpawn = true;
        world.addFreshEntity(fastener);
        fastener.playPlacementSound();
        return fastener;
    }

    @Nullable
    public static FenceFastenerEntity find(final Level world, final BlockPos pos) {
        final HangingEntity entity = findHanging(world, pos);
        if (entity instanceof FenceFastenerEntity ffe) {
            return ffe;
        }
        return null;
    }

    @Nullable
    public static HangingEntity findHanging(final Level world, final BlockPos pos) {
        for (final HangingEntity e : world.getEntitiesOfClass(HangingEntity.class, new AABB(pos).inflate(2))) {
            if (e.getPos().equals(pos)) {
                return e;
            }
        }
        return null;
    }
}
