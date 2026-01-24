package org.hedgetech.fairylightsredux.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.connection.Connection;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.fastener.FastenerType;
import org.hedgetech.fairylightsredux.fastener.accessor.FastenerAccessor;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class ConnectionMessage implements Message {
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
        buf.writeUtf(this.accessor.getType().name());
        this.accessor.writeToBuf(buf);
        buf.writeUUID(this.uuid);
    }

    @Override
    public void decode(final FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        final String typeName = buf.readUtf();
        FastenerAccessor accessor = FastenerType.fromName(typeName).createAccessor();
        this.accessor = accessor.readFromBuf(buf);
        buf.readUUID();
    }

    @SuppressWarnings("unchecked")
    public static <C extends Connection> Optional<C> getConnection(final ConnectionMessage msg, final Predicate<? super Connection> typePredicate, final Level world) {
        return msg.accessor.get(world, false)
                .flatMap(f -> (Optional<C>) f.get(msg.uuid).filter(typePredicate));
    }
}
