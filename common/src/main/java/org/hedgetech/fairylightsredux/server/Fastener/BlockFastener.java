package org.hedgetech.fairylightsredux.server.Fastener;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.Fastener.accessor.BlockFastenerAccessor;
import org.hedgetech.fairylightsredux.server.block.entity.FastenerBlockEntity;

public final class BlockFastener extends Fastener<BlockFastenerAccessor> {
    private final FastenerBlockEntity fastener;

    public BlockFastener(final FastenerBlockEntity fastener) {
        this.fastener = fastener;
        this.bounds = new AABB(fastener.getBlockPos());
    }

    @Override
    public Direction getFacing() {
        return this.fastener.getFacing();
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public BlockPos getPos() {
        return this.fastener.getBlockPos();
    }

    @Override
    public Vec3 getConnectionPoint() {
        return Vec3.atLowerCornerOf(this.getPos()).add(this.fastener.getOffset());
    }

    @Override
    public BlockFastenerAccessor createAccessor() {
        return new BlockFastenerAccessor(this);
    }
}
