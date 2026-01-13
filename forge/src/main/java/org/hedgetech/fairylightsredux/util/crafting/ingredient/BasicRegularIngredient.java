package org.hedgetech.fairylightsredux.util.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.hedgetech.fairylightsredux.util.crafting.GenericRecipe;

import java.util.Collections;
import java.util.Objects;

public class BasicRegularIngredient implements RegularIngredient {
    protected final Ingredient ingredient;

    public BasicRegularIngredient(final Ingredient ingredient) {
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
    }

    @Override
    public final GenericRecipe.MatchResultRegular matches(final ItemStack input) {
        return new GenericRecipe.MatchResultRegular(this, input, this.ingredient.test(input), Collections.emptyList());
    }

    @Override
    public ImmutableList<ItemStack> getInputs() {
        // TODO Deprecated call?
        return this.getMatchingSubtypes(this.ingredient);
    }

    @Override
    public String toString() {
        return this.ingredient.toString();
    }
}
