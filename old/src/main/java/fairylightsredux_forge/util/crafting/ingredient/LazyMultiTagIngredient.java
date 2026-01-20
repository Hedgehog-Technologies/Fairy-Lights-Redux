package org.hedgetech.fairylightsredux.util.crafting.ingredient;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class LazyMultiTagIngredient extends Ingredient {
    private final List<TagKey<Item>> tags;

    private LazyMultiTagIngredient(final List<TagKey<Item>> tags) {
        super(HolderSet.empty());
        this.tags = tags;
    }

    @Override
    public boolean test(@Nullable final ItemStack stack) {
        return stack != null && tags.stream().allMatch(stack::is);
    }

    @Override
    public boolean isEmpty() {
        for (TagKey<Item> tag : this.tags) {
            if (BuiltInRegistries.ITEM.getTagOrEmpty(tag).iterator().hasNext()) {
                return false;
            }
        }
        return true;
    }

    public static LazyMultiTagIngredient of(final List<TagKey<Item>> tags) {
        return new LazyMultiTagIngredient(tags);
    }
}
