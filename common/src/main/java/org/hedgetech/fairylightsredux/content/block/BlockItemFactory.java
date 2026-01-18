package org.hedgetech.fairylightsredux.content.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

@FunctionalInterface
public interface BlockItemFactory {
    BlockItem create(Block block, Item.Properties props, BlockDef blockDef);
}
