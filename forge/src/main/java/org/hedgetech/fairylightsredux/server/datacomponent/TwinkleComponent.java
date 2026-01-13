package org.hedgetech.fairylightsredux.server.datacomponent;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;

public record TwinkleComponent(boolean hasTwinkle) {
    public static final ResourceLocation KEY = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "twinkle");

    public static final Codec<TwinkleComponent> CODEC = Codec.BOOL
            .fieldOf("hasTwinkle")
            .xmap(TwinkleComponent::new, TwinkleComponent::hasTwinkle)
            .codec();

    public static final DataComponentType<TwinkleComponent> TYPE = DataComponentType.<TwinkleComponent>builder()
            .persistent(CODEC)
            .build();
}
