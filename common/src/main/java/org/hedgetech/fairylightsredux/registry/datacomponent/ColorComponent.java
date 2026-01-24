package org.hedgetech.fairylightsredux.registry.datacomponent;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;

public record ColorComponent(int color) {
    public static final Codec<ColorComponent> CODEC = Codec.INT
            .fieldOf("color")
            .xmap(ColorComponent::new, ColorComponent::color)
            .codec();

    public static final DataComponentType<ColorComponent> TYPE = DataComponentType.<ColorComponent>builder()
            .persistent(CODEC)
            .build();
}
