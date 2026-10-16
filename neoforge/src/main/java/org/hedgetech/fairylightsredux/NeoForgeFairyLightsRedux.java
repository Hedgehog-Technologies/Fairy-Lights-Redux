package org.hedgetech.fairylightsredux;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.hedgetech.fairylightsredux.platform.NeoForgeConfigHelper;
import org.hedgetech.fairylightsredux.server.creativeTabs.FLRCreativeTabs;
import org.hedgetech.fairylightsredux.server.item.FLRItems;

@Mod(Constants.MOD_ID)
public class NeoForgeFairyLightsRedux {

    public NeoForgeFairyLightsRedux(IEventBus eventBus, ModContainer container) {
        CommonClass.init();
        container.registerConfig(ModConfig.Type.COMMON, NeoForgeConfigHelper.SPEC);
        container.registerConfig(ModConfig.Type.CLIENT, NeoForgeConfigHelper.CLIENT_SPEC);
        eventBus.addListener(NeoForgeFairyLightsRedux::register);
    }

    private static void register(final RegisterEvent event) {
        event.register(Registries.ITEM, helper -> FLRItems.register(helper::register));
        event.register(Registries.CREATIVE_MODE_TAB, helper -> FLRCreativeTabs.register(helper::register));
    }
}
