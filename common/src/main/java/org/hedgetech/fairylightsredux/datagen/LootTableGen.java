package org.hedgetech.fairylightsredux.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.hedgetech.fairylightsredux.content.ItemDefs;
import org.hedgetech.fairylightsredux.content.def.ItemDef;

import java.util.function.BiConsumer;

public final class LootTableGen {
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        for (ItemDef def : ItemDefs.ITEMS) {
            if (def.loot() != null) {
                def.loot().generate(output, def);
            }
        }
    }
}
