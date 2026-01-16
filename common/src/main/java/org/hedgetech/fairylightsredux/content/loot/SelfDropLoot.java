package org.hedgetech.fairylightsredux.content.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.content.def.ItemDef;
import org.hedgetech.fairylightsredux.content.def.LootDef;

import java.util.function.BiConsumer;

public class SelfDropLoot implements LootDef {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output, ItemDef item) {
        output.accept(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "blocks/" + item.name()),
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .add(LootItem.lootTableItem(item.asItem()))
                        )
        );
    }
}
