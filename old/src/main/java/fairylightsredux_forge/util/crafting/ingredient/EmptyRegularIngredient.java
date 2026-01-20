package org.hedgetech.fairylightsredux.util.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.util.crafting.GenericRecipe;

import java.util.Collections;

public class EmptyRegularIngredient implements RegularIngredient {
    @Override
    public GenericRecipe.MatchResultRegular matches(final ItemStack input) {
        return new GenericRecipe.MatchResultRegular(this, input, input.isEmpty(), Collections.emptyList());
    }

    @Override
    public ImmutableList<ItemStack> getInputs() {
        return ImmutableList.of();
    }
}
