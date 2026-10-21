package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.hedgetech.fairylightsredux.server.connection.HangingLightsConnection;

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
        HANGING_LIGHTS = register(registry, "hanging_lights", new HangingLightsConnectionItem(defaultProperties("hanging_lights")));
        PENNANT_BUNTING = register(registry, "pennant_bunting", new PennantBuntingConnectionItem(defaultProperties("pennant_bunting")));
        TINSEL = register(registry, "tinsel", new TinselConnectionItem(dyeableProperties("tinsel")));
        LETTER_BUNTING = register(registry, "letter_bunting", new LetterBuntingConnectionItem(defaultProperties("letter_bunting")));
        GARLAND = register(registry, "garland", new ConnectionItem(defaultProperties("garland"), () -> ConnectionTypes.VINE_GARLAND));

        TRIANGLE_PENNANT = register(registry, "triangle_pennant", new PennantItem(dyeableProperties("triangle_pennant")));
        SPEARHEAD_PENNANT = register(registry, "spearhead_pennant", new PennantItem(dyeableProperties("spearhead_pennant")));
        SWALLOWTAIL_PENNANT = register(registry, "swallowtail_pennant", new PennantItem(dyeableProperties("swallowtail_pennant")));
        SQUARE_PENNANT = register(registry, "square_pennant", new PennantItem(dyeableProperties("square_pennant")));

        FAIRY_LIGHT = register(registry, "fairy_light", new ColorLightItem(FLRBlocks.FAIRY_LIGHT, lightProperties("fairy_light")));
        PAPER_LANTERN = register(registry, "paper_lantern", new ColorLightItem(FLRBlocks.PAPER_LANTERN, lightProperties("paper_lantern")));
        ORB_LANTERN = register(registry, "orb_lantern", new ColorLightItem(FLRBlocks.ORB_LANTERN, lightProperties("orb_lantern")));
        FLOWER_LIGHT = register(registry, "flower_light", new ColorLightItem(FLRBlocks.FLOWER_LIGHT, lightProperties("flower_light")));
        CANDLE_LANTERN_LIGHT = register(registry, "candle_lantern_light", new ColorLightItem(FLRBlocks.CANDLE_LANTERN_LIGHT, lightProperties("candle_lantern_light")));
        OIL_LANTERN_LIGHT = register(registry, "oil_lantern_light", new ColorLightItem(FLRBlocks.OIL_LANTERN_LIGHT, lightProperties("oil_lantern_light")));
        JACK_O_LANTERN = register(registry, "jack_o_lantern", new ColorLightItem(FLRBlocks.JACK_O_LANTERN, lightProperties("jack_o_lantern")));
        SKULL_LIGHT = register(registry, "skull_light", new ColorLightItem(FLRBlocks.SKULL_LIGHT, lightProperties("skull_light")));
        GHOST_LIGHT = register(registry, "ghost_light", new ColorLightItem(FLRBlocks.GHOST_LIGHT, lightProperties("ghost_light")));
        SPIDER_LIGHT = register(registry, "spider_light", new ColorLightItem(FLRBlocks.SPIDER_LIGHT, lightProperties("spider_light")));
        WITCH_LIGHT = register(registry, "witch_light", new ColorLightItem(FLRBlocks.WITCH_LIGHT, lightProperties("witch_light")));
        SNOWFLAKE_LIGHT = register(registry, "snowflake_light", new ColorLightItem(FLRBlocks.SNOWFLAKE_LIGHT, lightProperties("snowflake_light")));
        HEART_LIGHT = register(registry, "heart_light", new ColorLightItem(FLRBlocks.HEART_LIGHT, lightProperties("heart_light")));
        MOON_LIGHT = register(registry, "moon_light", new ColorLightItem(FLRBlocks.MOON_LIGHT, lightProperties("moon_light")));
        STAR_LIGHT = register(registry, "star_light", new ColorLightItem(FLRBlocks.STAR_LIGHT, lightProperties("star_light")));
        ICICLE_LIGHTS = register(registry, "icicle_lights", new ColorLightItem(FLRBlocks.ICICLE_LIGHTS, lightProperties("icicle_lights")));
        METEOR_LIGHT = register(registry, "meteor_light", new ColorLightItem(FLRBlocks.METEOR_LIGHT, lightProperties("meteor_lights")));
        OIL_LANTERN = register(registry, "oil_lantern", new ColorLightItem(FLRBlocks.OIL_LANTERN, lightProperties("oil_lantern")));
        CANDLE_LANTERN = register(registry, "candle_lantern", new ColorLightItem(FLRBlocks.CANDLE_LANTERN, lightProperties("candle_lantern")));
        INCANDESCENT_LIGHT = register(registry, "incandescent_light", new ColorLightItem(FLRBlocks.INCANDESCENT_LIGHT, lightProperties("incandescent_light")));
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
