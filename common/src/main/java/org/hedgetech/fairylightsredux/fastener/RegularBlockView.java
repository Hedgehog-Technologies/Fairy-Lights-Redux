package org.hedgetech.fairylightsredux.fastener;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.util.matrix.Matrix;

public class RegularBlockView implements BlockView {
    @Override
    public boolean isMoving(Level world, BlockPos sourcePos) {
        return false;
    }

    @Override
    public Vec3 getPos(Level world, BlockPos sourcePos, Vec3 pos) {
        return pos;
    }

    @Override
    public void unrotate(Level world, BlockPos sourcePos, Matrix matrix, float delta) {}
}
