package org.hedgetech.fairylightsredux.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.hedgetech.fairylightsredux.content.item.ItemDefs;
import org.hedgetech.fairylightsredux.content.item.ItemDef;

import java.util.function.BiConsumer;

public final class LootTableGen {
    public static void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        for (ItemDef def : ItemDefs.ITEMS) {
            if (def.loot() != null) {
                def.loot().generate(output, def);
            }
        }
    }

    private LootTableGen() {}
}
