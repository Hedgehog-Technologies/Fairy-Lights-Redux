package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.jetbrains.annotations.NotNull;

public class EditColorRecipe extends CustomRecipe {
    public EditColorRecipe(final CraftingBookCategory category) {
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
        ItemStack item = ItemStack.EMPTY;
        int red = 0, green = 0, blue = 0, brightness = 0, count = 0;

        for (int i = 0; i < input.size(); i++) {
            final ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(FLRCraftingRecipes.DYEABLE) && item.isEmpty()) {
                item = stack;
            } else if (DyeableItem.getDyeColor(stack) instanceof DyeColor dye) {
                final int color = DyeableItem.getColor(dye);
                final int r = color >> 16 & 0xFF;
                final int g = color >> 8 & 0xFF;
                final int b = color & 0xFF;
                red += r;
                green += g;
                blue += b;
                brightness += Math.max(r, Math.max(g, b));
                count++;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (item.isEmpty() || count == 0) return ItemStack.EMPTY;

        final int max = count * Math.max(red, Math.max(green, blue));
        final int color = red * brightness / max << 16 | green * brightness / max << 8 | blue * brightness / max;

        return DyeableItem.setColor(item.copyWithCount(1), color);
    }

    @Override
    public @NotNull RecipeSerializer<EditColorRecipe> getSerializer() {
        return FLRCraftingRecipes.EDIT_COLOR;
    }
}
