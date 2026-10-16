package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.hedgetech.fairylightsredux.Constants;

import java.util.function.BiConsumer;

public final class FLRItems {

    public static Item FAKE_ITEM;

    public static void register(final BiConsumer<ResourceLocation, Item> registry) {
        FAKE_ITEM = register(registry, "fake_item", new Item(defaultProperties("fake_item")));
    }

    private static <T extends Item> T register(final BiConsumer<ResourceLocation, Item> registry, final String name, final T item) {
        registry.accept(Constants.id(name), item);
        return item;
    }

    private static Item.Properties defaultProperties(final String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, Constants.id(name)));
    }

    private FLRItems() {}
}
