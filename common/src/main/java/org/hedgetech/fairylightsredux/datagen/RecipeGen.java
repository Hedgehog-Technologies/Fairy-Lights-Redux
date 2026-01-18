package org.hedgetech.fairylightsredux.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import org.hedgetech.fairylightsredux.content.block.BlockDef;
import org.hedgetech.fairylightsredux.content.block.BlockDefs;
import org.hedgetech.fairylightsredux.content.item.ItemDefs;
import org.hedgetech.fairylightsredux.content.item.ItemDef;

public final class RecipeGen {
    public static void generate(RecipeOutput output, HolderGetter<Item> itemLookup) {
        for (ItemDef def : ItemDefs.ITEMS) {
            if (def.recipe() != null) {
                def.recipe().generate(output, itemLookup, def);
            }
        }

        for (BlockDef def : BlockDefs.BLOCKS) {
            if (def.recipe() == null) continue;

            def.recipe().generate(output, itemLookup, def);
        }
    }


    private RecipeGen() {}
}
