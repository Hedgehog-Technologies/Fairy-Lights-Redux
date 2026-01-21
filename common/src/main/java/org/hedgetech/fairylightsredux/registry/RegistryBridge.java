package org.hedgetech.fairylightsredux.registry;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.hedgetech.fairylightsredux.content.block.entity.BlockEntityDef;
import org.hedgetech.fairylightsredux.content.sound.SoundDef;

import java.util.function.Supplier;

public interface RegistryBridge {
    Item registerItem(String name, Supplier<Item> item);

    Block registerBlock(String name, Supplier<Block> block);

    BlockItem registerBlockItem(String name, Supplier<BlockItem> blockItem);

    <T extends Entity> EntityType<T> registerEntityType(String name, EntityType<T> entityType);

    <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(String name, BlockEntityDef<T> def, Supplier<Block[]> validBlocks);

    SoundEvent registerSound(String name, SoundDef sound);
}
