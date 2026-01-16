package org.hedgetech.fairylightsredux.content.def;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.function.BiConsumer;

public interface LootDef {
    void generate(BiConsumer<ResourceLocation, LootTable.Builder> output, ItemDef item);
}
