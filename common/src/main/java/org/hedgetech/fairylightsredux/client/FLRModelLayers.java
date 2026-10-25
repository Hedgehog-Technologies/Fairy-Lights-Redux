package org.hedgetech.fairylightsredux.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.client.model.light.*;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FLRModelLayers {
    public static final ModelLayerLocation FAIRY_LIGHT = locationMain("fairy_light");
    public static final ModelLayerLocation PAPER_LANTERN = locationMain("paper_lantern");
    public static final ModelLayerLocation ORB_LANTERN = locationMain("orb_lantern");
    public static final ModelLayerLocation FLOWER_LIGHT = locationMain("flower_light");
    public static final ModelLayerLocation CANDLE_LANTERN_LIGHT = locationMain("candle_lantern_light");
    public static final ModelLayerLocation OIL_LANTERN_LIGHT = locationMain("oil_lantern_light");
    public static final ModelLayerLocation JACK_O_LANTERN = locationMain("jack_o_lantern");
    public static final ModelLayerLocation SKULL_LIGHT = locationMain("skull_light");
    public static final ModelLayerLocation GHOST_LIGHT = locationMain("ghost_light");
    public static final ModelLayerLocation SPIDER_LIGHT = locationMain("spider_light");
    public static final ModelLayerLocation WITCH_LIGHT = locationMain("witch_light");
    public static final ModelLayerLocation SNOWFLAKE_LIGHT = locationMain("snowflake_light");
    public static final ModelLayerLocation HEART_LIGHT = locationMain("heart_light");
    public static final ModelLayerLocation MOON_LIGHT = locationMain("moon_light");
    public static final ModelLayerLocation STAR_LIGHT = locationMain("star_light");
    public static final ModelLayerLocation ICICLE_LIGHTS_1 = locationMain("icicle_lights_1");
    public static final ModelLayerLocation ICICLE_LIGHTS_2 = locationMain("icicle_lights_2");
    public static final ModelLayerLocation ICICLE_LIGHTS_3 = locationMain("icicle_lights_3");
    public static final ModelLayerLocation ICICLE_LIGHTS_4 = locationMain("icicle_lights_4");
    public static final ModelLayerLocation METEOR_LIGHT = locationMain("meteor_light");
    public static final ModelLayerLocation OIL_LANTERN = locationMain("oil_lantern");
    public static final ModelLayerLocation CANDLE_LANTERN = locationMain("candle_lantern");
    public static final ModelLayerLocation INCANDESCENT_LIGHT = locationMain("incandescent_light");
    public static final ModelLayerLocation LIGHTS_WIRE = locationMain("lights_wire");
    public static final ModelLayerLocation PENNANT_WIRE = locationMain("pennant_wire");
    public static final ModelLayerLocation LETTER_WIRE = locationMain("letter_wire");
    public static final ModelLayerLocation TINSEL_WIRE = locationMain("tinsel_wire");
    public static final ModelLayerLocation TINSEL_STRIP = locationMain("tinsel_strip");
    public static final ModelLayerLocation VINE_WIRE = locationMain("vine_wire");
    public static final ModelLayerLocation GARLAND_RINGS = locationMain("garland_rings");
    public static final ModelLayerLocation BOW = locationMain("bow");

    public static void register(final BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> registry) {
        registry.accept(FAIRY_LIGHT, FairyLightModel::createLayer);
        registry.accept(PAPER_LANTERN, PaperLanternModel::createLayer);
        registry.accept(ORB_LANTERN, OrbLanternModel::createLayer);
        registry.accept(FLOWER_LIGHT, FlowerLightModel::createLayer);
        registry.accept(CANDLE_LANTERN_LIGHT, ColorCandleLanternModel::createLayer);
        registry.accept(OIL_LANTERN_LIGHT, ColorOilLanternModel::createLayer);
        registry.accept(JACK_O_LANTERN, JackOLanternLightModel::createLayer);
        registry.accept(SKULL_LIGHT, SkullLightModel::createLayer);
        registry.accept(GHOST_LIGHT, GhostLightModel::createLayer);
        registry.accept(SPIDER_LIGHT, SpiderLightModel::createLayer);
        registry.accept(WITCH_LIGHT, WitchLightModel::createLayer);
        registry.accept(SNOWFLAKE_LIGHT, SnowflakeLightModel::createLayer);
        registry.accept(HEART_LIGHT, HeartLightModel::createLayer);
        registry.accept(MOON_LIGHT, MoonLightModel::createLayer);
        registry.accept(STAR_LIGHT, StarLightModel::createLayer);
        registry.accept(ICICLE_LIGHTS_1, () -> IcicleLightsModel.createLayer(1));
        registry.accept(ICICLE_LIGHTS_2, () -> IcicleLightsModel.createLayer(2));
        registry.accept(ICICLE_LIGHTS_3, () -> IcicleLightsModel.createLayer(3));
        registry.accept(ICICLE_LIGHTS_4, () -> IcicleLightsModel.createLayer(4));
        registry.accept(METEOR_LIGHT, MeteorLightModel::createLayer);
        registry.accept(OIL_LANTERN, OilLanternModel::createLayer);
        registry.accept(CANDLE_LANTERN, CandleLanternModel::createLayer);
        registry.accept(INCANDESCENT_LIGHT, IncandescentLightModel::createLayer);
        registry.accept(LIGHTS_WIRE, HangingLightsRenderer::wireLayer);
        registry.accept(PENNANT_WIRE, PennantBuntingRenderer::wireLayer);
        registry.accept(LETTER_WIRE, LetterBuntingRenderer::wireLayer);
        registry.accept(TINSEL_WIRE, GarlandTinselRenderer::wireLayer);
        registry.accept(TINSEL_STRIP, GarlandTinselRenderer::stripLayer);
        registry.accept(VINE_WIRE, GarlandVineRenderer::wireLayer);
        registry.accept(GARLAND_RINGS, GarlandVineRenderer::ringsLayer);
        registry.accept(BOW, FastenerRenderer::bowLayer);
    }

    private static ModelLayerLocation locationMain(String name) {
        return new ModelLayerLocation(Constants.id(name), "main");
    }
}
