package org.hedgetech.fairylightsredux.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface ClientMessageContext {
    void enqueueWork(Runnable task);

    Level getLevel();

    Player getPlayer();
}
