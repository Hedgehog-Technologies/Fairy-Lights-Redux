package org.hedgetech.fairylightsredux.server.fastener;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.fastener.accessor.FastenerAccessor;

import javax.annotation.Nullable;
import java.util.*;

public abstract class AbstractFastener<F extends FastenerAccessor> implements Fastener<F> {
    private final Map<UUID, Connection> outgoing = new HashMap<>();
    private final Map<UUID, Incoming> incoming = new HashMap<>();

    protected AABB bounds = BlockEntity.INFINITE_EXTENT_AABB;

    @Nullable
    private Level world;
    private boolean dirty;

    @Override
    public Optional<Connection> get(final UUID id) {
        return Optional.ofNullable(this.outgoing.get(id));
    }

    @Override
    public List<Connection> getOwnConnections() {
        return ImmutableList.copyOf(this.outgoing.values());
    }

    @Override
    public List<Connection> getAllConnections() {
        final ImmutableList.Builder<Connection> list = new ImmutableList.Builder<>();
        list.addAll(this.outgoing.values());
        if (this.world != null) {
            this.incoming.values().forEach(i -> i.get(this.world).ifPresent(list::add));
        }
        return list.build();
    }

    static class Incoming {
        final FastenerAccessor fastener;
        final UUID id;

        Incoming(final FastenerAccessor fastener, final UUID id) {
            this.fastener = fastener;
            this.id = id;
        }

        boolean gone(final Level world) {
            return this.fastener.isGone(world);
        }

        Optional<Connection> get(final Level world) {
            return this.fastener.get(world, false).map(Optional::of).orElse(Optional.empty()).flatMap(f -> f.get(this.id));
        }
    }
}
