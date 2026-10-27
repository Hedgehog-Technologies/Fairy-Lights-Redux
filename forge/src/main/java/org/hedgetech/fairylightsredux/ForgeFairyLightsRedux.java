package org.hedgetech.fairylightsredux;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.level.NoteBlockEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.registries.RegisterEvent;
import org.hedgetech.fairylightsredux.network.*;
import org.hedgetech.fairylightsredux.platform.ForgeConfigHelper;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.block.FastenerBlock;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.creativeTabs.FLRCreativeTabs;
import org.hedgetech.fairylightsredux.server.entity.FLREntities;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.hedgetech.fairylightsredux.server.jingle.JingleManager;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;

import java.util.function.Predicate;

@Mod(Constants.MOD_ID)
public class ForgeFairyLightsRedux {
    public static final Channel<CustomPacketPayload> CHANNEL = ChannelBuilder.named(Constants.id("main"))
            .payloadChannel()
                .play()
                    .clientbound()
                        .addMain(UpdateEntityFastenerPayload.TYPE, UpdateEntityFastenerPayload.STREAM_CODEC, (payload, context) -> ForgeFairyLightsReduxClient.handle(payload))
                        .addMain(JinglePayload.TYPE, JinglePayload.STREAM_CODEC, (payload, context) -> ForgeFairyLightsReduxClient.handle(payload))
                        .addMain(OpenEditLetteredConnectionScreenPayload.TYPE, OpenEditLetteredConnectionScreenPayload.STREAM_CODEC, (payload, context) -> ForgeFairyLightsReduxClient.handle(payload))
                    .serverbound()
                        .addMain(InteractionConnectionPayload.TYPE, InteractionConnectionPayload.STREAM_CODEC, (payload, context) -> payload.handle(context.getSender()))
                        .addMain(EditLetteredConnectionPayload.TYPE, EditLetteredConnectionPayload.STREAM_CODEC, (payload, context) -> payload.handle(context.getSender()))
                        .addMain(PickConnectionPayload.TYPE, PickConnectionPayload.STREAM_CODEC, (payload, context) -> payload.handle(context.getSender()))
            .build();

    public ForgeFairyLightsRedux(FMLJavaModLoadingContext context) {
        CommonClass.init();
        context.registerConfig(ModConfig.Type.COMMON, ForgeConfigHelper.SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, ForgeConfigHelper.CLIENT_SPEC);

        AddReloadListenerEvent.BUS.addListener(event -> event.addListener(JingleManager.INSTANCE));
        NoteBlockEvent.Play.BUS.addListener((Predicate<NoteBlockEvent.Play>) event -> FastenerBlock.ringNoteBlockBell((Level) event.getLevel(), event.getPos(), event.getState(), event.getVanillaNoteId()));

        final BusGroup modBus = context.getModBusGroup();
        RegisterEvent.getBus(modBus).addListener(ForgeFairyLightsRedux::register);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeFairyLightsReduxClient.init(modBus);
        }
    }

    private static void register(final RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, helper -> FLRSounds.register(helper::register));
        event.register(Registries.DATA_COMPONENT_TYPE, helper -> FLRDataComponents.register(helper::register));
        event.register(Registries.BLOCK, helper -> FLRBlocks.register(helper::register));
        event.register(Registries.ITEM, helper -> FLRItems.register(helper::register));
        event.register(Registries.RECIPE_SERIALIZER, helper -> FLRCraftingRecipes.register(helper::register));
        event.register(Registries.BLOCK_ENTITY_TYPE, helper -> FLRBlockEntities.register(helper::register));
        event.register(Registries.ENTITY_TYPE, helper -> FLREntities.register(helper::register));
        event.register(Registries.CREATIVE_MODE_TAB, helper -> FLRCreativeTabs.register(helper::register));
    }
}
