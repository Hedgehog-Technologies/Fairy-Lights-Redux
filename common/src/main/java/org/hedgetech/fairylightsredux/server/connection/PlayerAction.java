package org.hedgetech.fairylightsredux.server.connection;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum PlayerAction {
    ATTACK,
    INTERACT;

    private static final IntFunction<PlayerAction> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

    public static final StreamCodec<ByteBuf, PlayerAction> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
}
