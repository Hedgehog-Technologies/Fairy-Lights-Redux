package org.hedgetech.fairylightsredux.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.FLRNames;
import org.hedgetech.fairylightsredux.client.model.light.*;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.*;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FLRModelLayers {
    public static final ModelLayerLocation FAIRY_LIGHT = locationMain(FLRNames.FAIRY_LIGHT);
    public static final ModelLayerLocation PAPER_LANTERN = locationMain(FLRNames.PAPER_LANTERN);
    public static final ModelLayerLocation ORB_LANTERN = locationMain(FLRNames.ORB_LANTERN);
    public static final ModelLayerLocation FLOWER_LIGHT = locationMain(FLRNames.FLOWER_LIGHT);
    public static final ModelLayerLocation CANDLE_LANTERN_LIGHT = locationMain(FLRNames.CANDLE_LANTERN_LIGHT);
    public static final ModelLayerLocation OIL_LANTERN_LIGHT = locationMain(FLRNames.OIL_LANTERN_LIGHT);
    public static final ModelLayerLocation JACK_O_LANTERN = locationMain(FLRNames.JACK_O_LANTERN);
    public static final ModelLayerLocation SKULL_LIGHT = locationMain(FLRNames.SKULL_LIGHT);
    public static final ModelLayerLocation GHOST_LIGHT = locationMain(FLRNames.GHOST_LIGHT);
    public static final ModelLayerLocation SPIDER_LIGHT = locationMain(FLRNames.SPIDER_LIGHT);
    public static final ModelLayerLocation WITCH_LIGHT = locationMain(FLRNames.WITCH_LIGHT);
    public static final ModelLayerLocation SNOWFLAKE_LIGHT = locationMain(FLRNames.SNOWFLAKE_LIGHT);
    public static final ModelLayerLocation HEART_LIGHT = locationMain(FLRNames.HEART_LIGHT);
    public static final ModelLayerLocation MOON_LIGHT = locationMain(FLRNames.MOON_LIGHT);
    public static final ModelLayerLocation STAR_LIGHT = locationMain(FLRNames.STAR_LIGHT);
    public static final ModelLayerLocation ICICLE_LIGHTS_1 = locationMain(FLRNames.ICICLE_LIGHTS + "_1");
    public static final ModelLayerLocation ICICLE_LIGHTS_2 = locationMain(FLRNames.ICICLE_LIGHTS + "_2");
    public static final ModelLayerLocation ICICLE_LIGHTS_3 = locationMain(FLRNames.ICICLE_LIGHTS + "_3");
    public static final ModelLayerLocation ICICLE_LIGHTS_4 = locationMain(FLRNames.ICICLE_LIGHTS + "_4");
    public static final ModelLayerLocation METEOR_LIGHT = locationMain(FLRNames.METEOR_LIGHT);
    public static final ModelLayerLocation OIL_LANTERN = locationMain(FLRNames.OIL_LANTERN);
    public static final ModelLayerLocation CANDLE_LANTERN = locationMain(FLRNames.CANDLE_LANTERN);
    public static final ModelLayerLocation INCANDESCENT_LIGHT = locationMain(FLRNames.INCANDESCENT_LIGHT);
    public static final ModelLayerLocation LIGHTS_WIRE = locationMain(FLRNames.LIGHTS_WIRE);
    public static final ModelLayerLocation PENNANT_WIRE = locationMain(FLRNames.PENNANT_WIRE);
    public static final ModelLayerLocation LETTER_WIRE = locationMain(FLRNames.LETTER_WIRE);
    public static final ModelLayerLocation TINSEL_WIRE = locationMain(FLRNames.TINSEL_WIRE);
    public static final ModelLayerLocation TINSEL_STRIP = locationMain(FLRNames.TINSEL_STRIP);
    public static final ModelLayerLocation VINE_WIRE = locationMain(FLRNames.VINE_WIRE);
    public static final ModelLayerLocation GARLAND_RINGS = locationMain(FLRNames.GARLAND_RINGS);
    public static final ModelLayerLocation BOW = locationMain(FLRNames.BOW);

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
