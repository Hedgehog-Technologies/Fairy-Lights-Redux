package org.hedgetech.fairylightsredux.fastener;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.connection.Connection;
import org.hedgetech.fairylightsredux.connection.ConnectionType;
import org.hedgetech.fairylightsredux.fastener.accessor.FastenerAccessor;
import org.hedgetech.fairylightsredux.util.AABBBuilder;
import org.hedgetech.fairylightsredux.util.Curve;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class AbstractFastener<F extends FastenerAccessor> implements Fastener<F> {
    private final Map<UUID, Connection> outgoing = new HashMap<>();
    private final Map<UUID, Incoming> incoming = new HashMap<>();
    private @Nullable Level world;
    private boolean dirty;

    protected AABB bounds = AABBBuilder.INFINITE_EXTENT_AABB;

    @Override
    public Optional<Connection> get(final UUID uuid) {
        return Optional.ofNullable(this.outgoing.get(uuid));
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

    @Override
    public AABB getBounds() {
        return this.bounds;
    }

    @Override
    public abstract BlockPos getPos();

    @Override
    public void setWorld(final Level world) {
        this.world = world;
        this.outgoing.values().forEach(c -> c.setWorld(world));
    }

    @Nullable
    @Override
    public Level getWorld() {
        return this.world;
    }

    @Override
    public boolean update() {
        final Iterator<Connection> it = this.outgoing.values().iterator();
        final Vec3 fromOffset = this.getConnectionPoint();
        boolean dirty = this.dirty;
        this.dirty = false;
        while (it.hasNext()) {
            final Connection connection = it.next();
            if (connection.update(fromOffset)) {
                dirty = true;
            }
            if (connection.isRemoved()) {
                dirty = true;
                it.remove();
                this.incoming.remove(connection.getUUID());
                if (this.world != null) {
                    this.drop(this.world, this.getPos(), connection);
                }
            }
        }
        if (this.world != null) {
            this.incoming.values().removeIf(i -> i.isGone(this.world));
        }
        if (dirty) {
            this.calculateBoundingBox();
        }
        return dirty;
    }

    @Override
    public void setDirty() {
        this.dirty = true;
    }

    protected void calculateBoundingBox() {
        if (this.outgoing.isEmpty()) {
            this.bounds = new AABB(this.getPos());
            return;
        }
        final AABBBuilder builder = new AABBBuilder();
        for (final Connection connection : this.outgoing.values()) {
            final Curve catenary = connection.getCatenary();
            if (catenary == null) continue;
            final Curve.SegmentIterator it = catenary.iterator();
            while (it.next()) {
                builder.include(it.getX(0.0F), it.getY(0.0F), it.getZ(0.0F));
                if (!it.hasNext()) {
                    builder.include(it.getX(1.0F), it.getY(1.0F), it.getZ(1.0F));
                }
            }
        }
        this.bounds = builder.add(this.getConnectionPoint()).build();
    }

    @Override
    public void dropItems(final Level world, final BlockPos pos) {
        for (final Connection connection : this.getAllConnections()) {
            this.drop(world, pos, connection);
        }
    }

    private void drop(final Level world, final BlockPos pos, final Connection connection) {
        if (!connection.shouldDrop()) return;
        final float offsetX = world.random.nextFloat() * 0.8F + 0.1F;
        final float offsetY = world.random.nextFloat() * 0.8F + 0.1F;
        final float offsetZ = world.random.nextFloat() * 0.8F + 0.1F;
        final ItemStack stack = connection.getItemStack();
        final ItemEntity entityItem = new ItemEntity(world, pos.getX() + offsetX, pos.getY() + offsetY, pos.getZ() + offsetZ, stack);
        final float scale = 0.05F;
        entityItem.setDeltaMovement(
                world.random.nextGaussian() * scale,
                world.random.nextGaussian() * scale + 0.2F,
                world.random.nextGaussian() * scale
        );
        world.addFreshEntity(entityItem);
        connection.setShouldDrop(false);
    }

    @Override
    public void remove() {
        this.outgoing.values().forEach(Connection::remove);
    }

    @Override
    public boolean hasNoConnections() {
        return this.outgoing.isEmpty() && this.incoming.isEmpty();
    }

    @Override
    public boolean hasConnectionWith(final Fastener<?> fastener) {
        return this.getConnectionTo(fastener.createAccessor()) != null;
    }

    @Nullable
    @Override
    public Connection getConnectionTo(final FastenerAccessor destination) {
        for (final Connection connection : this.outgoing.values()) {
            if (connection.isDestination(destination)) {
                return connection;
            }
        }
        return null;
    }

    @Override
    public boolean removeConnection(final UUID uuid) {
        boolean removed = false;
        final Connection connection = this.outgoing.remove(uuid);
        if (connection != null) {
            connection.remove();
            this.setDirty();
            removed = true;
        } else if (this.incoming.remove(uuid) != null) {
            this.setDirty();
            removed = true;
        }
        return removed;
    }

    @Override
    public boolean removeConnection(final Connection connection) {
        return this.removeConnection(connection.getUUID());
    }

    @Override
    public boolean reconnect(final Level world, final Connection connection, final Fastener<?> newDestination) {
        if (this.equals(newDestination) || newDestination.hasConnectionWith(this)) return false;
        final UUID uuid = connection.getUUID();
        if (connection.getDestination().get(world, false).filter(f -> {
            f.removeConnection(uuid);
            return true;
        }).isPresent()) {
            connection.setDestination(newDestination);
            connection.setShouldDrop(true);
            newDestination.createIncomingConnection(this.world, uuid, this, connection.getType());
            this.setDirty();
            return true;
        }
        return false;
    }

    @Deprecated(since = "May need to be updated to remove CompoundTag")
    @Override
    public Connection connect(final Level world, final Fastener<?> destination, final ConnectionType<?> type, final CompoundTag tag, final boolean drop) {
        throw new NotImplementedException("AbstractFastener.connect");
//        final UUID uuid = Mth.createInsecureUUID();
//        final Connection connection = this.createOutgoingConnection(world, uuid, destination, type, compound, drop);
//        destination.createIncomingConnection(world, uuid, this, type);
//        return connection;
    }

    @Deprecated(since = "May need to be updated to remove CompoundTag")
    @Override
    public Connection createOutgoingConnection(final Level world, final UUID uuid, final Fastener<?> destination, final ConnectionType<?> type, final CompoundTag tag, final boolean drop) {
        throw new NotImplementedException("AbstractFastener.createOutgoingConnection");
//        final Connection c = type.create(world, this, uuid);
//        c.deserialize(destination, compound, drop);
//        this.outgoing.put(uuid, c);
//        this.setDirty();
//        return c;
    }

    @Override
    public void createIncomingConnection(final Level world, final UUID uuid, final Fastener<?> destination, final ConnectionType<?> type) {
        this.incoming.put(uuid, new Incoming(destination.createAccessor(), uuid));
        this.setDirty();
    }

    // TODO - Serialization
//    @Override
//    public CompoundTag serializeNBT() {
//        final CompoundTag compound = new CompoundTag();
//        final ListTag outgoing = new ListTag();
//        for (final Map.Entry<UUID, Connection> connectionEntry : this.outgoing.entrySet()) {
//            final UUID uuid = connectionEntry.getKey();
//            final Connection connection = connectionEntry.getValue();
//            final CompoundTag connectionCompound = new CompoundTag();
//            connectionCompound.put("connection", connection.serialize());
//            connectionCompound.putString("type", RegistryObjects.getName(FairyLights.CONNECTION_TYPES.get(), connection.getType()).toString());
//            connectionCompound.putUUID("uuid", uuid);
//            outgoing.add(connectionCompound);
//        }
//        compound.put("outgoing", outgoing);
//        final ListTag incoming = new ListTag();
//        for (final Map.Entry<UUID, Incoming> e : this.incoming.entrySet()) {
//            final CompoundTag tag = new CompoundTag();
//            tag.putUUID("uuid", e.getKey());
//            tag.put("fastener", FastenerType.serialize(e.getValue().fastener));
//            incoming.add(tag);
//        }
//        compound.put("incoming", incoming);
//        return compound;
//    }
//
//    @Override
//    public void deserializeNBT(final CompoundTag compound) {
//        final ListTag listConnections = compound.getList("outgoing", Tag.TAG_COMPOUND);
//        final List<UUID> nbtUUIDs = new ArrayList<>();
//        for (int i = 0; i < listConnections.size(); i++) {
//            final CompoundTag connectionCompound = listConnections.getCompound(i);
//            final UUID uuid;
//            if (connectionCompound.hasUUID("uuid")) {
//                uuid = connectionCompound.getUUID("uuid");
//            } else {
//                uuid = Mth.createInsecureUUID();
//            }
//            nbtUUIDs.add(uuid);
//            if (this.outgoing.containsKey(uuid)) {
//                final Connection connection = this.outgoing.get(uuid);
//                connection.deserialize(connectionCompound.getCompound("connection"));
//            } else {
//                final ConnectionType<?> type = FairyLights.CONNECTION_TYPES.get().getValue(ResourceLocation.tryParse(connectionCompound.getString("type")));
//                if (type != null) {
//                    final Connection connection = type.create(this.world, this, uuid);
//                    connection.deserialize(connectionCompound.getCompound("connection"));
//                    this.outgoing.put(uuid, connection);
//                }
//            }
//        }
//        final Iterator<Map.Entry<UUID, Connection>> connectionsIter = this.outgoing.entrySet().iterator();
//        while (connectionsIter.hasNext()) {
//            final Map.Entry<UUID, Connection> connection = connectionsIter.next();
//            if (!nbtUUIDs.contains(connection.getKey())) {
//                connectionsIter.remove();
//                connection.getValue().remove();
//            }
//        }
//        this.incoming.clear();
//        final ListTag incoming = compound.getList("incoming", Tag.TAG_COMPOUND);
//        for (int i = 0; i < incoming.size(); i++) {
//            final CompoundTag incomingNbt = incoming.getCompound(i);
//            final UUID uuid = incomingNbt.getUUID("uuid");
//            final FastenerAccessor fastener = FastenerType.deserialize(incomingNbt.getCompound("fastener"));
//            this.incoming.put(uuid, new Incoming(fastener, uuid));
//        }
//        this.setDirty();
//    }

//    private final Optional<Fastener<?>> optional = Optional.of(this);
//
//    @Override
//    public <T> Optional<T> getCapability(final Capability<T> capability, final Direction facing) {
//        return capability == CapabilityHandler.FASTENER_CAP ? this.optional.cast() : Optional.empty();
//    }

    static class Incoming {
        final FastenerAccessor fastener;
        final UUID uuid;

        Incoming(final FastenerAccessor fastener, final UUID uuid) {
            this.fastener = fastener;
            this.uuid = uuid;
        }

        boolean isGone(final Level world) {
            return this.fastener.isGone(world);
        }

        Optional<Connection> get(final Level world) {
            return this.fastener.get(world, false)
                    .flatMap(f -> f.get(this.uuid));
        }
    }
}
