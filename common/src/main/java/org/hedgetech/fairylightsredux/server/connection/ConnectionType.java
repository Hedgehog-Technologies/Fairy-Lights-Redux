package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;

import java.util.UUID;
import java.util.function.Supplier;

public final class ConnectionType<T extends Connection> {
    private final IFactory<T> factory;
    private final Supplier<? extends Item> item;

    public ConnectionType(final IFactory<T> factory, final Supplier<? extends Item> item) {
        this.factory = factory;
        this.item = item;
    }

    public T create(final Level world, final Fastener<?> fastener, final UUID uuid) {
        return this.factory.create(this, world, fastener, uuid);
    }

    public Item getItem() {
        return this.item.get();
    }

    public interface IFactory<T extends Connection> {
        T create(final ConnectionType<T> type, final Level world, final Fastener<?> fastener, final UUID uuid);
    }
}
