package org.hedgetech.fairylightsredux;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.hedgetech.fairylightsredux.network.*;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.creativeTabs.FLRCreativeTabs;
import org.hedgetech.fairylightsredux.server.entity.FLREntities;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.item.crafting.FLRCraftingRecipes;
import org.hedgetech.fairylightsredux.server.jingle.JingleManager;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class FabricFairyLightsRedux implements ModInitializer {

    @Override
    public void onInitialize() {
        CommonClass.init();

        FLRSounds.register((id, value) -> Registry.register(BuiltInRegistries.SOUND_EVENT, id, value));
        FLRDataComponents.register((id, value) -> Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, value));
        FLRBlocks.register((id, value) -> Registry.register(BuiltInRegistries.BLOCK, id, value));
        FLRItems.register((id, value) -> Registry.register(BuiltInRegistries.ITEM, id, value));
        FLRCraftingRecipes.register((id, value) -> Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, value));
        FLRBlockEntities.register((id, value) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, value));
        FLREntities.register((id, value) -> Registry.register(BuiltInRegistries.ENTITY_TYPE, id, value));
        FLRCreativeTabs.register((id, value) -> Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, value));

        PayloadTypeRegistry.playS2C().register(UpdateEntityFastenerPayload.TYPE, UpdateEntityFastenerPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(InteractionConnectionPayload.TYPE, InteractionConnectionPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(OpenEditLetteredConnectionScreenPayload.TYPE, OpenEditLetteredConnectionScreenPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(JinglePayload.TYPE, JinglePayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(EditLetteredConnectionPayload.TYPE, EditLetteredConnectionPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(PickConnectionPayload.TYPE, PickConnectionPayload.STREAM_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(InteractionConnectionPayload.TYPE, (payload, context) -> payload.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(EditLetteredConnectionPayload.TYPE, (payload, context) -> payload.handle(context.player()));
        ServerPlayNetworking.registerGlobalReceiver(PickConnectionPayload.TYPE, (payload, context) -> payload.handle(context.player()));

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return JingleManager.ID;
            }

            @Override
            public @NotNull CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, Executor backgroundExecutor, Executor gameExecutor) {
                return JingleManager.INSTANCE.reload(barrier, manager, backgroundExecutor, gameExecutor);
            }
        });
    }
}
