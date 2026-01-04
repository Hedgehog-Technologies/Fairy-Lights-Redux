package org.hedgetech.fairylightsredux.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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

    @Override
    public @NonNull EntityDimensions getDimensions(final @NonNull Pose pose) {
        final float sizeInBlocks = 9.0F / 16.0F;
        return EntityDimensions.fixed(sizeInBlocks, sizeInBlocks);
    }

    @Override
    protected @NonNull AABB calculateBoundingBox(final @NonNull BlockPos pos, final @NonNull Direction direction) {
        final EntityDimensions dims = this.getDimensions(Pose.STANDING);
        final double w = dims.width();
        final double h = dims.height();

        // center of the block with a small offset toward the face the entity hangs on
        final double cx = pos.getX() + 0.5D - direction.getStepX() * 0.5D;
        final double cy = pos.getY() + 0.5D;
        final double cz = pos.getZ() + 0.5D - direction.getStepZ() * 0.5D;

        final double minX = cx - w / 2.0D;
        final double minY = cy - h / 2.0D;
        final double minZ = cz - w / 2.0D;

        return new AABB(minX, minY, minZ, minX + w, minY + h, minZ + w);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(final double distance) {
        return distance < 4096;
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

    // Copy from super but remove() moved to after onBroken()
    @Override
    public boolean hurtServer(final @NonNull ServerLevel level, final @NonNull DamageSource source, final float amount) {
        if (this.isInvulnerableToBase(source)) {
            return false;
        }
        if (this.isAlive()) {
            this.markHurt();
            this.dropItem(level, source.getEntity());
            this.remove(RemovalReason.KILLED);
        }
        return true;
    }

    @Override
    public boolean canUsePortal(final boolean isEndPortal) {
        return false;
    }

    @Override
    public void dropItem(final @NonNull ServerLevel level, @Nullable final Entity breaker) {
        this.getFastener().ifPresent(fastener -> fastener.dropItems(level, this.pos));
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
        super.setPos(Mth.floor(x) + 0.5, Mth.floor(y) + 0.5, Mth.floor(z) + 0.5);
    }

    @Override
    public void setDirection(final @NonNull Direction facing) {}

    @Override
    protected void recalculateBoundingBox() {
        final double posX = this.pos.getX() + 0.5D;
        final double posY = this.pos.getY() + 0.5D;
        final double posZ = this.pos.getZ() + 0.5D;
        this.setPosRaw(posX, posY, posZ);
        final float w = 3 / 16F;
        final float h = 3 / 16F;
        this.setBoundingBox(new AABB(posX - w, posY - h, posZ - w, posX + w, posY + h, posZ + w));
    }

    @Override
    public void tick() {
        this.getFastener().ifPresent(fastener -> {
           if (!this.level().isClientSide() && (fastener.hasNoConnections() || this.checkSurface())) {
               this.dropItem((ServerLevel) this.level(), null);
               this.remove(RemovalReason.DISCARDED);
           } else if (fastener.update() && !this.level().isClientSide()) {
               final UpdateEntityFastenerMessage msg = new UpdateEntityFastenerMessage(this, fastener.serializeNBT());
               ServerProxy.sendToPlayersWatchingEntity(msg, this);
           }
        });
    }

    @Override
    public @NonNull InteractionResult interact(final Player player, final @NonNull InteractionHand hand) {
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

    @Override
    public void addAdditionalSaveData(final CompoundTag compound) {

    }

    private boolean checkSurface() {
        if (this.surfaceCheckTime++ == 100) {
            this.surfaceCheckTime = 0;
            return !this.survives();
        }
        return false;
    }

    public boolean ignoreExplosion() {
        return true;
    }
}
