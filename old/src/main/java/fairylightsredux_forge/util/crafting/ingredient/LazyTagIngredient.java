package org.hedgetech.fairylightsredux.util.crafting.ingredient;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;

public class LazyTagIngredient extends Ingredient {
    private final TagKey<Item> tag;

    private LazyTagIngredient(final TagKey<Item> tag) {
        super(HolderSet.empty());
        this.tag = tag;
    }

    @Override
    public boolean test(@Nullable final ItemStack stack) {
        return stack != null && stack.is(this.tag);
    }

    @Override
    public boolean isEmpty() {
        return !BuiltInRegistries.ITEM.getTagOrEmpty(this.tag).iterator().hasNext();
    }

    public static LazyTagIngredient of(final TagKey<Item> tag) {
        return new LazyTagIngredient(tag);
    }
}
