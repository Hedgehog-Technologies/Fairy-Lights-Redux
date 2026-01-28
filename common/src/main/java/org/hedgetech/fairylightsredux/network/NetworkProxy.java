package org.hedgetech.fairylightsredux.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class NetworkProxy {
    private static PlatformNetwork PLATFORM;

    public static void init(PlatformNetwork platform) {
        PLATFORM = platform;
    }

    public static void sendToPlayersWatchingChunk(Object message, Level level, BlockPos pos) {
        if (PLATFORM == null) {
            throw new IllegalStateException("Network not initialized");
        }
        PLATFORM.sendToPlayersWatchingChunk(message, level, pos);
    }

    public static void sendToPlayersWatchingEntity(Object message, Entity entity) {
        if (PLATFORM == null) {
            throw new IllegalStateException("Network not initialized");
        }
        PLATFORM.sendToPlayersWatchingEntity(message, entity);
    }

    public static void sendToServer(Object message) {
        PLATFORM.sendToServer(message);
    }

    public static void sendToPlayer(Object message, Player player) {
        PLATFORM.sendToPlayer(message, player);
    }

    private NetworkProxy() {}
}
