package org.hedgetech.fairylightsredux.network;

import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.connection.ILettered;
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record EditLetteredConnectionPayload(CompoundTag fastener, UUID connection, StyledString text) implements CustomPacketPayload {
    public static final Type<EditLetteredConnectionPayload> TYPE = new Type<>(Constants.id("edit_lettered_connection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, EditLetteredConnectionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, EditLetteredConnectionPayload::fastener,
            UUIDUtil.STREAM_CODEC, EditLetteredConnectionPayload::connection,
            StyledString.STREAM_CODEC, EditLetteredConnectionPayload::text,
            EditLetteredConnectionPayload::new
    );

    private static final double RANGE = (Connection.MAX_LENGTH + 1) * (Connection.MAX_LENGTH + 1);

    @Override
    public @NotNull Type<EditLetteredConnectionPayload> type() {
        return TYPE;
    }

    public void handle(final ServerPlayer player) {
        final Level world = player.level();
        FastenerType.load(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), this.fastener))
                .flatMap(accessor -> accessor.get(world, false))
                .flatMap(fastener -> fastener.get(this.connection))
                .filter(connection -> connection.isModifiable(player)
                    && player.distanceToSqr(Vec3.atLowerCornerOf(connection.getFastener().getPos())) < RANGE)
                .ifPresent(connection -> {
                    if (connection instanceof final ILettered lettered && lettered.isSupportedText(this.text)) {
                        lettered.setText(this.text);
                    }
                });
    }
}
