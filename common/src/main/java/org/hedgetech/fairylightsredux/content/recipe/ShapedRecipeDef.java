package org.hedgetech.fairylightsredux.content.recipe;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.hedgetech.fairylightsredux.content.block.BlockDef;
import org.hedgetech.fairylightsredux.content.item.ItemDef;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ShapedRecipeDef implements RecipeDef {
    private final List<String> pattern;
    private final Map<Character, Supplier<ItemLike>> ingredients;
    private final Map<String, Criterion<?>> criteria;

    public ShapedRecipeDef(List<String> pattern, Map<Character, Supplier<ItemLike>> ingredients, Map<String, Criterion<?>> criteria) {
        this.pattern = pattern;
        this.ingredients = ingredients;
        this.criteria = criteria;
    }

    @Override
    public void generate(RecipeOutput output, HolderGetter<Item> lookup, ItemDef item) {
        generateInternal(output, lookup, item, item.category());
    }

    @Override
    public void generate(RecipeOutput output, HolderGetter<Item> lookup, BlockDef block) {
        generateInternal(output, lookup, block, block.category());
    }

    private void generateInternal(RecipeOutput output, HolderGetter<Item> lookup, ItemLike result) {
        generateInternal(output, lookup, result, RecipeCategory.MISC);
    }

    private void generateInternal(RecipeOutput output, HolderGetter<Item> lookup, ItemLike result, RecipeCategory category) {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(
                lookup,
                category,
                result
        );

        for (String row : this.pattern) {
            builder.pattern(row);
        }

        this.ingredients.forEach((key, ing) ->
                builder.define(key, ing.get())
        );

        this.criteria.forEach(builder::unlockedBy);

        builder.save(output);
    }
}
