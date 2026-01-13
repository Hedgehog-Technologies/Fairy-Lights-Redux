package org.hedgetech.fairylightsredux.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import org.hedgetech.fairylightsredux.Constants;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GenericRecipeBuilder implements RecipeBuilder {
    private final Recipe<?> recipe;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private final Item resultItem;

    @Nullable
    private String group;

    public GenericRecipeBuilder(Recipe<?> recipe, Item resultItem) {
        this.recipe = Objects.requireNonNull(recipe, "recipe");
        this.resultItem = Objects.requireNonNull(resultItem, "resultItem");
    }

    @Override
    public @NonNull RecipeBuilder unlockedBy(@NonNull String name, @NonNull Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public @NonNull RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public @NonNull Item getResult() {
        return this.resultItem;
    }

    public void save(RecipeOutput output, ResourceLocation id) {
        this.save(output, ResourceKey.create(Registries.RECIPE, id));
    }

    @Override
    public void save(@NonNull RecipeOutput output, @NonNull ResourceKey<Recipe<?>> key) {
        Advancement.Builder advBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
                .rewards(AdvancementRewards.Builder.recipe(key))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advBuilder::addCriterion);
        ResourceLocation advId = key.location().withPrefix("recipes/" + Constants.MOD_ID + "/");
        output.accept(key, this.recipe, advBuilder.build(advId));
    }

    public static GenericRecipeBuilder customRecipe(Recipe<?> recipe, Item resultItem) {
        return new GenericRecipeBuilder(recipe, resultItem);
    }
}
