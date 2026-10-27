package org.hedgetech.fairylightsredux.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
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
import org.hedgetech.fairylightsredux.server.item.crafting.*;
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
        this.light(FLRItems.GHOST_LIGHT, FLRNames.GHOST_LIGHT, Map.of('P', Ingredient.of(Items.PAPER), 'G', Ingredient.of(Items.WHITE_STAINED_GLASS_PANE)), " I ", "PDP", "IGI");
        this.light(FLRItems.SPIDER_LIGHT, FLRNames.SPIDER_LIGHT, Map.of('W', Ingredient.of(Items.COBWEB), 'S', this.tag(FLRCraftingRecipes.STRINGS), 'E', Ingredient.of(Items.SPIDER_EYE)), " I ", "WDW", "SES");
        this.light(FLRItems.WITCH_LIGHT, FLRNames.WITCH_LIGHT, Map.of('B', Ingredient.of(Items.GLASS_BOTTLE), 'W', Ingredient.of(Items.WHEAT), 'S', Ingredient.of(Items.STICK)), " I ", "BDW", " S ");
        this.light(FLRItems.SNOWFLAKE_LIGHT, FLRNames.SNOWFLAKE_LIGHT, Map.of('S', Ingredient.of(Items.SNOWBALL), 'G', Ingredient.of(Items.WHITE_STAINED_GLASS_PANE)), " I ", "SDS", " G ");
        this.light(FLRItems.HEART_LIGHT, FLRNames.HEART_LIGHT, Map.of('G', Ingredient.of(Items.RED_STAINED_GLASS_PANE)), " I ", "IDI", " G ");
        this.light(FLRItems.MOON_LIGHT, FLRNames.MOON_LIGHT, Map.of('G', Ingredient.of(Items.WHITE_STAINED_GLASS_PANE), 'C', Ingredient.of(Items.CLOCK)), " I ", "GDG", " C ");
        this.light(FLRItems.STAR_LIGHT, FLRNames.STAR_LIGHT, Map.of('P', Ingredient.of(Items.WHITE_STAINED_GLASS_PANE), 'G', this.tag(FLRCraftingRecipes.GOLD_NUGGETS)), " I ", "PDP", " G ");
        this.light(FLRItems.ICICLE_LIGHTS, FLRNames.ICICLE_LIGHTS, Map.of('G', this.tag(FLRCraftingRecipes.COLORLESS_GLASS_PANES), 'B', Ingredient.of(Items.WATER_BUCKET)), " I ", "GDG", " B ");
        this.light(FLRItems.METEOR_LIGHT, FLRNames.METEOR_LIGHT, Map.of('G', this.tag(FLRCraftingRecipes.GLOWSTONE_DUSTS), 'P', Ingredient.of(Items.PAPER)), " I ", "GDG", "IPI");
        this.shaped(RecipeCategory.DECORATIONS, FLRItems.OIL_LANTERN, 4)
                .pattern(" I ")
                .pattern("STS")
                .pattern("IGI")
                .define('I', FLRCraftingRecipes.IRON_INGOTS)
                .define('S', Items.STICK)
                .define('T', Items.TORCH)
                .define('G', FLRCraftingRecipes.COLORLESS_GLASS_PANES)
                .unlockedBy("has_iron", this.has(Items.IRON_INGOT))
                .unlockedBy("has_torch", this.has(Items.TORCH))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, FLRItems.CANDLE_LANTERN, 4)
                .pattern(" I ")
                .pattern("GTG")
                .pattern("IGI")
                .define('I', FLRCraftingRecipes.IRON_INGOTS)
                .define('G', FLRCraftingRecipes.GOLD_NUGGETS)
                .define('T', Items.TORCH)
                .unlockedBy("has_iron", this.has(Items.IRON_INGOT))
                .unlockedBy("has_torch", this.has(Items.TORCH))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, FLRItems.INCANDESCENT_LIGHT, 4)
                .pattern(" I ")
                .pattern("ITI")
                .pattern(" G ")
                .define('I', FLRCraftingRecipes.IRON_INGOTS)
                .define('G', FLRCraftingRecipes.COLORLESS_GLASS_PANES)
                .define('T', Items.TORCH)
                .unlockedBy("has_iron", this.has(Items.IRON_INGOT))
                .unlockedBy("has_torch", this.has(Items.TORCH))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, FLRItems.LETTER_BUNTING)
                .pattern("I-I")
                .pattern("PBF")
                .define('I', FLRCraftingRecipes.IRON_INGOTS)
                .define('-', FLRCraftingRecipes.STRINGS)
                .define('P', Items.PAPER)
                .define('B', Items.INK_SAC)
                .define('F', FLRCraftingRecipes.FEATHERS)
                .unlockedBy("has_iron", this.has(Items.IRON_INGOT))
                .unlockedBy("has_string", this.has(Items.STRING))
                .save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, FLRItems.GARLAND, 2)
                .pattern("I-I")
                .define('I', FLRCraftingRecipes.IRON_INGOTS)
                .define('-', Items.VINE)
                .unlockedBy("has_iron", this.has(Items.IRON_INGOT))
                .unlockedBy("has_vine", this.has(Items.VINE))
                .save(this.output);
        this.dyed(FLRItems.TINSEL, 1, FLRNames.TINSEL, FLRNames.TINSEL, Items.PAPER, Map.of('P', Ingredient.of(Items.PAPER), 'I', this.tag(FLRCraftingRecipes.IRON_INGOTS), '-', this.tag(FLRCraftingRecipes.STRINGS)), " P ", "I-I", " D ");
        this.pennant(FLRItems.TRIANGLE_PENNANT, FLRNames.TRIANGLE_PENNANT, " P ");
        this.pennant(FLRItems.SPEARHEAD_PENNANT, FLRNames.SPEARHEAD_PENNANT, " PP");
        this.pennant(FLRItems.SWALLOWTAIL_PENNANT, FLRNames.SWALLOWTAIL_PENNANT, "P P");
        this.pennant(FLRItems.SQUARE_PENNANT, FLRNames.SQUARE_PENNANT, "PPP");
        SpecialRecipeBuilder.special(HangingLightsRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id(FLRNames.HANGING_LIGHTS)));
        SpecialRecipeBuilder.special(HangingLightsAugmentationRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id(FLRNames.HANGING_LIGHTS_AUGMENTATION)));
        SpecialRecipeBuilder.special(PennantBuntingRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id(FLRNames.PENNANT_BUNTING)));
        SpecialRecipeBuilder.special(PennantBuntingAugmentationRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id(FLRNames.PENNANT_BUNTING_AUGMENTATION)));
        SpecialRecipeBuilder.special(LightTwinkleRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id(FLRNames.LIGHT_TWINKLE)));
        SpecialRecipeBuilder.special(ColorChangingLightRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id(FLRNames.COLOR_CHANGING_LIGHT)));
        SpecialRecipeBuilder.special(EditColorRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id(FLRNames.EDIT_COLOR)));
        SpecialRecipeBuilder.special(CopyColorRecipe::new).save(this.output, ResourceKey.create(Registries.RECIPE, Constants.id(FLRNames.COPY_COLOR)));
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
