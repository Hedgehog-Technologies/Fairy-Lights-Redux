package org.hedgetech.fairylightsredux.server.creativeTabs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.item.FLRItems;

import java.util.function.BiConsumer;

public final class FLRCreativeTabs {

    public static CreativeModeTab GENERAL;

    public static void register(final BiConsumer<ResourceLocation, CreativeModeTab> registry) {
        GENERAL = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.literal("FAKE TAB"))
                .icon(() -> new ItemStack(FLRItems.FAKE_ITEM))
                .displayItems((parameters, output) -> {
                    output.accept(FLRItems.FAKE_ITEM);
                })
                .build();
        registry.accept(Constants.id("general"), GENERAL);
    }

    private FLRCreativeTabs() {}
}
