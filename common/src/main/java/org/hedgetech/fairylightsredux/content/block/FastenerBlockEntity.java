package org.hedgetech.fairylightsredux.content.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.fastener.FastenerHolder;
import org.hedgetech.fairylightsredux.registry.FLRBlockEntities;
import org.hedgetech.fairylightsredux.registry.FLRBlocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public final class FastenerBlockEntity extends BlockEntity implements FastenerHolder {
    private Fastener<?> fastener;

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

    // TODO - Figure out compound tag usage
//    @Override
//    public CompoundTag getUpdateTag() {
//        return this.saveWithoutMetadata();
//    }

    @Override
    public void setLevel(final @NotNull Level world) {
        super.setLevel(world);
        // FIXME - figure out how to store fastener reference now
//        this.getFastener().ifPresent(fastener -> fastener.setWorld(world));
    }

    public static void tick(Level world, BlockPos pos, BlockState state, FastenerBlockEntity fbe) {
        throw new NotImplementedException("FastenerBlockEntity.tick");
        // FIXME - figure out how to store fastener reference now
//        fbe.getFastener().ifPresent(fastener -> {
//            if (!world.isClientSide() && fastener.hasNoConnections()) {
//                world.removeBlock(pos, false);
//            } else if (!world.isClientSide() && fastener.update()) {
//                fbe.setChanged();
//                world.sendBlockUpdated(pos, state, state, 3);
//            }
//        });
    }

    public static void tickClient(Level world, BlockPos pos, BlockState state, FastenerBlockEntity fbe) {
        throw new NotImplementedException("FastenerBlockEntity.tickClient");
        // FIXME - figure out how to store fastener reference now
//        fbe.getFastener().ifPresent(Fastener::update);
    }

    @Override
    public void setRemoved() {
        // FIXME - figure out how to store fastener reference now
//        this.getFastener().ifPresent(Fastener::remove);
        super.setRemoved();
    }

    private Optional<Fastener<?>> getFastener() {
        throw new NotImplementedException("FastenerBlockEntity.getFastener");
        // FIXME - figure out how to store fastener reference now
//        return this.getCapability(CapabilityHandler.FASTENER_CAP);
    }

    @Override
    public Fastener<?> getFastener() {
        if (this.fastener == null) {
            this.fastener = createFastener();
        }
        return this.fastener;
    }

    private Fastener<?> createFastener() {
        
    }
}
