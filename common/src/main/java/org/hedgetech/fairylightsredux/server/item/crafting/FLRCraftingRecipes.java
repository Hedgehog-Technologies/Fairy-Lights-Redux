package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.hedgetech.fairylightsredux.Constants;

import java.util.function.BiConsumer;

public final class FLRCraftingRecipes {
    public static final TagKey<Item> LIGHTS = TagKey.create(Registries.ITEM, Constants.id("lights"));
    public static final TagKey<Item> STRINGS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "strings"));

    public static void register(final BiConsumer<ResourceLocation, RecipeSerializer<?>> registry) {

    }

    private FLRCraftingRecipes() {}
}
