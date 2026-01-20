package org.hedgetech.fairylightsredux.data;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ForgeFLRRecipeProvider extends RecipeProvider {
    ForgeFLRRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        // TODO - LOOK INTO NBT STUFF FROM PAST
        this.shaped(RecipeCategory.DECORATIONS, FLRItems.LETTER_BUNTING.get())
                .define('I', Tags.Items.INGOTS_IRON)
                .define('-', Tags.Items.STRINGS)
                .define('P', Items.PAPER)
                .define('B', Items.INK_SAC)
                .define('F', Tags.Items.FEATHERS)
                .pattern("I-I")
                .pattern("PBF")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_string", has(Tags.Items.STRINGS))
                .save(this.output);

        this.shaped(RecipeCategory.DECORATIONS, FLRItems.GARLAND.get(), 2)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('-', Items.VINE)
                .pattern("I-I")
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_vine", this.has(Items.VINE))
                .save(this.output);

        this.shaped(RecipeCategory.DECORATIONS, FLRItems.OIL_LANTERN.get(), 4)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('S', Items.STICK)
                .define('T', Items.TORCH)
                .define('G', Tags.Items.GLASS_PANES_COLORLESS)
                .pattern(" I ")
                .pattern("STS")
                .pattern("IGI")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_torch", has(Items.TORCH))
                .save(this.output);

        this.shaped(RecipeCategory.DECORATIONS, FLRItems.CANDLE_LANTERN.get(), 4)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.NUGGETS_GOLD)
                .define('T', Items.TORCH)
                .pattern(" I ")
                .pattern("GTG")
                .pattern("IGI")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_torch", has(Items.TORCH))
                .save(this.output);

        this.shaped(RecipeCategory.DECORATIONS, FLRItems.INCANDESCENT_LIGHT.get(), 4)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('G', Tags.Items.GLASS_PANES_COLORLESS)
                .define('T', Items.TORCH)
                .pattern(" I ")
                .pattern("ITI")
                .pattern(" G ")
                .unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_torch", has(Items.TORCH))
                .save(this.output);

        GenericRecipeBuilder.customRecipe(FLRCraftingRecipes.HANGING_LIGHTS.get(), FLRItems.HANGING_LIGHTS.get())
                .unlockedBy("has_lights", has(FLRCraftingRecipes.LIGHTS))
                .save(this.output, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hanging_lights"));
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput output) {
            return new ForgeFLRRecipeProvider(provider, output);
        }

        @Override
        public @NonNull String getName() {
            return ForgeFLRRecipeProvider.class.getSimpleName();
        }
    }
}
