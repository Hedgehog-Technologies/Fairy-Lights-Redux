package org.hedgetech.fairylightsredux.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class LightVariantTypes {
    private static final Map<ResourceLocation, LightVariantType<?>> BY_ID = new HashMap<>();

    public static <V extends LightVariant<?>> LightVariantType<V> register(ResourceLocation id, MapCodec<V> codec) {
        LightVariantType<V> type = new LightVariantType<>(id, codec);
        BY_ID.put(id, type);
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <V extends LightVariant<?>> LightVariantType<V> byId(ResourceLocation id) {
        return (LightVariantType<V>) BY_ID.get(id);
    }

    public static Collection<LightVariantType<?>> values() {
        return BY_ID.values();
    }
}
