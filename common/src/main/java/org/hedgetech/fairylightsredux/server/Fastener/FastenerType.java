package org.hedgetech.fairylightsredux.server.Fastener;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.hedgetech.fairylightsredux.server.Fastener.accessor.BlockFastenerAccessor;
import org.hedgetech.fairylightsredux.server.Fastener.accessor.FenceFastenerAccessor;
import org.hedgetech.fairylightsredux.server.Fastener.accessor.IFastenerAccessor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public enum FastenerType {
    BLOCK(BlockFastenerAccessor::new),
    FENCE(FenceFastenerAccessor::new);

    private static final Map<String, FastenerType> NAME_TO_TYPE = new HashMap<>();

    static {
        for (final FastenerType type : values()) {
            NAME_TO_TYPE.put(type.name, type);
        }
    }

    private final Supplier<? extends IFastenerAccessor> supplier;
    private final String name;

    FastenerType(final Supplier<? extends IFastenerAccessor> supplier) {
        this.supplier = supplier;
        this.name = this.name().toLowerCase(Locale.ENGLISH);
    }

    public static void save(final ValueOutput output, final IFastenerAccessor accessor) {
        output.putString("type", accessor.getType().name);
        accessor.save(output.child("data"));
    }

    public static Optional<IFastenerAccessor> load(final ValueInput input) {
        return input.getString("type").map(NAME_TO_TYPE::get).flatMap(type -> {
            final IFastenerAccessor accessor = type.supplier.get();
            return accessor.load(input.childOrEmpty("data"))
                    ? Optional.of(accessor)
                    : Optional.empty();
        });
    }
}
