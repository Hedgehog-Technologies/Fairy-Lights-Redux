package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.jetbrains.annotations.NotNull;

public class CopyColorRecipe extends CustomRecipe {
    public CopyColorRecipe(final CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(final @NotNull CraftingInput input, final @NotNull Level world) {
        return !this.craft(input).isEmpty();
    }

    @Override
    public @NotNull ItemStack assemble(final @NotNull CraftingInput input, final HolderLookup.@NotNull Provider registries) {
        return this.craft(input);
    }

    private ItemStack craft(final CraftingInput input) {
        ItemStack original = ItemStack.EMPTY;
        ItemStack target = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            final ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;
            if (!stack.is(FLRCraftingRecipes.DYEABLE)) return ItemStack.EMPTY;

            if (original.isEmpty()) {
                original = stack;
            } else if (target.isEmpty()) {
                target = stack;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (target.isEmpty()) return ItemStack.EMPTY;

        return DyeableItem.setColor(target.copyWithCount(1), DyeableItem.getColor(original));
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(final @NotNull CraftingInput input) {
        final NonNullList<ItemStack> remaining = CraftingRecipe.defaultCraftingReminder(input);

        for (int i = 0; i < input.size(); i++) {
            final ItemStack stack = input.getItem(i);

            if (stack.isEmpty()) {
                remaining.set(i, stack.copyWithCount(1));
                break;
            }
        }

        return remaining;
    }

    @Override
    public @NotNull RecipeSerializer<CopyColorRecipe> getSerializer() {
        return FLRCraftingRecipes.COPY_COLOR;
    }
}
