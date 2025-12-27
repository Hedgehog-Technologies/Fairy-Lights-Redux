package org.hedgetech.fairylightsredux.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;

public final class FastenerBlockEntity extends BlockEntity {
    public FastenerBlockEntity(final BlockPos pos, final BlockState state) {
        super(FLRBlockEntities.FASTENER.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return this.getFastener().map(fastener -> fastener.getBounds().inflate(1)).orElseGet(super::getRenderBoundingBox);
    }

    public Vec3 getOffset() {
        return FLRBlocks.FASTENER.get().getOffset(this.getFacing(), 0.125F);
    }
}
