package org.hedgetech.fairylightsredux;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

@Mod(Constants.MOD_ID)
public final class ForgeFairyLightsRedux {

    public ForgeFairyLightsRedux() {

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();

        public static final SimpleChannel NETWORK = ChannelBuilder
                .named(Constants.NETWORK)
                .optionalClient()
                .networkProtocolVersion(1)
                .simpleChannel()
                    .any()
                        .serverbound()
    }
}