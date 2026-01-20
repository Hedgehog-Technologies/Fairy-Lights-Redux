package org.hedgetech.fairylightsredux.server.string;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;

public record StringType(int color) {
    public static final ResourceLocation KEY = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "string_type");

    public static final Codec<StringType> CODEC = Codec.INT
            .fieldOf("color")
            .xmap(StringType::new, StringType::color)
            .codec();

    public static final DataComponentType<StringType> TYPE = DataComponentType.<StringType>builder()
            .persistent(StringType.CODEC)
            .build();
}
