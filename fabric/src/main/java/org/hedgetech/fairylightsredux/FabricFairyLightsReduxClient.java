package org.hedgetech.fairylightsredux;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;
import org.hedgetech.fairylightsredux.network.JinglePayload;
import org.hedgetech.fairylightsredux.network.OpenEditLetteredConnectionScreenPayload;
import org.hedgetech.fairylightsredux.network.UpdateEntityFastenerPayload;

public class FabricFairyLightsReduxClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateEntityFastenerPayload.TYPE, (payload, context) -> payload.handle(context.player().level()));
        ClientPlayNetworking.registerGlobalReceiver(JinglePayload.TYPE, (payload, context) -> payload.handle(context.player().level()));
        ClientPlayNetworking.registerGlobalReceiver(OpenEditLetteredConnectionScreenPayload.TYPE, (payload, context) -> payload.getConnection(context.player().level()).ifPresent(EditLetteredConnectionScreen::open));
    }
}
