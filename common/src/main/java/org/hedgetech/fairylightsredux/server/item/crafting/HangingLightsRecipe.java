package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.component.HangingLights;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.string.StringType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HangingLightsRecipe extends CustomRecipe {
    private static final int CORD_WIDTH = 3;

    public HangingLightsRecipe(final CraftingBookCategory category) {
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
        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x + CORD_WIDTH <= input.width(); x++) {
                if (input.getItem(x, y).is(FLRCraftingRecipes.IRON_INGOTS) && input.getItem(x + 1, y).is(FLRCraftingRecipes.STRINGS) && input.getItem(x + 2, y).is(FLRCraftingRecipes.IRON_INGOTS)) {
                    final ItemStack result = this.craft(input, x, y);

                    if (!result.isEmpty()) {
                        return result;
                    }
                }
            }
        }

        return ItemStack.EMPTY;
    }

    private ItemStack craft(final CraftingInput input, final int cordX, final int cordY) {
        final List<ItemStack> pattern = new ArrayList<>();
        boolean whiteString = false;
        boolean twinkle = false;

        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                if (y == cordY && x >= cordX && x < cordX + CORD_WIDTH) continue;

                final ItemStack stack = input.getItem(x, y);
                if (stack.isEmpty()) continue;

                if (stack.is(FLRCraftingRecipes.LIGHTS)) {
                    pattern.add(stack.copyWithCount(1));
                } else if (DyeableItem.getDyeColor(stack) == DyeColor.WHITE && !whiteString) {
                    whiteString = true;
                } else if (stack.is(FLRCraftingRecipes.GLOWSTONE_DUSTS) && !twinkle) {
                    twinkle = true;
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (pattern.isEmpty()) return ItemStack.EMPTY;

        if (twinkle) {
            boolean anyTwinkles = false;

            for (final ItemStack light : pattern) {
                if (light.is(FLRCraftingRecipes.TWINKLING_LIGHTS)) {
                    light.set(FLRDataComponents.TWINKLE, Unit.INSTANCE);
                    anyTwinkles = true;
                }
            }

            if (!anyTwinkles) return ItemStack.EMPTY;
        }

        final ItemStack result = new ItemStack(FLRItems.HANGING_LIGHTS);
        result.set(FLRDataComponents.HANGING_LIGHTS, new HangingLights(whiteString ? StringType.WHITE_STRING : StringType.BLACK_STRING, pattern));

        return result;
    }

    @Override
    public @NotNull RecipeSerializer<HangingLightsRecipe> getSerializer() {
        return FLRCraftingRecipes.HANGING_LIGHTS;
    }
}
