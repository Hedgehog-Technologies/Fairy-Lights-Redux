package org.hedgetech.fairylightsredux.content.recipe;

import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import org.hedgetech.fairylightsredux.content.def.ItemDef;
import org.hedgetech.fairylightsredux.content.def.RecipeDef;

import java.util.function.Supplier;

public final class SingleIngredientRecipe implements RecipeDef {
    private final Supplier<ItemLike> ingredient;

    public SingleIngredientRecipe(Supplier<ItemLike> ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public void generate(RecipeOutput output, ItemDef item) {
        ShapedRecipeBuilder.shaped(item.asItem(), item.category())
                .pattern("X")
        ;
    }
}
