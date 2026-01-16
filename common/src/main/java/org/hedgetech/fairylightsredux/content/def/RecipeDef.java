package org.hedgetech.fairylightsredux.content.def;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;

public interface RecipeDef {
    void generate(RecipeOutput output, HolderGetter<Item> lookup, ItemDef item);
}
