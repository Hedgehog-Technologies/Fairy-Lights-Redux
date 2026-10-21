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

public class PennantBuntingAugmentationRecipe extends CustomRecipe {
    public PennantBuntingAugmentationRecipe(final CraftingBookCategory category) {
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
        ItemStack bunting = ItemStack.EMPTY;
        final List<ItemStack> pattern = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            final ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (stack.is(FLRItems.PENNANT_BUNTING) && bunting.isEmpty()) {
                bunting = stack;
            } else if (stack.is(FLRCraftingRecipes.PENNANTS)) {
                pattern.add(stack.copyWithCount(1));
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (bunting.isEmpty() || pattern.isEmpty()) return ItemStack.EMPTY;

        final ItemStack result = bunting.copyWithCount(1);
        result.set(FLRDataComponents.PENNANT_BUNTING, new PennantBunting(pattern, StyledString.EMPTY));

        return result;
    }

    @Override
    public @NotNull RecipeSerializer<PennantBuntingAugmentationRecipe> getSerializer() {
        return FLRCraftingRecipes.PENNANT_BUNTING_AUGMENTATION;
    }
}
