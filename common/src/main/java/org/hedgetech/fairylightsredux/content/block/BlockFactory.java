package org.hedgetech.fairylightsredux.content.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

@FunctionalInterface
public interface BlockFactory {
    Block create(BlockBehaviour.Properties props, BlockDef def);
}
