package org.hedgetech.fairylightsredux.server.datacomponent;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.hedgetech.fairylightsredux.Constants;

public record ColorComponent(DyeColor color) {
    public static final ResourceLocation KEY = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flr_color");

    public static final Codec<ColorComponent> CODEC = DyeColor.CODEC.xmap(ColorComponent::new, ColorComponent::color);

    public static final DataComponentType<ColorComponent> TYPE = DataComponentType.<ColorComponent>builder()
            .persistent(CODEC)
            .build();
}
