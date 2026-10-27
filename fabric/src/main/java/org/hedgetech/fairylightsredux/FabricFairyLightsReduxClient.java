package org.hedgetech.fairylightsredux;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import org.hedgetech.fairylightsredux.client.*;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.FastenerBlockEntityRenderer;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.LightBlockEntityRenderer;
import org.hedgetech.fairylightsredux.client.renderer.entity.FenceFastenerRenderer;
import org.hedgetech.fairylightsredux.network.JinglePayload;
import org.hedgetech.fairylightsredux.network.OpenEditLetteredConnectionScreenPayload;
import org.hedgetech.fairylightsredux.network.UpdateEntityFastenerPayload;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.entity.FLREntities;

public class FabricFairyLightsReduxClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FLRModelLayers.register((layer, definition) -> EntityModelLayerRegistry.registerModelLayer(layer, definition::get));

        BlockEntityRenderers.register(FLRBlockEntities.LIGHT, LightBlockEntityRenderer::new);
        BlockEntityRenderers.register(FLRBlockEntities.FASTENER, FastenerBlockEntityRenderer::new);
        EntityRendererRegistry.register(FLREntities.FASTENER, FenceFastenerRenderer::new);

        ItemTintSources.ID_MAPPER.put(HangingLightsTintSource.ID, HangingLightsTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(LightTintSource.ID, LightTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(PennantBuntingTintSource.ID, PennantBuntingTintSource.MAP_CODEC);
        ItemTintSources.ID_MAPPER.put(LetterBuntingTintSource.ID, LetterBuntingTintSource.MAP_CODEC);

        ClientPlayNetworking.registerGlobalReceiver(UpdateEntityFastenerPayload.TYPE, (payload, context) -> payload.handle(context.player().level()));
        ClientPlayNetworking.registerGlobalReceiver(JinglePayload.TYPE, (payload, context) -> payload.handle(context.player().level()));
        HudElementRegistry.addLast(JingleOverlay.ID, (graphics, deltaTracker) -> JingleOverlay.render(graphics));
        ClientTickEvents.END_CLIENT_TICK.register(client -> HangingLightsTutorial.tick());
        ClientPlayNetworking.registerGlobalReceiver(OpenEditLetteredConnectionScreenPayload.TYPE, (payload, context) -> payload.getConnection(context.player().level()).ifPresent(EditLetteredConnectionScreen::open));
    }
}
