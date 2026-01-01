package org.hedgetech.fairylightsredux.server.fastener;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public record FastenerData(Map<UUID, OutgoingData> outgoing, Map<UUID, IncomingData> incoming) {
    public static final FastenerData EMPTY = new FastenerData(Map.of(), Map.of());

    public static final Codec<FastenerData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.unboundedMap(UUIDUtil.CODEC, OutgoingData.CODEC)
                    .fieldOf("outgoing")
                    .forGetter(FastenerData::outgoing),
            Codec.unboundedMap(UUIDUtil.CODEC, IncomingData.CODEC)
                    .fieldOf("incoming")
                    .forGetter(FastenerData::incoming)
    ).apply(inst, FastenerData::new));

    public record OutgoingData(UUID uuid, ResourceLocation type, ConnectionData connection) {
        public static final Codec<OutgoingData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                UUIDUtil.CODEC.fieldOf("uuid").forGetter(OutgoingData::uuid),
                ResourceLocation.CODEC.fieldOf("type").forGetter(OutgoingData::type),
                ConnectionData.CODEC.fieldOf("connection").forGetter(OutgoingData::connection)
        ).apply(inst, OutgoingData::new));
    }

    public record IncomingData(UUID uuid, FastenerTypeData fastener) {
        public static final Codec<IncomingData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                UUIDUtil.CODEC.fieldOf("uuid").forGetter(IncomingData::uuid),
                FastenerTypeData.CODEC.fieldOf("fastener").forGetter(IncomingData::fastener)
        ).apply(inst, IncomingData::new));
    }
}
