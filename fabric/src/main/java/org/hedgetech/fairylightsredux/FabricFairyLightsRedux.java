package org.hedgetech.fairylightsredux;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.hedgetech.fairylightsredux.server.creativeTabs.FLRCreativeTabs;
import org.hedgetech.fairylightsredux.server.item.FLRItems;

public class FabricFairyLightsRedux implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();
        FLRItems.register((id, value) -> Registry.register(BuiltInRegistries.ITEM, id, value));
        FLRCreativeTabs.register((id, value) -> Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, value));
    }
}
