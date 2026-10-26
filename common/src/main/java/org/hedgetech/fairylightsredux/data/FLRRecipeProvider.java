package org.hedgetech.fairylightsredux.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.FLRNames;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.item.crafting.DyedShapedRecipe;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class FLRRecipeProvider extends RecipeProvider {
    private final HolderLookup.Provider registries;
    private final RecipeOutput output;

    private FLRRecipeProvider(final HolderLookup.Provider registries, final RecipeOutput output) {
        super(registries, output);
        this.registries = registries;
        this.output = output;
    }

    @Override
    public void buildRecipes() {
        this.light(FLRItems.FAIRY_LIGHT, FLRNames.FAIRY_LIGHT, Map.of('G', this.tag(FLRCraftingRecipes.COLORLESS_GLASS_PANES)), " I ", "IDI", " G ");
        this.light(FLRItems.PAPER_LANTERN, FLRNames.PAPER_LANTERN, Map.of('P', Ingredient.of(Items.PAPER)), " I ", "PDP", "PPP");
        this.light(FLRItems.ORB_LANTERN, FLRNames.ORB_LANTERN, Map.of('S', this.tag(FLRCraftingRecipes.STRINGS), 'W', Ingredient.of(Items.WHITE_WOOL)), " I ", "SDS", " W ");
        this.light(FLRItems.FLOWER_LIGHT, FLRNames.FLOWER_LIGHT, Map.of('R', Ingredient.of(Items.POPPY), 'Y', Ingredient.of(Items.DANDELION), 'B', Ingredient.of(Items.BLUE_ORCHID)), " I ", "RDB", " Y ");
        this.light(FLRItems.CANDLE_LANTERN_LIGHT, FLRNames.CANDLE_LANTERN_LIGHT, Map.of('G', this.tag(FLRCraftingRecipes.GOLD_NUGGETS)), " I ", "GDG", "IGI");
        this.light(FLRItems.OIL_LANTERN_LIGHT, FLRNames.OIL_LANTERN_LIGHT, Map.of('S', Ingredient.of(Items.STICK), 'G', this.tag(FLRCraftingRecipes.COLORLESS_GLASS_PANES)), " I ", "SDS", "IGI");
        this.light(FLRItems.JACK_O_LANTERN, FLRNames.JACK_O_LANTERN, Map.of('S', this.tag(ItemTags.WOODEN_SLABS), 'G', Ingredient.of(Items.TORCH), 'P', Ingredient.of(Items.JACK_O_LANTERN)), " I ", "SDS", "GPG");
        this.light(FLRItems.SKULL_LIGHT, FLRNames.SKULL_LIGHT, Map.of('B', this.tag(FLRCraftingRecipes.BONES)), " I ", "IDI", " B ");
    }

    // Every pennant shares the string loops and paper beside the dye; only the bottom row differs
    private void pennant(final Item pennant, final String name, final String bottom) {
        this.dyed(pennant, 1, name, name, Items.PAPER, Map.of('P', Ingredient.of(Items.PAPER), '-', this.tag(FLRCraftingRecipes.STRINGS)), "- -", "PDP", bottom);
    }

    // Every light shares the iron top and dye center; the rest of the shape is per light. Eight of a
    // light around a dye recolors them, and shares the crafting recipe's group in the recipe book
    private void light(final Item light, final String name, final Map<Character, Ingredient> ingredients, final String... shape) {
        final Map<Character, Ingredient> key = new HashMap<>(ingredients);
        key.put('I', this.tag(FLRCraftingRecipes.IRON_INGOTS));

        this.dyed(light, 4, name, name, Items.IRON_INGOT, key, shape);
        this.dyed(light, 8, name + "_redye", name, light, Map.of('L', Ingredient.of(light)), "LLL", "LDL", "LLL");
    }

    // 'D' in the shape is any dye, and the result takes its color
    private void dyed(final Item item, final int count, final String id, final String group, final Item unlockedBy, final Map<Character, Ingredient> ingredients, final String... shape) {
        final Map<Character, Ingredient> key = new HashMap<>(ingredients);
        key.put('D', this.tag(FLRCraftingRecipes.DYES));

        final ShapedRecipePattern pattern = ShapedRecipePattern.of(key, shape);
        final ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(Registries.RECIPE, Constants.id(id));
        final Advancement.Builder advancement = this.output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey))
                .addCriterion(getHasName(unlockedBy), this.has(unlockedBy))
                .rewards(AdvancementRewards.Builder.recipe(recipeKey))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.output.accept(recipeKey, new DyedShapedRecipe(group, CraftingBookCategory.MISC, pattern, new ItemStack(item, count)), advancement.build(recipeKey.location().withPrefix("recipes/misc/")));
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(final PackOutput output, final CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(final HolderLookup.@NotNull Provider registries, final @NotNull RecipeOutput output) {
            return new FLRRecipeProvider(registries, output);
        }

        @Override
        public @NotNull String getName() {
            return "Fairy Lights Redux Recipes";
        }
    }
}
