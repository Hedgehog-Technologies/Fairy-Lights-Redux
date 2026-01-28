package org.hedgetech.fairylightsredux.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface PlatformNetwork {
    void sendToPlayersWatchingChunk(Object message, Level level, BlockPos pos);

    void sendToPlayersWatchingEntity(Object message, Entity entity);

    void sendToServer(Object message);

    void sendToPlayer(Object message, Player player);
}
