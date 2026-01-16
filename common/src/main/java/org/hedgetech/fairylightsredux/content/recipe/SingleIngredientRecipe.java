package org.hedgetech.fairylightsredux.content.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.hedgetech.fairylightsredux.content.def.ItemDef;
import org.hedgetech.fairylightsredux.content.def.RecipeDef;

import java.util.function.Supplier;

public final class SingleIngredientRecipe implements RecipeDef {
    private final RecipeCategory category;
    private final Supplier<ItemLike> ingredient;

    public SingleIngredientRecipe(RecipeCategory category, Supplier<ItemLike> ingredient) {
        this.category = category;
        this.ingredient = ingredient;
    }

    @Override
    public void generate(RecipeOutput output, HolderGetter<Item> lookup, ItemDef item) {
        ShapedRecipeBuilder.shaped(lookup, item.category(), item.asItem())
                .pattern("X")
                .define('X', this.ingredient.get())
                .unlockedBy("has_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(this.ingredient.get()))
                .save(output);
    }
}
