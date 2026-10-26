package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.FLRNames;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;

import java.util.function.BiConsumer;

public final class FLRItems {
    public static ConnectionItem HANGING_LIGHTS;
    public static ConnectionItem PENNANT_BUNTING;
    public static ConnectionItem TINSEL;
    public static ConnectionItem LETTER_BUNTING;
    public static ConnectionItem GARLAND;

    public static Item TRIANGLE_PENNANT;
    public static Item SPEARHEAD_PENNANT;
    public static Item SWALLOWTAIL_PENNANT;
    public static Item SQUARE_PENNANT;

    public static LightItem FAIRY_LIGHT;
    public static LightItem FLOWER_LIGHT;
    public static LightItem JACK_O_LANTERN;
    public static LightItem SKULL_LIGHT;
    public static LightItem GHOST_LIGHT;
    public static LightItem SPIDER_LIGHT;
    public static LightItem WITCH_LIGHT;
    public static LightItem SNOWFLAKE_LIGHT;
    public static LightItem HEART_LIGHT;
    public static LightItem MOON_LIGHT;
    public static LightItem STAR_LIGHT;
    public static LightItem ICICLE_LIGHTS;
    public static LightItem METEOR_LIGHT;
    public static LightItem CANDLE_LANTERN_LIGHT;
    public static LightItem OIL_LANTERN_LIGHT;
    public static LightItem PAPER_LANTERN;
    public static LightItem ORB_LANTERN;
    public static LightItem OIL_LANTERN;
    public static LightItem CANDLE_LANTERN;
    public static LightItem INCANDESCENT_LIGHT;

    public static void register(final BiConsumer<ResourceLocation, Item> registry) {
        HANGING_LIGHTS = register(registry, FLRNames.HANGING_LIGHTS, new HangingLightsConnectionItem(defaultProperties(FLRNames.HANGING_LIGHTS)));
        PENNANT_BUNTING = register(registry, FLRNames.PENNANT_BUNTING, new PennantBuntingConnectionItem(defaultProperties(FLRNames.PENNANT_BUNTING)));
        TINSEL = register(registry, FLRNames.TINSEL, new TinselConnectionItem(dyeableProperties(FLRNames.TINSEL)));
        LETTER_BUNTING = register(registry, FLRNames.LETTER_BUNTING, new LetterBuntingConnectionItem(defaultProperties(FLRNames.LETTER_BUNTING)));
        GARLAND = register(registry, FLRNames.GARLAND, new ConnectionItem(defaultProperties(FLRNames.GARLAND), () -> ConnectionTypes.VINE_GARLAND));

        TRIANGLE_PENNANT = register(registry, FLRNames.TRIANGLE_PENNANT, new PennantItem(dyeableProperties(FLRNames.TRIANGLE_PENNANT)));
        SPEARHEAD_PENNANT = register(registry, FLRNames.SPEARHEAD_PENNANT, new PennantItem(dyeableProperties(FLRNames.SPEARHEAD_PENNANT)));
        SWALLOWTAIL_PENNANT = register(registry, FLRNames.SWALLOWTAIL_PENNANT, new PennantItem(dyeableProperties(FLRNames.SWALLOWTAIL_PENNANT)));
        SQUARE_PENNANT = register(registry, FLRNames.SQUARE_PENNANT, new PennantItem(dyeableProperties(FLRNames.SQUARE_PENNANT)));

        FAIRY_LIGHT = register(registry, FLRNames.FAIRY_LIGHT, new ColorLightItem(FLRBlocks.FAIRY_LIGHT, lightProperties(FLRNames.FAIRY_LIGHT)));
        PAPER_LANTERN = register(registry, FLRNames.PAPER_LANTERN, new ColorLightItem(FLRBlocks.PAPER_LANTERN, lightProperties(FLRNames.PAPER_LANTERN)));
        ORB_LANTERN = register(registry, FLRNames.ORB_LANTERN, new ColorLightItem(FLRBlocks.ORB_LANTERN, lightProperties(FLRNames.ORB_LANTERN)));
        FLOWER_LIGHT = register(registry, FLRNames.FLOWER_LIGHT, new ColorLightItem(FLRBlocks.FLOWER_LIGHT, lightProperties(FLRNames.FLOWER_LIGHT)));
        CANDLE_LANTERN_LIGHT = register(registry, FLRNames.CANDLE_LANTERN_LIGHT, new ColorLightItem(FLRBlocks.CANDLE_LANTERN_LIGHT, lightProperties(FLRNames.CANDLE_LANTERN_LIGHT)));
        OIL_LANTERN_LIGHT = register(registry, FLRNames.OIL_LANTERN_LIGHT, new ColorLightItem(FLRBlocks.OIL_LANTERN_LIGHT, lightProperties(FLRNames.OIL_LANTERN_LIGHT)));
        JACK_O_LANTERN = register(registry, FLRNames.JACK_O_LANTERN, new ColorLightItem(FLRBlocks.JACK_O_LANTERN, lightProperties(FLRNames.JACK_O_LANTERN)));
        SKULL_LIGHT = register(registry, FLRNames.SKULL_LIGHT, new ColorLightItem(FLRBlocks.SKULL_LIGHT, lightProperties(FLRNames.SKULL_LIGHT)));
        GHOST_LIGHT = register(registry, FLRNames.GHOST_LIGHT, new ColorLightItem(FLRBlocks.GHOST_LIGHT, lightProperties(FLRNames.GHOST_LIGHT)));
        SPIDER_LIGHT = register(registry, FLRNames.SPIDER_LIGHT, new ColorLightItem(FLRBlocks.SPIDER_LIGHT, lightProperties(FLRNames.SPIDER_LIGHT)));
        WITCH_LIGHT = register(registry, FLRNames.WITCH_LIGHT, new ColorLightItem(FLRBlocks.WITCH_LIGHT, lightProperties(FLRNames.WITCH_LIGHT)));
        SNOWFLAKE_LIGHT = register(registry, FLRNames.SNOWFLAKE_LIGHT, new ColorLightItem(FLRBlocks.SNOWFLAKE_LIGHT, lightProperties(FLRNames.SNOWFLAKE_LIGHT)));
        HEART_LIGHT = register(registry, FLRNames.HEART_LIGHT, new ColorLightItem(FLRBlocks.HEART_LIGHT, lightProperties(FLRNames.HEART_LIGHT)));
        MOON_LIGHT = register(registry, FLRNames.MOON_LIGHT, new ColorLightItem(FLRBlocks.MOON_LIGHT, lightProperties(FLRNames.MOON_LIGHT)));
        STAR_LIGHT = register(registry, FLRNames.STAR_LIGHT, new ColorLightItem(FLRBlocks.STAR_LIGHT, lightProperties(FLRNames.STAR_LIGHT)));
        ICICLE_LIGHTS = register(registry, FLRNames.ICICLE_LIGHTS, new ColorLightItem(FLRBlocks.ICICLE_LIGHTS, lightProperties(FLRNames.ICICLE_LIGHTS)));
        METEOR_LIGHT = register(registry, FLRNames.METEOR_LIGHT, new ColorLightItem(FLRBlocks.METEOR_LIGHT, lightProperties(FLRNames.METEOR_LIGHT)));
        OIL_LANTERN = register(registry, FLRNames.OIL_LANTERN, new ColorLightItem(FLRBlocks.OIL_LANTERN, lightProperties(FLRNames.OIL_LANTERN)));
        CANDLE_LANTERN = register(registry, FLRNames.CANDLE_LANTERN, new ColorLightItem(FLRBlocks.CANDLE_LANTERN, lightProperties(FLRNames.CANDLE_LANTERN)));
        INCANDESCENT_LIGHT = register(registry, FLRNames.INCANDESCENT_LIGHT, new ColorLightItem(FLRBlocks.INCANDESCENT_LIGHT, lightProperties(FLRNames.INCANDESCENT_LIGHT)));
    }

    private static <T extends Item> T register(final BiConsumer<ResourceLocation, Item> registry, final String name, final T item) {
        registry.accept(Constants.id(name), item);
        return item;
    }

    private static Item.Properties defaultProperties(final String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, Constants.id(name)));
    }

    private static Item.Properties dyeableProperties(final String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, Constants.id(name)))
                .component(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.DYED_COLOR, true));
    }

    private static Item.Properties lightProperties(final String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, Constants.id(name)))
                .useBlockDescriptionPrefix()
                .stacksTo(16)
                .component(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.DYED_COLOR, true));
    }

    private FLRItems() {}
}
