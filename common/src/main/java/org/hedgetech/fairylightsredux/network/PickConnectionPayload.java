package org.hedgetech.fairylightsredux.network;

import net.minecraft.core.BlockPos;
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
import org.hedgetech.fairylightsredux.mixin.MixinServerGamePacketListenerImpl;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;

import java.util.UUID;

public record PickConnectionPayload(CompoundTag fastener, UUID connection, Vec3 hit) implements CustomPacketPayload {
    public static final Type<PickConnectionPayload> TYPE = new Type<>(Constants.id("pick_connection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PickConnectionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, PickConnectionPayload::fastener,
            UUIDUtil.STREAM_CODEC, PickConnectionPayload::connection,
            Vec3.STREAM_CODEC, PickConnectionPayload::hit,
            PickConnectionPayload::new
    );

    private static final double RANGE = (Connection.MAX_LENGTH + 1) * (Connection.MAX_LENGTH + 1);

    @Override
    public Type<PickConnectionPayload> type() {
        return TYPE;
    }

    public void handle(final ServerPlayer player) {
        final Level world = player.level();
        FastenerType.load(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), this.fastener))
                .flatMap(accessor -> accessor.get(world, false))
                .flatMap(fastener -> fastener.get(this.connection))
                .filter(connection -> connection.isModifiable(player)
                    && player.distanceToSqr(Vec3.atLowerCornerOf(connection.getFastener().getPos())) < RANGE
                    && player.canInteractWithBlock(BlockPos.containing(this.hit), 1.0D))
                .ifPresent(connection -> ((MixinServerGamePacketListenerImpl) player.connection).fairylightsredux$tryPickItem(connection.getItemStack()));
    }
}
