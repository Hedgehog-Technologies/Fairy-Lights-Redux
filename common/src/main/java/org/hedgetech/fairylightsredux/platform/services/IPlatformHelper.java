package org.hedgetech.fairylightsredux.platform.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform.
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Sends a payload to every player tracking the entity, and to the entity itself if it is a player.
     * @param entity The entity whose trackers receive the payload.
     * @param payload The payload to send
     */
    void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload);

    /**
     * Sends a payload from the server to one player.
     * @param player The player who receives the payload.
     * @param payload The payload to send.
     */
    void sendToPlayer(ServerPlayer player, CustomPacketPayload payload);

    /**
     * Sends a payload from the client to the server. Only call this on the client.
     * @param payload The payload to send.
     */
    void sendToServer(CustomPacketPayload payload);

    /**
     * Gets the name of the environment type as a string.
     * @return The name of the environment type, either "development" or "production".
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
