package org.hedgetech.fairylightsredux.content.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.content.block.BlockDef;
import org.hedgetech.fairylightsredux.content.item.ItemDef;

import java.util.function.BiConsumer;

public class SelfLootDef implements LootDef {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output, ItemDef item) {
        output.accept(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "items/" + item.id()),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .add(LootItem.lootTableItem(item.asItem()))
                        )
        );
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output, BlockDef block) {
        output.accept(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "blocks/" + block.id()),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .add(LootItem.lootTableItem(block.asItem()))
                        )
        );
    }
}
