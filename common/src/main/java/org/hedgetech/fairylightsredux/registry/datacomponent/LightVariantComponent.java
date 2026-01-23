package org.hedgetech.fairylightsredux.registry.datacomponent;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

public record LightVariantComponent(ResourceLocation variantId) {
    public static final Codec<LightVariantComponent> CODEC =
            ResourceLocation.CODEC.xmap(
                    LightVariantComponent::new,
                    LightVariantComponent::variantId
            );

    public static final StreamCodec<FriendlyByteBuf, LightVariantComponent> STREAM_CODEC =
            StreamCodec.composite(
                    ResourceLocation.STREAM_CODEC,
                    LightVariantComponent::variantId,
                    LightVariantComponent::new
            );

    public static final DataComponentType<LightVariantComponent> TYPE =
            DataComponentType.<LightVariantComponent>builder()
                    .persistent(CODEC)
                    .networkSynchronized(STREAM_CODEC)
                    .build();

}
