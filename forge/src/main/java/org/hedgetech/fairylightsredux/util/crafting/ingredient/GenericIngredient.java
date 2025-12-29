package org.hedgetech.fairylightsredux.util.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.hedgetech.fairylightsredux.util.crafting.GenericRecipe;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

public interface GenericIngredient<I extends GenericIngredient<I, M>, M extends GenericRecipe.MatchResult<I, M>> {
    ImmutableList<ItemStack> getInputs();

    default ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
        return ImmutableList.of(this.getInputs());
    }

    M matches(final ItemStack input);

    default boolean dictatesOutputType() {
        return false;
    }

    default void present(final CompoundTag nbt) {}

    default void absent(final CompoundTag nbt) {}

    default ImmutableList<ItemStack> getMatchingSubtypes(final Ingredient stack) {
        Objects.requireNonNull(stack, "stack");
        return ImmutableList.copyOf(stack.getItems());
    }
}
