package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;

import java.util.UUID;

public class ConnectionType<T extends Connection> {
    private final Factory<T>

    public interface Factory<T extends Connection> {
        T create(final ConnectionType<T> type, final Level world, final Fastener<?> fastener, final UUID uuid);
    }
}
