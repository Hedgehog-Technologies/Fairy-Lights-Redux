package org.hedgetech.fairylightsredux.server.net;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;
import org.hedgetech.fairylightsredux.server.fastener.accessor.FastenerAccessor;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class ConnectionMessage implements IMessage {
    public BlockPos pos;
    public FastenerAccessor accessor;
    public UUID uuid;

    public ConnectionMessage() {}

    public ConnectionMessage(final Connection connection) {
        final Fastener<?> fastener = connection.getFastener();
        this.pos = fastener.getPos();
        this.accessor = fastener.createAccessor();
        this.uuid = connection.getUUID();
    }

    @Override
    public void encode(final FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeNbt(FastenerType.serialize(this.accessor));
        buf.writeUUID(this.uuid);
    }

    @Override
    public void decode(final FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.accessor = FastenerType.deserialize(Objects.requireNonNull(buf.readNbt()));
        this.uuid = buf.readUUID();
    }

    @SuppressWarnings("unchecked")
    public static <C extends Connection> Optional<C> getConnection(final ConnectionMessage message, final Predicate<? super Connection> typePredicate, final Level world) {
        return message.accessor.get(world, false)
                .map(Optional::of)
                .orElse(Optional.empty())
                .flatMap(f -> (Optional<C>) f.get(message.uuid).filter(typePredicate));
    }
}
