package org.hedgetech.fairylightsredux.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface PlatformNetwork {
    void sendToPlayersWatchingChunk(Object message, Level level, BlockPos pos);

    void sendToServer(Object message);

    void sendToPlayer(Object message, Player player);
}
