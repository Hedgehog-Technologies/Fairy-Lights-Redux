package org.hedgetech.fairylightsredux;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import org.hedgetech.fairylightsredux.client.*;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.FastenerBlockEntityRenderer;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.LightBlockEntityRenderer;
import org.hedgetech.fairylightsredux.client.renderer.entity.FenceFastenerRenderer;
import org.hedgetech.fairylightsredux.network.EditLetteredConnectionPayload;
import org.hedgetech.fairylightsredux.network.JinglePayload;
import org.hedgetech.fairylightsredux.network.OpenEditLetteredConnectionScreenPayload;
import org.hedgetech.fairylightsredux.network.UpdateEntityFastenerPayload;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.entity.FLREntities;

public class ForgeFairyLightsReduxClient {
    private ForgeFairyLightsReduxClient() {}

    public static void init(final BusGroup modBus) {
        TickEvent.ClientTickEvent.Post.BUS.addListener(event -> HangingLightsTutorial.tick());
        AddGuiOverlayLayersEvent.getBus(modBus).addListener(event -> event.getLayeredDraw().add(JingleOverlay.ID, (graphics, deltaTracker) -> JingleOverlay.render(graphics)));

        EntityRenderersEvent.RegisterLayerDefinitions.getBus(modBus).addListener(event -> FLRModelLayers.register(event::registerLayerDefinition));
        EntityRenderersEvent.RegisterRenderers.getBus(modBus).addListener(event -> {
            event.registerBlockEntityRenderer(FLRBlockEntities.LIGHT, LightBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(FLRBlockEntities.FASTENER, FastenerBlockEntityRenderer::new);
            event.registerEntityRenderer(FLREntities.FASTENER, FenceFastenerRenderer::new);
        });

        ItemTintSources.ID_MAPPER.put(HangingLightsTintSource.ID, HangingLightsTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(LightTintSource.ID, LightTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(PennantBuntingTintSource.ID, PennantBuntingTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(LetterBuntingTintSource.ID, LetterBuntingTintSource.MAP_CODEC);
    }

    public static void handle(final UpdateEntityFastenerPayload payload) {
        payload.handle(Minecraft.getInstance().level);
    }

    public static void handle(final JinglePayload payload) {
        payload.handle(Minecraft.getInstance().level);
    }

    public static void handle(final OpenEditLetteredConnectionScreenPayload payload) {
        payload.getConnection(Minecraft.getInstance().level).ifPresent(EditLetteredConnectionScreen::open);
    }
}
