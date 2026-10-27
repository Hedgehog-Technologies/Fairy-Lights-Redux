package org.hedgetech.fairylightsredux.server.component;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Unit;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.FLRNames;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;

import java.util.List;
import java.util.function.BiConsumer;

public final class FLRDataComponents {
    public static DataComponentType<HangingLights> HANGING_LIGHTS;
    public static DataComponentType<PennantBunting> PENNANT_BUNTING;
    public static DataComponentType<StyledString> LETTER_BUNTING;
    public static DataComponentType<Unit> TWINKLE;
    public static DataComponentType<List<Integer>> COLORS;

    public static void register(final BiConsumer<ResourceLocation, DataComponentType<?>> registry) {
        HANGING_LIGHTS = register(registry, FLRNames.HANGING_LIGHTS, DataComponentType.<HangingLights>builder()
                .persistent(HangingLights.CODEC)
                .networkSynchronized(HangingLights.STREAM_CODEC)
                .cacheEncoding()
                .build()
        );
        PENNANT_BUNTING = register(registry, FLRNames.PENNANT_BUNTING, DataComponentType.<PennantBunting>builder()
                .persistent(PennantBunting.CODEC)
                .networkSynchronized(PennantBunting.STREAM_CODEC)
                .cacheEncoding()
                .build()
        );
        LETTER_BUNTING = register(registry, FLRNames.LETTER_BUNTING, DataComponentType.<StyledString>builder()
                .persistent(StyledString.CODEC)
                .networkSynchronized(StyledString.STREAM_CODEC)
                .cacheEncoding()
                .build()
        );
        TWINKLE = register(registry, FLRNames.TWINKLE, DataComponentType.<Unit>builder()
                .persistent(Unit.CODEC)
                .networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
                .build()
        );
        COLORS = register(registry, FLRNames.COLORS, DataComponentType.<List<Integer>>builder()
                .persistent(ExtraCodecs.nonEmptyList(ExtraCodecs.RGB_COLOR_CODEC.listOf()))
                .networkSynchronized(ByteBufCodecs.INT.apply(ByteBufCodecs.list()))
                .build()
        );
    }

    private static <T> DataComponentType<T> register(final BiConsumer<ResourceLocation, DataComponentType<?>> registry, final String name, final DataComponentType<T> type) {
        registry.accept(Constants.id(name), type);
        return type;
    }

    private FLRDataComponents() {}
}
