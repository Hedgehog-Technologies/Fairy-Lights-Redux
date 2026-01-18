package org.hedgetech.fairylightsredux.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public interface RegistryBridge {
    Item registerItem(String name, Supplier<Item> item);

    Block registerBlock(String name, Supplier<Block> block);

    BlockItem registerBlockItem(String name, Supplier<BlockItem> blockItem);
}
