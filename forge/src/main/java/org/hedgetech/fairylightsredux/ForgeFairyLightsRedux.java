package org.hedgetech.fairylightsredux;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.hedgetech.fairylightsredux.data.DataGatherer;

@Mod(Constants.MOD_ID)
public final class ForgeFairyLightsRedux {
    public ForgeFairyLightsRedux(FMLJavaModLoadingContext ctx) {

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Constants.LOG.info("Hello Forge world!");
        CommonClass.init();
        GatherDataEvent.getBus(ctx.getModBusGroup()).addListener(DataGatherer::onGatherData);
    }
}