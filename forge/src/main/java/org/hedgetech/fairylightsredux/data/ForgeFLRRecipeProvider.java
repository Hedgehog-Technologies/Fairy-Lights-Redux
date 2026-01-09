package org.hedgetech.fairylightsredux.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public final class ForgeFLRRecipeProvider extends RecipeProvider {
    ForgeFLRRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        this.shaped(RecipeCategory.DECORATIONS, FLRItems.GARLAND.get(), 2)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('-', Items.VINE)
                .pattern("I-I")
                .unlockedBy("has_iron", this.has(Tags.Items.INGOTS_IRON))
                .unlockedBy("has_vine", this.has(Items.VINE))
                .save(this.output);
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
