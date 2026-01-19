package org.hedgetech.fairylightsredux.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.hedgetech.fairylightsredux.content.block.entity.BlockEntityDef;

import java.util.function.Supplier;

public interface RegistryBridge {
    Item registerItem(String name, Supplier<Item> item);

    Block registerBlock(String name, Supplier<Block> block);

    BlockItem registerBlockItem(String name, Supplier<BlockItem> blockItem);

    <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(String name, BlockEntityDef<T> def, Supplier<Block[]> validBlocks);
}
