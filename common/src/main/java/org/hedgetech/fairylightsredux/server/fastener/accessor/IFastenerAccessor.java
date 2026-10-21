package org.hedgetech.fairylightsredux.server.fastener.accessor;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;

import java.util.Optional;

public interface IFastenerAccessor {
    default Optional<Fastener<?>> get(final Level world) {
        return this.get(world, true);
    }

    Optional<Fastener<?>> get(final Level world, final boolean load);

    boolean isGone(final Level world);

    FastenerType getType();

    void save(ValueOutput output);

    boolean load(ValueInput input);
}
