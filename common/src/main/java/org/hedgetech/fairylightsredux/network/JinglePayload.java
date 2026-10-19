package org.hedgetech.fairylightsredux.network;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.Fastener.FastenerType;
import org.hedgetech.fairylightsredux.server.connection.HangingLightsConnection;
import org.hedgetech.fairylightsredux.server.jingle.Jingle;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record JinglePayload(CompoundTag fastener, UUID connection, int lightOffset, Jingle jingle) implements CustomPacketPayload {
    public static final Type<JinglePayload> TYPE = new Type<>(Constants.id("jingle"));

    public static final StreamCodec<RegistryFriendlyByteBuf, JinglePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, JinglePayload::fastener,
            UUIDUtil.STREAM_CODEC, JinglePayload::connection,
            ByteBufCodecs.VAR_INT, JinglePayload::lightOffset,
            Jingle.STREAM_CODEC, JinglePayload::jingle,
            JinglePayload::new
    );

    @Override
    public @NotNull Type<JinglePayload> type() {
        return TYPE;
    }

    public void handle(final Level world) {
        FastenerType.load(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), this.fastener))
                .flatMap(accessor -> accessor.get(world, false))
                .flatMap(fastener -> fastener.get(this.connection))
                .ifPresent(connection -> {
                    if (connection instanceof final HangingLightsConnection hangingLights) {
                        hangingLights.play(this.jingle, this.lightOffset);
                    }
                });
    }
}
