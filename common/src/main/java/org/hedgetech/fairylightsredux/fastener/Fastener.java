package org.hedgetech.fairylightsredux.fastener;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.connection.Connection;
import org.hedgetech.fairylightsredux.connection.ConnectionType;
import org.hedgetech.fairylightsredux.fastener.accessor.FastenerAccessor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Fastener<F extends FastenerAccessor> {
    Optional<Connection> get(final UUID id);

    List<Connection> getOwnConnections();

    List<Connection> getAllConnections();

    default Optional<Connection> getFirstConnection() {
        return this.getAllConnections().stream().findFirst();
    }

    AABB getBounds();

    Vec3 getConnectionPoint();

    BlockPos getPos();

    Direction getFacing();

    void setWorld(Level world);

    @Nullable
    Level getWorld();

    F createAccessor();

    boolean isMoving();

    default void resistSnap(final Vec3 from) {}

    @Deprecated(since = "May not be needed? (why did I think this?)")
    boolean update();

    void setDirty();

    void dropItems(Level world, BlockPos pos);

    void remove();

    boolean hasNoConnections();

    boolean hasConnectionWith(Fastener<?> fastener);

    @Nullable
    Connection getConnectionTo(FastenerAccessor destination);

    boolean removeConnection(UUID uuid);

    boolean removeConnection(Connection connection);

    boolean reconnect(final Level world, Connection connection, Fastener<?> newDestination);

    @Deprecated(since = "May need to be updated to remove CompoundTag")
    Connection connect(Level world, Fastener<?> destination, ConnectionType<?> type, CompoundTag tag, final boolean drop);

    @Deprecated(since = "May need to be updated to remove CompoundTag")
    Connection createOutgoingConnection(Level world, UUID uuid, Fastener<?> destination, ConnectionType<?> type, CompoundTag tag, final boolean drop);

    void createIncomingConnection(Level world, UUID uuid, Fastener<?> destination, ConnectionType<?> type);
}
