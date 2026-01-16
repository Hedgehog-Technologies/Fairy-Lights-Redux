package org.hedgetech.fairylightsredux.content.def;

import net.minecraft.data.recipes.RecipeOutput;

public interface RecipeDef {
    void generate(RecipeOutput output, ItemDef item);
}
