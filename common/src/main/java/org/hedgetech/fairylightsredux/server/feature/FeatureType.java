package org.hedgetech.fairylightsredux.server.feature;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum FeatureType {
    CORD,
    FEATURE;

    private static final IntFunction<FeatureType> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

    public static final StreamCodec<ByteBuf, FeatureType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
}
