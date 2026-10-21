package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ColorChangingLightRecipe extends CustomRecipe {
    public ColorChangingLightRecipe(final CraftingBookCategory category) {
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
        ItemStack light = ItemStack.EMPTY;
        int lightX = 0, lightY = 0;
        int nuggetX = -1, nuggetY = -1;
        final List<Integer> colors = new ArrayList<>();

        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                final ItemStack stack = input.getItem(x, y);
                if (stack.isEmpty()) continue;

                if (stack.is(FLRCraftingRecipes.DYEABLE_LIGHTS) && light.isEmpty()) {
                    light = stack;
                    lightX = x;
                    lightY = y;
                } else if (stack.is(FLRCraftingRecipes.GOLD_NUGGETS) && nuggetX < 0) {
                    nuggetX = x;
                    nuggetY = y;
                } else if (DyeableItem.getDyeColor(stack) instanceof DyeColor dye) {
                    colors.add(DyeableItem.getColor(dye));
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (light.isEmpty() || colors.isEmpty() || nuggetY != lightY || Math.abs(nuggetX - lightX) != 1) {
            return ItemStack.EMPTY;
        }

        final ItemStack result = light.copyWithCount(1);

        if (result.has(DataComponents.DYED_COLOR)) {
            colors.addFirst(DyeableItem.getColor(result));
            result.remove(DataComponents.DYED_COLOR);
        }

        result.set(FLRDataComponents.COLORS, List.copyOf(colors));
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<ColorChangingLightRecipe> getSerializer() {
        return FLRCraftingRecipes.COLOR_CHANGING_LIGHT;
    }
}
