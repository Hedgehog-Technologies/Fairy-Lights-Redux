package org.hedgetech.fairylightsredux.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import org.hedgetech.fairylightsredux.content.ItemDefs;
import org.hedgetech.fairylightsredux.content.def.ItemDef;

public final class RecipeGen {
    private final HolderGetter<Item> itemLookup;

    public RecipeGen(HolderGetter<Item> itemLookup) {
        this.itemLookup = itemLookup;
    }

    public void generate(RecipeOutput output) {
        for (ItemDef def : ItemDefs.ITEMS) {
            if (def.recipe() != null) {
                def.recipe().generate(output, this.itemLookup, def);
            }
        }
    }
}
