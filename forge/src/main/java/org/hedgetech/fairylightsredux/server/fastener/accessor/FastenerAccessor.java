package org.hedgetech.fairylightsredux.server.fastener.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import net.minecraftforge.common.util.LazyOptional;
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;

public interface FastenerAccessor {
    default LazyOptional<Fastener<?>> get(final Level world) {
        return this.get(world, true);
    }

    LazyOptional<Fastener<?>> get(final Level world, final boolean load);

    boolean isGone(final Level world);

    FastenerType getType();

    CompoundTag serialize();

    void deserialize(CompoundTag compound);
}
