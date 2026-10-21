package org.hedgetech.fairylightsredux.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.fastener.BlockFastener;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.fastener.IFastenerHolder;
import org.hedgetech.fairylightsredux.server.block.FastenerBlock;
import org.jetbrains.annotations.NotNull;

public final class FastenerBlockEntity extends BlockEntity implements IFastenerHolder {
    private final BlockFastener fastener;

    public FastenerBlockEntity(final BlockPos pos, final BlockState state) {
        super(FLRBlockEntities.FASTENER, pos, state);
        this.fastener = new BlockFastener(this);
    }

    @Override
    public Fastener<?> getFastener() {
        return this.fastener;
    }

    public Vec3 getOffset() {
        return FastenerBlock.getFastenerOffset(this.getFacing(), 0.125F);
    }

    public Direction getFacing() {
        final BlockState state = this.getBlockState();
        if (!state.hasProperty(FastenerBlock.FACING)) return Direction.UP;
        return state.getValue(FastenerBlock.FACING);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(final HolderLookup.@NotNull Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public void setLevel(final @NotNull Level world) {
        super.setLevel(world);
        this.fastener.setWorld(world);
    }

    public static void tick(final Level world, final BlockPos pos, final BlockState state, final FastenerBlockEntity be) {
        if (be.fastener.hasNoConnections()) {
            world.removeBlock(pos, false);
        } else if (be.fastener.update()) {
            be.setChanged();
            world.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
        }
    }

    public static void tickClient(final Level world, final BlockPos pos, final BlockState state, final FastenerBlockEntity be) {
        be.fastener.update();
    }

    @Override
    public void preRemoveSideEffects(final @NotNull BlockPos pos, final @NotNull BlockState state) {
        this.fastener.dropItems(this.level, pos);
        this.fastener.remove();
    }

    @Override
    protected void saveAdditional(final @NotNull ValueOutput output) {
        super.saveAdditional(output);
        this.fastener.save(output.child("fastener"));
    }

    @Override
    protected void loadAdditional(final @NotNull ValueInput input) {
        super.loadAdditional(input);
        this.fastener.load(input.childOrEmpty("fastener"));
    }
}
