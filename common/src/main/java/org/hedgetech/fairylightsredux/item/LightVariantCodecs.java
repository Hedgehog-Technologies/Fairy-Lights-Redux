package org.hedgetech.fairylightsredux.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public final class LightVariantCodecs {
    public static final MapCodec<LightVariant<?>> CODEC = ResourceLocation.CODEC.dispatchMap(
            variant -> variant.type().id(),
            id -> {
                LightVariantType<?> type = LightVariantTypes.byId(id);
                if (type == null) {
                    throw new IllegalArgumentException("Unknown LightVariant type: " + id);
                }
                return type.codec();
            }
    );
}
