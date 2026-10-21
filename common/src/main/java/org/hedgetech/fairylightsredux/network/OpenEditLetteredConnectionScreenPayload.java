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
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public record OpenEditLetteredConnectionScreenPayload(CompoundTag fastener, UUID connection) implements CustomPacketPayload {
    public static final Type<OpenEditLetteredConnectionScreenPayload> TYPE = new Type<>(Constants.id("open_edit_lettered_connection_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenEditLetteredConnectionScreenPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, OpenEditLetteredConnectionScreenPayload::fastener,
            UUIDUtil.STREAM_CODEC, OpenEditLetteredConnectionScreenPayload::connection,
            OpenEditLetteredConnectionScreenPayload::new
    );

    @Override
    public @NotNull Type<OpenEditLetteredConnectionScreenPayload> type() {
        return TYPE;
    }

    public Optional<Connection> getConnection(final Level world) {
        return FastenerType.load(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), this.fastener))
                .flatMap(accessor -> accessor.get(world, false))
                .flatMap(fastener -> fastener.get(this.connection));
    }
}
