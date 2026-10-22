package org.hedgetech.fairylightsredux;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.level.NoteBlockEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.hedgetech.fairylightsredux.platform.NeoForgeConfigHelper;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.block.FastenerBlock;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.creativeTabs.FLRCreativeTabs;
import org.hedgetech.fairylightsredux.server.entity.FLREntities;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.hedgetech.fairylightsredux.server.jingle.JingleManager;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;

@Mod(Constants.MOD_ID)
public class NeoForgeFairyLightsRedux {
    public NeoForgeFairyLightsRedux(IEventBus eventBus, ModContainer container) {
        CommonClass.init();
        container.registerConfig(ModConfig.Type.COMMON, NeoForgeConfigHelper.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfigHelper.CLIENT_SPEC);
        NeoForge.EVENT_BUS.addListener((final AddServerReloadListenersEvent event) -> event.addListener(JingleManager.ID, JingleManager.INSTANCE));
        NeoForge.EVENT_BUS.addListener((final NoteBlockEvent.Play event) -> {
            if (FastenerBlock.ringNoteBlockBell((Level) event.getLevel(), event.getPos(), event.getState(), event.getVanillaNoteId())) {
                event.setCanceled(true);
            }
        });
        eventBus.addListener(NeoForgeFairyLightsRedux::register);
    }

    private static void register(final RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, helper -> FLRSounds.register(helper::register));
        event.register(Registries.DATA_COMPONENT_TYPE, helper -> FLRDataComponents.register(helper::register));
        event.register(Registries.BLOCK, helper -> FLRBlocks.register(helper::register));
        event.register(Registries.ITEM, helper -> FLRItems.register(helper::register));
        event.register(Registries.RECIPE_SERIALIZER, helper -> FLRCraftingRecipes.register(helper::register));
        event.register(Registries.BLOCK_ENTITY_TYPE, helper -> FLRBlockEntities.register(helper::register));
        event.register(Registries.ENTITY_TYPE, helper -> FLREntities.register(helper::register));
        event.register(Registries.CREATIVE_MODE_TAB, helper -> FLRCreativeTabs.register(helper::register));
    }
}
