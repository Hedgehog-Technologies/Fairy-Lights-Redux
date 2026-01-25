package org.hedgetech.fairylightsredux.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.ResourceLocation;

public record LightVariantType<V extends LightVariant<?>>(
        ResourceLocation id,
        MapCodec<V> codec
) {}
