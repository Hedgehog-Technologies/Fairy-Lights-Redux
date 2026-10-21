package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.HolderLookup;
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

public class HangingLightsAugmentationRecipe extends CustomRecipe {
    public HangingLightsAugmentationRecipe(final CraftingBookCategory category) {
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
        ItemStack hangingLights = ItemStack.EMPTY;
        final List<ItemStack> pattern = new ArrayList<>();
        boolean whiteString = false;

        for (int i = 0; i < input.size(); i++) {
            final ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(FLRItems.HANGING_LIGHTS) && hangingLights.isEmpty()) {
                hangingLights = stack;
            } else if (stack.is(FLRCraftingRecipes.LIGHTS)) {
                pattern.add(stack.copyWithCount(1));
            } else if (DyeableItem.getDyeColor(stack) == DyeColor.WHITE && !whiteString) {
                whiteString = true;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (hangingLights.isEmpty() || pattern.isEmpty() && !whiteString) return ItemStack.EMPTY;

        final HangingLights lights = hangingLights.getOrDefault(FLRDataComponents.HANGING_LIGHTS, HangingLights.DEFAULT);
        if (pattern.isEmpty() && lights.string() == StringType.WHITE_STRING) return ItemStack.EMPTY;

        final ItemStack result = hangingLights.copyWithCount(1);
        result.set(FLRDataComponents.HANGING_LIGHTS, new HangingLights(
                whiteString ? StringType.WHITE_STRING : lights.string(),
                pattern.isEmpty() ? lights.pattern() : pattern
        ));

        return result;
    }

    @Override
    public @NotNull RecipeSerializer<HangingLightsAugmentationRecipe> getSerializer() {
        return FLRCraftingRecipes.HANGING_LIGHTS_AUGMENTATION;
    }
}
