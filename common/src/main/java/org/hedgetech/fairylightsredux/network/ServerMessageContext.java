package org.hedgetech.fairylightsredux.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public interface ServerMessageContext {
    void enqueueWork(Runnable task);

    Level getLevel();

    ServerPlayer getPlayer();
}
