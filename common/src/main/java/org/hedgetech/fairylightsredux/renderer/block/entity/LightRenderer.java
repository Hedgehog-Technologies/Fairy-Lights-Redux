package org.hedgetech.fairylightsredux.renderer.block.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.hedgetech.fairylightsredux.client.model.light.LightModel;
import org.hedgetech.fairylightsredux.feature.light.Light;
import org.hedgetech.fairylightsredux.feature.light.LightBehavior;
import org.hedgetech.fairylightsredux.item.LightVariant;
import org.hedgetech.fairylightsredux.item.SimpleLightVariant;
import org.hedgetech.fairylightsredux.network.NetworkClientProxy;
import org.hedgetech.fairylightsredux.util.FLRMth;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LightRenderer {
    static class DefaultModel extends LightModel<LightBehavior> {
        private static final ModelPart EMPTY = new ModelPart(List.of(), Map.of());

        public DefaultModel() {
            super(new ModelPart(List.of(), Map.of(
                    "lit", EMPTY,
                    "lit_tint", EMPTY,
                    "lit_tint_glow", EMPTY,
                    "unlist", EMPTY
            )));
        }

        @Override
        public void renderToBuffer(final PoseStack matrix, final VertexConsumer builder, final int light, final int overlay, final float r, final float g, final float b, final float a) {}
    }

    private final LightModelProvider<LightBehavior> defaultLight = LightModelProvider.of(new DefaultModel());
    private final Map<LightVariant<?>, LightModelProvider<?>> lights;

    public LightRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        lights = new ImmutableMap.Builder<LightVariant<?>, LightModelProvider<?>>()
                .put(SimpleLightVariant.FAIRY_LIGHT, LightModelProvider.of(new FairyLightModel(baker.apply(FLRModelLayers.FAIRY_LIGHT))))
                .put(SimpleLightVariant.PAPER_LANTERN, LightModelProvider.of(new PaperLanternModel(baker.apply(FLRModelLayers.PAPER_LANTERN))))
                .put(SimpleLightVariant.ORB_LANTERN, LightModelProvider.of(new OrbLanternModel(baker.apply(FLRModelLayers.ORB_LANTERN))))
                .put(SimpleLightVariant.FLOWER_LIGHT, LightModelProvider.of(new FlowerLightModel(baker.apply(FLRModelLayers.FLOWER_LIGHT))))
                .put(SimpleLightVariant.CANDLE_LANTERN_LIGHT, LightModelProvider.of(new ColorCandleLanternModel(baker.apply(FLRModelLayers.CANDLE_LANTERN_LIGHT))))
                .put(SimpleLightVariant.OIL_LANTERN_LIGHT, LightModelProvider.of(new ColorOilLanternModel(baker.apply(FLRModelLayers.OIL_LANTERN_LIGHT))))
                .put(SimpleLightVariant.JACK_O_LANTERN, LightModelProvider.of(new JackOLanternLightModel(baker.apply(FLRModelLayers.JACK_O_LANTERN))))
                .put(SimpleLightVariant.SKULL_LIGHT, LightModelProvider.of(new SkullLightModel(baker.apply(FLRModelLayers.SKULL_LIGHT))))
                .put(SimpleLightVariant.GHOST_LIGHT, LightModelProvider.of(new GhostLightModel(baker.apply(FLRModelLayers.GHOST_LIGHT))))
                .put(SimpleLightVariant.SPIDER_LIGHT, LightModelProvider.of(new SpiderLightModel(baker.apply(FLRModelLayers.SPIDER_LIGHT))))
                .put(SimpleLightVariant.WITCH_LIGHT, LightModelProvider.of(new WitchLightModel(baker.apply(FLRModelLayers.WITCH_LIGHT))))
                .put(SimpleLightVariant.SNOWFLAKE_LIGHT, LightModelProvider.of(new SnowflakeLightModel(baker.apply(FLRModelLayers.SNOWFLAKE_LIGHT))))
                .put(SimpleLightVariant.HEART_LIGHT, LightModelProvider.of(new HeartLightModel(baker.apply(FLRModelLayers.HEART_LIGHT))))
                .put(SimpleLightVariant.MOON_LIGHT, LightModelProvider.of(new MoonLightModel(baker.apply(FLRModelLayers.MOON_LIGHT))))
                .put(SimpleLightVariant.STAR_LIGHT, LightModelProvider.of(new StarLightModel(baker.apply(FLRModelLayers.STAR_LIGHT))))
                .put(SimpleLightVariant.ICICLE_LIGHTS, LightModelProvider.of(
                        new IcicleLightsModel[] {
                                new IcicleLightsModel(baker.apply(FLRModelLayers.ICICLE_LIGHTS_1), 1),
                                new IcicleLightsModel(baker.apply(FLRModelLayers.ICICLE_LIGHTS_2), 2),
                                new IcicleLightsModel(baker.apply(FLRModelLayers.ICICLE_LIGHTS_3), 3),
                                new IcicleLightsModel(baker.apply(FLRModelLayers.ICICLE_LIGHTS_4), 4)
                        },
                        (models, i) -> models[i < 0 ? 3 : FLRMth.mod(FLRMth.hash(i), 4)]
                ))
                .put(SimpleLightVariant.METEOR_LIGHT, LightModelProvider.of(new MeteorLightModel(baker.apply(FLRModelLayers.METEOR_LIGHT))))
                .put(SimpleLightVariant.OIL_LANTERN, LightModelProvider.of(new OilLanternModel(baker.apply(FLRModelLayers.OIL_LANTERN))))
                .put(SimpleLightVariant.CANDLE_LANTERN, LightModelProvider.of(new CandleLanternModel(baker.apply(FLRModelLayers.CANDLE_LANTERN))))
                .put(SimpleLightVariant.INCANDESCENT_LIGHT, LightModelProvider.of(new IncandescentLightModel(baker.apply(FLRModelLayers.INCANDESCENT_LIGHT))))
                .build();
    }

    public Data start(final MultiBufferSource source) {
        // FIXME - Unsure about the RenderType here
        final VertexConsumer buf = NetworkClientProxy.TRANSLUCENT_TEXTURE.buffer(source, RenderType::entityTranslucent);
        ForwardingVertexConsumer translucent = new ForwardingVertexConsumer() {
            @Override
            protected VertexConsumer delegate() {
                return buf;
            }

            @Override
            public VertexConsumer normal(float x, float y, float z) {
                return super.normal(0.0F, 1.0F, 0.0F);
            }
        };
        return new Data(buf, translucent);
    }

    public <T extends LightBehavior> LightModel<T> getModel(final Light<?> light, final int index) {
        return this.getModel(light.getVariant(), index);
    }

    public <T extends LightBehavior> LightModel<T> getModel(final LightVariant<?> variant, final int index) {
        return (LightModel<T>) this.lights.getOrDefault(variant, this.defaultLight).get(index);
    }

    public void render(final PoseStack matrix, final Data data, final Light<?> light, final int index, final float delta, final int packedLight, final int packedOverlay) {
        this.render(matrix, data, light, this.getModel(light, index), delta, packedLight, packedOverlay);
    }

    public <T extends LightBehavior> void render(final PoseStack matrix, final Data data, final Light<T> light, final LightModel<T> model, final float delta, final int packedLight, final int packedOverlay) {
        model.animate(light, light.getBehavior(), delta);
        model.renderToBuffer(matrix, data.solid, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        model.renderTranslucent(matrix, data.translucent, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    interface LightModelProvider<T extends LightBehavior> {
        LightModel<T> get(final int index);

        static <T extends LightBehavior> LightModelProvider<T> of(final LightModel<T> model) {
            return i -> model;
        }

        static <T extends LightBehavior> LightModelProvider<T> of(final Supplier<LightModel<T>> model) {
            return i -> model.get();
        }

        static <T extends LightBehavior, D> LightModelProvider<T> of(final D data, final BiFunction<? super D, Integer, LightModel<T>> function) {
            return i -> function.apply(data, i);
        }
    }

    public static class Data {
        final VertexConsumer solid;
        final VertexConsumer translucent;

        Data(final VertexConsumer solid, final VertexConsumer translucent) {
            this.solid = solid;
            this.translucent = translucent;
        }
    }
}
