package org.hedgetech.fairylightsredux.content.recipe;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.Item;
import org.hedgetech.fairylightsredux.content.block.BlockDef;
import org.hedgetech.fairylightsredux.content.item.ItemDef;

public interface RecipeDef {
    void generate(RecipeOutput output, HolderGetter<Item> lookup, ItemDef item);

    void generate(RecipeOutput output, HolderGetter<Item> lookup, BlockDef block);
}
