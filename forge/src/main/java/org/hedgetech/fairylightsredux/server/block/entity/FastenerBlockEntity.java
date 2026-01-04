package org.hedgetech.fairylightsredux.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.block.FastenerBlock;
import org.hedgetech.fairylightsredux.server.capability.CapabilityHandler;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.jspecify.annotations.NonNull;

public final class FastenerBlockEntity extends BlockEntity {
    public FastenerBlockEntity(final BlockPos pos, final BlockState state) {
        super(FLRBlockEntities.FASTENER.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return this.getFastener().map(fastener -> fastener.getBounds().inflate(1)).orElseGet(super::getRenderBoundingBox);
    }

//    @Override
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        return ClientboundBlockEntityDataPacket.create(this);
//    }
//
//    @Override
//    public CompoundTag getUpdateTag() {
//        return this.saveWithoutMetadata();
//    }

    @Override
    public void setLevel(final @NonNull Level world) {
        super.setLevel(world);
        this.getFastener().ifPresent(fastener -> fastener.setWorld(world));
    }

    @Override
    public void setRemoved() {
        this.getFastener().ifPresent(Fastener::remove);
        super.setRemoved();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, FastenerBlockEntity fbe) {
        fbe.getFastener().ifPresent(fastener -> {
            if (!level.isClientSide() && fastener.hasNoConnections()) {
                level.removeBlock(pos, false);
            } else if (!level.isClientSide() && fastener.update()) {
                fbe.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }
        });
    }

    public static void tickClient(Level level, BlockPos pos, BlockState state, FastenerBlockEntity fbe) {
        fbe.getFastener().ifPresent(Fastener::update);
    }

    public Vec3 getOffset() {
        return FLRBlocks.FASTENER.get().getOffset(this.getFacing(), 0.125F);
    }

    public Direction getFacing() {
        assert this.level != null;
        final BlockState state = this.level.getBlockState(this.worldPosition);
        if (state.getBlock() != FLRBlocks.FASTENER.get()) {
            return Direction.UP;
        }
        return state.getValue(FastenerBlock.FACING);
    }

    private LazyOptional<Fastener<?>> getFastener() {
        return this.getCapability(CapabilityHandler.FASTENER_CAP);
    }
}
