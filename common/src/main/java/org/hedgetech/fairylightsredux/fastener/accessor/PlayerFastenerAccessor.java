package org.hedgetech.fairylightsredux.fastener.accessor;

import net.minecraft.world.entity.player.Player;
import org.hedgetech.fairylightsredux.fastener.FastenerType;

public final class PlayerFastenerAccessor extends EntityFastenerAccessor<Player> {
    public PlayerFastenerAccessor() {
        super(Player.class);
    }

    public PlayerFastenerAccessor(final PlayerFastener fastener) {
        super(Player.class, fastener);
    }

    @Override
    public FastenerType getType() {
        return FastenerType.PLAYER;
    }
}
