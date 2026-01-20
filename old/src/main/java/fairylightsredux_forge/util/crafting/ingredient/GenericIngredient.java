package org.hedgetech.fairylightsredux.util.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.hedgetech.fairylightsredux.util.crafting.GenericRecipe;

import java.util.List;
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

    @Deprecated // CompoundTag deprecation
    default void present(final CompoundTag nbt) {}

    @Deprecated // CompoundTag deprecation
    default void absent(final CompoundTag nbt) {}

    @Deprecated // When is this used? Fix and / or replace when we figure it out
    default ImmutableList<ItemStack> getMatchingSubtypes(final Ingredient stack) {
        Objects.requireNonNull(stack, "stack");
        return ImmutableList.of();
    }

    default void addTooltip(final List<Component> tooltip) {}
}
