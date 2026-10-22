package org.hedgetech.fairylightsredux;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.creativeTabs.FLRCreativeTabs;
import org.hedgetech.fairylightsredux.server.entity.FLREntities;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;

public class FabricFairyLightsRedux implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();
        FLRSounds.register((id, value) -> Registry.register(BuiltInRegistries.SOUND_EVENT, id, value));
        FLRDataComponents.register((id, value) -> Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, value));
        FLRBlocks.register((id, value) -> Registry.register(BuiltInRegistries.BLOCK, id, value));
        FLRItems.register((id, value) -> Registry.register(BuiltInRegistries.ITEM, id, value));
        FLRCraftingRecipes.register((id, value) -> Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, value));
        FLRBlockEntities.register((id, value) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, value));
        FLREntities.register((id, value) -> Registry.register(BuiltInRegistries.ENTITY_TYPE, id, value));
        FLRCreativeTabs.register((id, value) -> Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, value));
    }
}
