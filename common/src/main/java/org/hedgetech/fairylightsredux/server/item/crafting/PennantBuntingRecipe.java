package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.component.PennantBunting;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PennantBuntingRecipe extends CustomRecipe {
    private static final int CORD_WIDTH = 3;

    public PennantBuntingRecipe(final CraftingBookCategory category) {
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
                    if (!result.isEmpty()) return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    private ItemStack craft(final CraftingInput input, final int cordX, final int cordY) {
        final List<ItemStack> pattern = new ArrayList<>();

        for (int y = 0; y < input.height(); y++) {
            for (int x = 0; x < input.width(); x++) {
                if (y == cordY && x >= cordX && x < cordX + CORD_WIDTH) continue;

                final ItemStack stack = input.getItem(x, y);
                if (stack.isEmpty()) continue;

                if (stack.is(FLRCraftingRecipes.PENNANTS)) {
                    pattern.add(stack.copyWithCount(1));
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (pattern.isEmpty()) return ItemStack.EMPTY;

        final ItemStack result = new ItemStack(FLRItems.PENNANT_BUNTING);
        result.set(FLRDataComponents.PENNANT_BUNTING, new PennantBunting(pattern, StyledString.EMPTY));

        return result;
    }

    @Override
    public @NotNull RecipeSerializer<PennantBuntingRecipe> getSerializer() {
        return FLRCraftingRecipes.PENNANT_BUNTING;
    }
}
