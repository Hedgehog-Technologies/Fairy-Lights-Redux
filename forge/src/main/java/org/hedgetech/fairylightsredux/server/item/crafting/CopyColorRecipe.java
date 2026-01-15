package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.jspecify.annotations.NonNull;

public class CopyColorRecipe extends CustomRecipe {
    public CopyColorRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(final CraftingInput input, final @NonNull Level world) {
        int count = 0;
        for (int i = 0; i < input.size(); i++) {
            final ItemStack stack = input.getItem(i);
            if (!stack.isEmpty() && (!stack.is(FLRCraftingRecipes.DYEABLE) || count++ >= 2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NonNull ItemStack assemble(final CraftingInput input, final HolderLookup.@NonNull Provider registries) {
        ItemStack original = ItemStack.EMPTY;
        for (int i = 0; i < input.size(); i++) {
            final ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (!stack.is(FLRCraftingRecipes.DYEABLE)) break;
            if (original.isEmpty()) {
                original = stack;
            } else {
                final ItemStack copy = stack.copy();
                copy.setCount(1);
                DyeableItem.setColor(copy, DyeableItem.getColor(original));
                return copy;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NonNull NonNullList<ItemStack> getRemainingItems(final @NonNull CraftingInput input) {
        ItemStack original = ItemStack.EMPTY;
        final NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            final ItemStack stack = input.getItem(i);
            if (!stack.getCraftingRemainder().isEmpty()) {
                remaining.set(i, stack.getCraftingRemainder());
            } else if (original.isEmpty() && !stack.isEmpty() && stack.is(FLRCraftingRecipes.DYEABLE)) {
                final ItemStack rem = stack.copy();
                rem.setCount(1);
                remaining.set(i, rem);
                original = stack;
            }
        }
        return remaining;
    }

    public boolean canCraftInDimensions(final int width, final int height) {
        return width * height >= 2;
    }

    @Override
    public @NonNull RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return FLRCraftingRecipes.COPY_COLOR.get();
    }
}
