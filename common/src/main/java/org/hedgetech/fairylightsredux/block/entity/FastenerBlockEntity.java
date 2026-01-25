package org.hedgetech.fairylightsredux.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.block.FastenerBlock;
import org.hedgetech.fairylightsredux.fastener.BlockFastener;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.fastener.RegularBlockView;
import org.hedgetech.fairylightsredux.registry.FLRBlockEntities;
import org.hedgetech.fairylightsredux.registry.FLRBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class FastenerBlockEntity extends BlockEntity {
    private BlockFastener fastener;

    public FastenerBlockEntity(final BlockPos pos, final BlockState state) {
        super(FLRBlockEntities.get("fastener"), pos, state);
    }

    // FIXME - Figure out where this has moved to
//    @Override
//    public AABB getRenderBoundingBox() {
//        return this.getFastener().map(fastener -> fastener.getBounds().inflate(1)).orElseGet(super::getRenderBoundingBox);
//    }

    @Nullable
    public Vec3 getOffset() {
        Block block = FLRBlocks.get("fastener");
        if (!(block instanceof FastenerBlock fb)) return null;
        return fb.getOffset(this.getFacing(), 0.125F);
    }

    public Direction getFacing() {
        final BlockState state = Objects.requireNonNull(this.level).getBlockState(this.worldPosition);
        if (state.getBlock() != FLRBlocks.get("fastener")) {
            return Direction.UP;
        }
        return state.getValue(FastenerBlock.FACING);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void setLevel(final @NotNull Level world) {
        super.setLevel(world);
        this.getFastener().setWorld(world);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, FastenerBlockEntity fbe) {
        Fastener<?> fastener = fbe.getFastener();
        if (!world.isClientSide() && fastener.hasNoConnections()) {
            world.removeBlock(pos, false);
        } else if (!world.isClientSide() && fastener.update()) {
            fbe.setChanged();
            world.sendBlockUpdated(pos, state, state, 3);
        }
    }

    public static void tickClient(Level world, BlockPos pos, BlockState state, FastenerBlockEntity fbe) {
        fbe.getFastener().update();
    }

    @Override
    public void setRemoved() {
        this.getFastener().remove();
        super.setRemoved();
    }

    public Fastener<?> getFastener() {
        if (this.fastener == null) {
            this.fastener = new BlockFastener(this, new RegularBlockView());
        }
        return this.fastener;
    }
}
