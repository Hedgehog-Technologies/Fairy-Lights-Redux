package org.hedgetech.fairylightsredux.platform;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;
import org.hedgetech.fairylightsredux.ForgeFairyLightsRedux;
import org.hedgetech.fairylightsredux.platform.services.IPlatformHelper;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload) {
        ForgeFairyLightsRedux.CHANNEL.send(payload, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity));
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        ForgeFairyLightsRedux.CHANNEL.send(payload, PacketDistributor.PLAYER.with(player));
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ForgeFairyLightsRedux.CHANNEL.send(payload, PacketDistributor.SERVER.noArg());
    }
}
