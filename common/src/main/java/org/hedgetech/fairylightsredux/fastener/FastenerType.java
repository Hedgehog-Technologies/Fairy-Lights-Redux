package org.hedgetech.fairylightsredux.fastener;

import net.minecraft.nbt.CompoundTag;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.fastener.accessor.BlockFastenerAccessor;
import org.hedgetech.fairylightsredux.fastener.accessor.FastenerAccessor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public enum FastenerType {
    BLOCK(BlockFastenerAccessor::new),
    FENCE(FenceFastenerAccessor::new),
    PLAYER(PlayerFastenerAccessor::new);

    private static final Map<String, FastenerType> NAME_TO_TYPE = new HashMap<>();

    private final Supplier<? extends FastenerAccessor> supplier;
    private final String name;

    FastenerType(final Supplier<? extends FastenerAccessor> supplier) {
        this.supplier = supplier;
        this.name = this.name().toLowerCase(Locale.ENGLISH);
    }

    public final FastenerAccessor createAccessor() {
        return this.supplier.get();
    }

    @Deprecated(since = "Compound tags be damned")
    public static CompoundTag serialize(final FastenerAccessor accessor) {
        throw new NotImplementedException("FastenerType.serialize");
//        final CompoundTag compound = new CompoundTag();
//        compound.putString("type", accessor.getType().name);
//        compound.put("data", accessor.serialize());
//        return compound;
    }

    @Deprecated(since = "Compound tags be damned")
    public static FastenerAccessor deserialize(final CompoundTag compound) {
        throw new NotImplementedException("FastenerType.deserialize");
//        final FastenerAccessor accessor = NAME_TO_TYPE.get(compound.getString("type")).createAccessor();
//        accessor.deserialize(compound.getCompound("data"));
//        return accessor;
    }

    static {
        for (final FastenerType type : values()) {
            NAME_TO_TYPE.put(type.name, type);
        }
    }
}
