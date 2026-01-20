package org.hedgetech.fairylightsredux._old.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public record ConnectionData(ResourceLocation type, CompoundTag payload) {
    public static final Codec<ConnectionData> CODEC = RecordCodecBuilder.create(

    );

    public static final DataComponentType<ConnectionData> CONNECTION = DataComponentType.builder()
            .persistent(ConnectionData.CODEC)
            .networkSynchronized(ConnectionData.STREAM_CODEC)
            .build();
}
