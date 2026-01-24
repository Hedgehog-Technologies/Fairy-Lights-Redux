package org.hedgetech.fairylightsredux.fastener.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.fastener.FastenerType;

import java.util.Optional;

public interface FastenerAccessor {
    default Optional<Fastener<?>> get(final Level world) {
        return this.get(world, true);
    }

    Optional<Fastener<?>> get(final Level world, final boolean load);

    boolean isGone(final Level world);

    FastenerType getType();

    void writeToBuf(FriendlyByteBuf buf);

    FastenerAccessor readFromBuf(FriendlyByteBuf buf);

    @Deprecated(since = "Use FastenerAccessor.writeToBuf instead", forRemoval = true)
    CompoundTag serialize();

    @Deprecated(since = "Use FastenerAccessor.readFromBuf instead", forRemoval = true)
    void deserialize(CompoundTag tag);
}
