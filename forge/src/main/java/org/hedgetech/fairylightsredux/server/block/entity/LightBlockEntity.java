package org.hedgetech.fairylightsredux.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.hedgetech.fairylightsredux.server.feature.light.Light;

public class LightBlockEntity extends BlockEntity {
    private Light<?> light;
    private boolean on = true;

    public LightBlockEntity(BlockPos pos, BlockState state) {
        super()
    }
}
