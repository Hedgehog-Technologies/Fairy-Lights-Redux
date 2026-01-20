package org.hedgetech.fairylightsredux.server.datacomponent;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;

import java.util.List;

public record ColorsComponent(List<Integer> colors) {
    public static final ResourceLocation KEY = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "colors");

    public static final Codec<ColorsComponent> CODEC = Codec.list(Codec.INT)
            .fieldOf("colors")
            .xmap(ColorsComponent::new, ColorsComponent::colors)
            .codec();

    public static final DataComponentType<ColorsComponent> TYPE = DataComponentType.<ColorsComponent>builder()
            .persistent(CODEC)
            .build();
}
