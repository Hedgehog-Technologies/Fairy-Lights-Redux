package org.hedgetech.fairylightsredux.registry;

import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.feature.light.LightBehavior;
import org.hedgetech.fairylightsredux.item.LightVariant;

import java.util.HashMap;
import java.util.Map;

public final class FLRLightVariants {
    private static final Map<ResourceLocation, LightVariant<?>> REGISTRY = new HashMap<>();
    public static final ResourceLocation BASIC = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "basic");

    public static void register(ResourceLocation id, LightVariant<?> variant) {
        REGISTRY.put(id, variant);
    }

    @SuppressWarnings("unchecked")
    public static <T extends LightBehavior> LightVariant<T> get(ResourceLocation id) {
        return (LightVariant<T>) REGISTRY.get(id);
    }
}
