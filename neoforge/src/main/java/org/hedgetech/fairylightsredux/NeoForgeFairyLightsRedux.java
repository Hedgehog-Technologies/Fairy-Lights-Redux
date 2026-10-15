package org.hedgetech.fairylightsredux;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.hedgetech.fairylightsredux.platform.NeoForgeConfigHelper;

@Mod(Constants.MOD_ID)
public class NeoForgeFairyLightsRedux {

    public NeoForgeFairyLightsRedux(IEventBus eventBus, ModContainer container) {
        CommonClass.init();
        container.registerConfig(ModConfig.Type.COMMON, NeoForgeConfigHelper.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfigHelper.CLIENT_SPEC);
    }
}
