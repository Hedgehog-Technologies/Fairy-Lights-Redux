package org.hedgetech.fairylightsredux.fastener.accessor;

import net.minecraft.nbt.CompoundTag;
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

    @Deprecated(since = "Compound tags be damned")
    CompoundTag serialize();

    @Deprecated(since = "Compound tags be damned")
    void deserialize(CompoundTag tag);
}
