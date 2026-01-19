package org.hedgetech.fairylightsredux.content.loot;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.hedgetech.fairylightsredux.content.block.BlockDef;
import org.hedgetech.fairylightsredux.content.item.ItemDef;

import java.util.function.BiConsumer;

public interface LootDef {
    void generate(BiConsumer<ResourceLocation, LootTable.Builder> output, ItemDef item);

    void generate(BiConsumer<ResourceLocation, LootTable.Builder> output, BlockDef block);
}
