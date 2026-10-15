package org.hedgetech.fairylightsredux;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.registries.RegisterEvent;
import org.hedgetech.fairylightsredux.platform.ForgeConfigHelper;

@Mod(Constants.MOD_ID)
public class ForgeFairyLightsRedux {

    public static final Channel<CustomPacketPayload> CHANNEL = ChannelBuilder.named(Constants.id("main"))
            .payloadChannel()
                .play()
                    .clientbound()
                    .serverbound()
            .build();

    public ForgeFairyLightsRedux(FMLJavaModLoadingContext context) {
        CommonClass.init();
        context.registerConfig(ModConfig.Type.COMMON, ForgeConfigHelper.SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, ForgeConfigHelper.CLIENT_SPEC);

        final BusGroup modBus = context.getModBusGroup();
        RegisterEvent.getBus(modBus).addListener(ForgeFairyLightsRedux::register);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeFairyLightsReduxClient.init(modBus);
        }
    }

    private static void register(final RegisterEvent event) {

    }
}
