package org.hedgetech.fairylightsredux.server.item.component;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.hedgetech.fairylightsredux.Constants;

public record FLRColor(DyeColor color) {
    public static final ResourceLocation KEY = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flr_color");

    public static final Codec<FLRColor> CODEC = DyeColor.CODEC.xmap(FLRColor::new, FLRColor::color);

    public static final DataComponentType<FLRColor> TYPE = DataComponentType.<FLRColor>builder()
            .persistent(FLRColor.CODEC)
            .build();
}
