package org.hedgetech.fairylightsredux.handler;

import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.connection.HangingLightsConnection;
import org.hedgetech.fairylightsredux.feature.light.Light;
import org.hedgetech.fairylightsredux.jingle.Jingle;
import org.hedgetech.fairylightsredux.jingle.JingleLibrary;
import org.hedgetech.fairylightsredux.jingle.JingleManager;
import org.hedgetech.fairylightsredux.network.NetworkProxy;
import org.hedgetech.fairylightsredux.network.clientbound.JingleMessage;

public final class ServerEventHandler {
    public static boolean tryJingle(final Level world, final HangingLightsConnection hangingLights) {
        String lib;
        if (Constants.CHRISTMAS.isOccurringNow()) {
            lib = JingleLibrary.CHRISTMAS;
        } else if (Constants.HALLOWEEN.isOccurringNow()) {
            lib = JingleLibrary.HALLOWEEN;
        } else {
            lib = JingleLibrary.RANDOM;
        }
        return tryJingle(world, hangingLights, lib);
    }

    public static boolean tryJingle(final Level world, final HangingLightsConnection hangingLights, final String lib) {
        if (world.isClientSide()) return false;
        final Light<?>[] lights = hangingLights.getFeatures();
        final Jingle jingle = JingleManager.INSTANCE.get(lib).getRandom(world.random, lights.length);
        if (jingle != null) {
            final int lightOffset = lights.length / 2 - jingle.getRange() / 2;
            hangingLights.play(jingle, lightOffset);
            NetworkProxy.sendToPlayersWatchingChunk(new JingleMessage(hangingLights, lightOffset, jingle), world, hangingLights.getFastener().getPos());
            return true;
        }
        return false;
    }

    private ServerEventHandler() {}
}
