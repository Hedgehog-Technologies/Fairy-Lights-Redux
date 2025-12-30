package org.hedgetech.fairylightsredux.util.crafting.ingredient;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.util.crafting.GenericRecipe;

public interface RegularIngredient extends GenericIngredient<RegularIngredient, GenericRecipe.MatchResultRegular> {
    @Deprecated
    default void matched(final ItemStack ingredient, final CompoundTag nbt) {}
}
