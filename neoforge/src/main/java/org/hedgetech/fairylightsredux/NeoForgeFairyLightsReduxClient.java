package org.hedgetech.fairylightsredux;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.hedgetech.fairylightsredux.client.*;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.FastenerBlockEntityRenderer;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.LightBlockEntityRenderer;
import org.hedgetech.fairylightsredux.client.renderer.entity.FenceFastenerRenderer;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.entity.FLREntities;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeFairyLightsReduxClient {

    public NeoForgeFairyLightsReduxClient(IEventBus eventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        NeoForge.EVENT_BUS.addListener((final ClientTickEvent.Post event) -> HangingLightsTutorial.tick());
        eventBus.addListener((final RegisterGuiLayersEvent event) -> event.registerAboveAll(JingleOverlay.ID, (graphics, deltaTracker) -> JingleOverlay.render(graphics)));
        eventBus.addListener(NeoForgeFairyLightsReduxClient::registerLayerDefinitions);
        eventBus.addListener(NeoForgeFairyLightsReduxClient::registerRenderers);
        eventBus.addListener(NeoForgeFairyLightsReduxClient::registerItemTintSources);
    }

    private static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        FLRModelLayers.register(event::registerLayerDefinition);
    }

    private static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(FLRBlockEntities.LIGHT, LightBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(FLRBlockEntities.FASTENER, FastenerBlockEntityRenderer::new);
        event.registerEntityRenderer(FLREntities.FASTENER, FenceFastenerRenderer::new);
    }

    private static void registerItemTintSources(final RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(HangingLightsTintSource.ID, HangingLightsTintSource.MAP_CODEC);
        event.register(LightTintSource.ID, LightTintSource.MAP_CODEC);
        event.register(PennantBuntingTintSource.ID, PennantBuntingTintSource.MAP_CODEC);
        event.register(LetterBuntingTintSource.ID, LetterBuntingTintSource.MAP_CODEC);
    }
}
