package org.hedgetech.fairylightsredux.fastener;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.content.block.FastenerBlockEntity;
import org.hedgetech.fairylightsredux.fastener.accessor.BlockFastenerAccessor;

import java.util.Objects;

public final class BlockFastener extends AbstractFastener<BlockFastenerAccessor> {
    private final FastenerBlockEntity fastener;
    private final BlockView view;

    public BlockFastener(final FastenerBlockEntity fastener, final BlockView view) {
        this.fastener = fastener;
        this.view = view;
        this.bounds = new AABB(fastener.getBlockPos());
        this.setWorld(fastener.getLevel());
    }

    @Override
    public Direction getFacing() {
        return this.fastener.getFacing();
    }

    @Override
    public boolean isMoving() {
        return this.view.isMoving(this.getWorld(), this.fastener.getBlockPos());
    }

    @Override
    public BlockPos getPos() {
        return this.fastener.getBlockPos();
    }

    @Override
    public Vec3 getConnectionPoint() {
        return this.view.getPos(this.getWorld(), this.fastener.getBlockPos(), Vec3.atLowerCornerOf(this.getPos()).add(Objects.requireNonNull(this.fastener.getOffset())));
    }

    @Override
    public BlockFastenerAccessor createAccessor() {
        return new BlockFastenerAccessor(this);
    }
}
