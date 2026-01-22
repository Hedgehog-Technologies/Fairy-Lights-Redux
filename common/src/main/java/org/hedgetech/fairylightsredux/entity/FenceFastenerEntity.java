package org.hedgetech.fairylightsredux.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.content.block.BlockDefs;
import org.hedgetech.fairylightsredux.content.entity.EntityDefs;
import org.hedgetech.fairylightsredux.content.item.ConnectionItem;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.fastener.FenceFastener;
import org.hedgetech.fairylightsredux.registry.FLRBlocks;
import org.hedgetech.fairylightsredux.registry.FLREntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FenceFastenerEntity extends BlockAttachedEntity {
    private FenceFastener fastener;
    private int surfaceCheckTime;

    public FenceFastenerEntity(final EntityType<? extends FenceFastenerEntity> type, final Level world) {
        super(type, world);
    }

    public FenceFastenerEntity(final Level world) {
        this(FLREntities.get(EntityDefs.FENCE_FASTENER_ID, FenceFastenerEntity.class), world);
    }

    public FenceFastenerEntity(final Level world, final BlockPos pos) {
        this(world);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

//    @Override
    @Deprecated(since = "No longer exists - use this.dimensions.width", forRemoval = true)
    public int getWidth() {
        return 9;
    }

    //    @Override
    @Deprecated(since = "No longer exists - use this.dimensions.height", forRemoval = true)
    public int getHeight() {
        return 9;
    }

    //    @Override
    @Deprecated(since = "No longer exists - use this.dimensions.eyeHeight", forRemoval = true)
    public float getEyeHeight(final Pose pose, final EntityDimensions dimensions) {
        /*
         * Because this entity is inside of a block when
         * EntityLivingBase#canEntityBeSeen performs its
         * raytracing it will always return false during
         * NetHandlerPlayServer#processUseEntity, making
         * the player reach distance be limited at three
         * blocks as opposed to the standard six blocks.
         * EntityLivingBase#canEntityBeSeen will add the
         * value given by getEyeHeight to the y position
         * of the entity to calculate the end point from
         * which to raytrace to. Returning one lets most
         * interactions with a player succeed, typically
         * for breaking the connection or creating a new
         * connection. I hope you enjoy my line lengths.
         */
        return 1;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(final double distance) {
        return distance < 4096;
    }

    @Override
    public boolean ignoreExplosion(@NotNull Explosion explosion) {
        return true;
    }

    @Override
    public boolean survives() {
        return !this.level().isLoaded(this.pos) || ConnectionItem.isFence(this.level().getBlockState(this.pos));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {}

    @Override
    public void remove(final @NotNull RemovalReason reason) {
        this.getFastener().remove();
        super.remove(reason);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel world, @NotNull DamageSource source, float amount) {
        if (this.isInvulnerableToBase(source)) return false;
        if (this.isAlive()) {
            this.markHurt();
            this.dropItem(world, source.getEntity());
            this.remove(RemovalReason.KILLED);
        }
        return true;
    }

    //    @Override
    @Deprecated(since = "No longer exists - Use canTeleport", forRemoval = true)
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean canTeleport(final @NotNull Level fromLevel, final @NotNull Level toLevel) {
        return false;
    }

    @Override
    public void dropItem(final @NotNull ServerLevel world, @Nullable final Entity breaker) {
        this.getFastener().dropItems(world, this.pos);
        if (breaker != null) {
            world.levelEvent(2001, this.pos, Block.getId(FLRBlocks.get(BlockDefs.FASTENER_ID).defaultBlockState()));
        }
    }

//    @Override
//    public void playPlacementSound() {
//        // TODO - Verify this equates to the original `FLBlocks.FASTENER.get().getSoundType(FLBlocks.FASTENER.get().defaultBlockState(), this.level(), this.getPos(), null)`
//        final SoundType sound = FLRBlocks.get("fastener").defaultBlockState().getSoundType();
//        this.playSound(sound.getPlaceSound(), (sound.getVolume() + 1) / 2, sound.getPitch() * 0.8F);
//    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.BLOCKS;
    }

    @Override
    public void setPos(final double x, final double y, final double z) {
        super.setPos(Mth.floor(x) + 0.5D, Mth.floor(y) + 0.5D, Mth.floor(z) + 0.5D);
    }

//    @Override
//    public void setDirection(final @NotNull Direction facing) {}

    @Override
    protected void recalculateBoundingBox() {
        final double posX = this.pos.getX() + 0.5D;
        final double posY = this.pos.getY() + 0.5D;
        final double posZ = this.pos.getZ() + 0.5D;
        final float w = 3 / 16.0F;
        final float h = 3 / 16.0F;
        this.setPosRaw(posX, posY, posZ);
        this.setBoundingBox(new AABB(posX - w, posY - h, posZ - w,
                posX + w, posY + h, posZ + w));
    }

//    @Override
//    protected @NotNull AABB calculateBoundingBox(BlockPos blockPos, @NotNull Direction direction) {
//        final double posX = blockPos.getX() + 0.5D;
//        final double posY = blockPos.getY() + 0.5D;
//        final double posZ = blockPos.getZ() + 0.5D;
//        final float w = 3 / 16.0F;
//        final float h = 3 / 16.0F;
//        return new AABB(posX - w, posY - h, posZ - w,
//                posX + w, posY + h, posZ + w);
//    }

    //    @Override
    @Deprecated(since = "No longer exists")
    public AABB getBoundingBoxForCulling() {
        throw new NotImplementedException("FenceFastenerEntity.getBoundingBoxForCulling");
//        return this.getFastener().map(fastener -> fastener.getBounds().inflate(1.0D)).orElseGet(super::getBoundingBoxForCulling);
    }

    @Override
    public void tick() {
        Fastener<?> fastener = this.getFastener();
        if (!(this.level().isClientSide() && (fastener.hasNoConnections() || this.checkSurface()))) {
            this.dropItem((ServerLevel) this.level(), null);
            this.remove(RemovalReason.DISCARDED);
        } else if (fastener.update() && !this.level().isClientSide()) {
            // FIXME - implement update packet sending
            throw new NotImplementedException("FenceFastenerEntity.tick");
//                final UpdateEntityFastenerMessage msg = new UpdateEntityFastenerMessage(this, fastener.serializeNBT());
//                ServerProxy.sendToPlayersWatchingEntity(msg, this);
        }
    }

    private boolean checkSurface() {
        if (this.surfaceCheckTime++ == 100) {
            this.surfaceCheckTime = 0;
            return !this.survives();
        }
        return false;
    }

    @Override
    public @NotNull InteractionResult interact(final Player player, final @NotNull InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof ConnectionItem ci) {
            if (this.level().isClientSide()) {
                player.swing(hand);
            } else {
                ci.connect(stack, player, this.level(), this.getFastener());
            }
            return InteractionResult.SUCCESS;
        }
        return super.interact(player, hand);
    }

    // TODO - Figure out if IEntityAdditionalSpawnData equivalent is needed
//    @Override
//    public void writeSpawnData(final FriendlyByteBuf buf) {
//        this.getFastener().ifPresent(fastener -> {
//            try {
//                NbtIo.write(fastener.serializeNBT(), new ByteBufOutputStream(buf));
//            } catch (final IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }
//
//    @Override
//    public void readSpawnData(final FriendlyByteBuf buf) {
//        this.getFastener().ifPresent(fastener -> {
//            try {
//                fastener.deserializeNBT(NbtIo.read(new ByteBufInputStream(buf), new NbtAccounter(0x200000)));
//            } catch (final IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//    }

    //    @Override
    @Deprecated(since = "No longer exists - was just used to add forge specific stuff anyways", forRemoval = true)
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new NotImplementedException("FenceFastenerEntity.getAddEntityPacket");
//        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public Fastener<?> getFastener() {
        if (this.fastener == null) {
            this.fastener = new FenceFastener(this);
        }
        return this.fastener;
    }

    public static FenceFastenerEntity create(final Level world, final BlockPos fence) {
        final FenceFastenerEntity fastener = new FenceFastenerEntity(world, fence);
//        fastener.forceSpawn = true;
        world.addFreshEntity(fastener);
        return fastener;
    }

    @Nullable
    public static FenceFastenerEntity find(final Level world, final BlockPos pos) {
        final BlockAttachedEntity e = findAttached(world, pos);
        if (e instanceof FenceFastenerEntity ffe) {
            return ffe;
        }
        return null;
    }

    @Nullable
    public static BlockAttachedEntity findAttached(final Level world, final BlockPos pos) {
        for (final BlockAttachedEntity e : world.getEntitiesOfClass(BlockAttachedEntity.class, new AABB(pos).inflate(2.0D))) {
            if (e.getPos().equals(pos)) {
                return e;
            }
        }
        return null;
    }
}
