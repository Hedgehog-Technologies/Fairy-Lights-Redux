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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.Fastener.FastenerType;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.connection.PlayerAction;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record InteractionConnectionPayload(CompoundTag fastener, UUID connection, PlayerAction action, Vec3 hit, FeatureType featureType, int feature) implements CustomPacketPayload {
    public static final Type<InteractionConnectionPayload> TYPE = new Type<>(Constants.id("interaction_connection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, InteractionConnectionPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, InteractionConnectionPayload::fastener,
            UUIDUtil.STREAM_CODEC, InteractionConnectionPayload::connection,
            PlayerAction.STREAM_CODEC, InteractionConnectionPayload::action,
            Vec3.STREAM_CODEC, InteractionConnectionPayload::hit,
            FeatureType.STREAM_CODEC, InteractionConnectionPayload::featureType,
            ByteBufCodecs.VAR_INT, InteractionConnectionPayload::feature,
            InteractionConnectionPayload::new
    );

    private static final double RANGE = (Connection.MAX_LENGTH + 1) * (Connection.MAX_LENGTH + 1);

    @Override
    public @NotNull Type<InteractionConnectionPayload> type() {
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
                .ifPresent(connection -> {
                    if (this.action == PlayerAction.ATTACK) {
                        connection.disconnect(this.hit);
                    } else {
                        this.interact(player, connection);
                    }
                });
    }

    private void interact(final ServerPlayer player, final Connection connection) {
        for (final InteractionHand hand : InteractionHand.values()) {
            final ItemStack stack = player.getItemInHand(hand);
            final int count = stack.getCount();

            if (connection.interact(player, this.hit, this.featureType, this.feature, stack, hand)) {
                if (player.hasInfiniteMaterials() && stack.getCount() < count) {
                    stack.setCount(count);
                }
                break;
            }
        }
    }
}
