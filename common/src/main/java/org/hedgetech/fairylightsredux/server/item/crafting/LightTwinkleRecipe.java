package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.jetbrains.annotations.NotNull;

public class LightTwinkleRecipe extends CustomRecipe {
    public LightTwinkleRecipe(final CraftingBookCategory category) {
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
        boolean glowstone = false;

        for (int i = 0; i < input.size(); i++) {
            final ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(FLRCraftingRecipes.TWINKLING_LIGHTS) && light.isEmpty()) {
                light = stack;
            } else if (stack.is(FLRCraftingRecipes.GLOWSTONE_DUSTS) && !glowstone) {
                glowstone = true;
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (light.isEmpty() || !glowstone) return ItemStack.EMPTY;

        final ItemStack result = light.copyWithCount(1);
        result.set(FLRDataComponents.TWINKLE, Unit.INSTANCE);

        return result;
    }

    @Override
    public @NotNull RecipeSerializer<LightTwinkleRecipe> getSerializer() {
        return FLRCraftingRecipes.LIGHT_TWINKLE;
    }
}
