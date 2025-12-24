package org.hedgetech.fairylightsredux.server.fastener;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.fastener.accessor.FastenerAccessor;

import java.util.Optional;
import java.util.UUID;

public interface Fastener<F extends FastenerAccessor> extends ICapabilitySerializable<CompoundTag> {
    Optional<Connection> get(final UUID id);
}
