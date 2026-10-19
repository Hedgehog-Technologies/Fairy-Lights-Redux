package org.hedgetech.fairylightsredux.server.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.server.Fastener.Fastener;
import org.hedgetech.fairylightsredux.server.Fastener.FenceFastener;
import org.hedgetech.fairylightsredux.server.Fastener.IFastenerHolder;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.item.ConnectionItem;
import org.jetbrains.annotations.NotNull;

public final class FenceFastenerEntity extends BlockAttachedEntity implements IFastenerHolder {
    private static final EntityDataAccessor<CompoundTag> FASTENER = SynchedEntityData.defineId(FenceFastenerEntity.class, EntityDataSerializers.COMPOUND_TAG);

    private final FenceFastener fastener = new FenceFastener(this);

    public FenceFastenerEntity(final EntityType<? extends FenceFastenerEntity> type, final Level world) {
        super(type, world);
    }

    public FenceFastenerEntity(final Level world, final BlockPos pos) {
        super(FLREntities.FASTENER, world, pos);
        this.setPos(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public Fastener<?> getFastener() {
        return this.fastener;
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.Builder builder) {
        builder.define(FASTENER, new CompoundTag());
    }

    @Override
    public void onSyncedDataUpdated(final @NotNull EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (FASTENER.equals(key) && this.level().isClientSide()) {
            this.fastener.load(TagValueInput.create(ProblemReporter.DISCARDING, this.registryAccess(), this.entityData.get(FASTENER)));
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(final double distance) {
        return distance < 4096;
    }

    @Override
    public boolean ignoreExplosion(final @NotNull Explosion explosion) {
        return true;
    }

    @Override
    public boolean canUsePortal(final boolean allowPassengers) {
        return false;
    }

    @Override
    public boolean survives() {
        return !this.level().isLoaded(this.pos) || ConnectionItem.isFence(this.level().getBlockState(this.pos));
    }

    @Override
    public void remove(final RemovalReason reason) {
        if (reason.shouldDestroy()) this.fastener.remove();
        super.remove(reason);
    }

    @Override
    public boolean hurtServer(final ServerLevel level, final DamageSource source, final float amount) {
        if (this.isInvulnerableToBase(source)) return false;
        if (!this.isRemoved()) {
            this.markHurt();
            this.dropItem(level, source.getEntity());
            this.kill(level);
        }

        return true;
    }

    @Override
    public void dropItem(final ServerLevel level, final Entity breaker) {
        this.fastener.dropItems(level, this.pos);
        if (breaker != null) level.levelEvent(2001, this.pos, Block.getId(FLRBlocks.FASTENER.defaultBlockState()));
    }

    public void playPlacementSound() {
        final SoundType sound = FLRBlocks.FASTENER.defaultBlockState().getSoundType();
        this.playSound(sound.getPlaceSound(), (sound.getVolume() + 1) / 2, sound.getPitch() * 0.8F);
    }

    @Override
    public @NotNull SoundSource getSoundSource() {
        return SoundSource.BLOCKS;
    }

    @Override
    protected void recalculateBoundingBox() {
        final double posX = this.pos.getX() + 0.5;
        final double posY = this.pos.getY() + 0.5;
        final double posZ = this.pos.getZ() + 0.5;
        this.setPosRaw(posX, posY, posZ);

        final float w = 3 / 16F;
        final float h = 3 / 16F;
        this.setBoundingBox(new AABB(posX - w, posY - h, posZ - w, posX + w, posY + h, posZ + w));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isRemoved()) return;

        if (this.level() instanceof final ServerLevel level) {
            if (this.fastener.hasNoConnections()) {
                this.dropItem(level, null);
                this.discard();
            } else if (this.fastener.update()) {
                final TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, this.registryAccess());
                this.fastener.save(output);
                this.entityData.set(FASTENER, output.buildResult());
            }
        } else {
            this.fastener.update();
        }
    }

    @Override
    public @NotNull InteractionResult interact(final Player player, final @NotNull InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof final ConnectionItem item) {
            if (!this.level().isClientSide()) {
                item.connect(stack, player, this.level(), this.fastener);
            }
            return InteractionResult.SUCCESS;
        }

        return super.interact(player, hand);
    }

    @Override
    protected void addAdditionalSaveData(final @NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.fastener.save(output.child("fastener"));
    }

    @Override
    protected void readAdditionalSaveData(final @NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.fastener.load(input.childOrEmpty("fastener"));
    }

    public static FenceFastenerEntity create(final Level world, final BlockPos fence) {
        final FenceFastenerEntity fastener = new FenceFastenerEntity(world, fence);
        world.addFreshEntity(fastener);
        fastener.playPlacementSound();
        return fastener;
    }

    public static BlockAttachedEntity findHanging(final Level world, final BlockPos pos) {
        for (final BlockAttachedEntity e : world.getEntitiesOfClass(BlockAttachedEntity.class, new AABB(pos).inflate(2))) {
            if (e.getPos().equals(pos)) return e;
        }
        return null;
    }
}
