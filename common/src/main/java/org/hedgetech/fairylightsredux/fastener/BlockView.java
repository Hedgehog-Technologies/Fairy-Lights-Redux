package org.hedgetech.fairylightsredux.fastener;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.util.matrix.Matrix;

public interface BlockView {
    boolean isMoving(final Level world, final BlockPos sourcePos);

    Vec3 getPos(final Level world, final BlockPos sourcePos, final Vec3 pos);

    void unrotate(final Level world, final BlockPos sourcePos, final Matrix matrix, final float delta);
}
