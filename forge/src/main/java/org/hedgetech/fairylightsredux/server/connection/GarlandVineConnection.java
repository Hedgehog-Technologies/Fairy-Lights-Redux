package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;

import java.util.UUID;

public final class GarlandVineConnection extends Connection {
    public GarlandVineConnection(final ConnectionType<? extends GarlandVineConnection> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
        super(type, world, fastener, uuid);
    }

    @Override
    public float getRadius() {
        return 2.5F / 16.0F;
    }
}
