package org.hedgetech.fairylightsredux.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.fastener.IFastenerHolder;
import org.jetbrains.annotations.NotNull;

public record UpdateEntityFastenerPayload(int entityId, CompoundTag fastener) implements CustomPacketPayload {
    public static final Type<UpdateEntityFastenerPayload> TYPE = new Type<>(Constants.id("update_entity_fastener"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateEntityFastenerPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, UpdateEntityFastenerPayload::entityId,
            ByteBufCodecs.COMPOUND_TAG, UpdateEntityFastenerPayload::fastener,
            UpdateEntityFastenerPayload::new
    );

    @Override
    public @NotNull Type<UpdateEntityFastenerPayload> type() {
        return TYPE;
    }

    public void handle(final Level world) {
        if (world.getEntity(this.entityId) instanceof final IFastenerHolder holder) {
            holder.getFastener().load(TagValueInput.create(ProblemReporter.DISCARDING, world.registryAccess(), this.fastener));
        }
    }
}
