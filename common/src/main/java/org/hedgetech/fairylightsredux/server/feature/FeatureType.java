package org.hedgetech.fairylightsredux.server.feature;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;

public final class FeatureType {
    private static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "feature");
    private static final DefaultedRegistry<FeatureType> REGISTRY = new DefaultedMappedRegistry<>(
            "default",
            ResourceKey.createRegistryKey(LOCATION),
            Lifecycle.experimental(),
            false
    );

    public static final FeatureType DEFAULT = register("default");

    public int getId() {
        return REGISTRY.getId(this);
    }

    public static FeatureType register(final String name) {
        return Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "ft_" + name), new FeatureType());
    }

    public static FeatureType fromId(final int id) {
        return REGISTRY.byId(id);
    }

    private FeatureType() {}
}
