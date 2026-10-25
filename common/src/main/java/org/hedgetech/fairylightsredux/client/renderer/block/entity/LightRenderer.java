package org.hedgetech.fairylightsredux.client.renderer.block.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.client.FLRModelLayers;
import org.hedgetech.fairylightsredux.client.model.light.*;
import org.hedgetech.fairylightsredux.client.renderer.UnsortedRenderType;
import org.hedgetech.fairylightsredux.server.feature.light.ILightBehavior;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.hedgetech.fairylightsredux.server.item.LightVariant;
import org.hedgetech.fairylightsredux.util.FLRMth;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class LightRenderer {
    private static final ResourceLocation TEXTURE = Constants.id("textures/entity/connections.png");

    static class DefaultModel extends LightModel<ILightBehavior> {
        private static final ModelPart EMPTY = new ModelPart(List.of(), Map.of());

        public DefaultModel() {
            super(new ModelPart(List.of(), Map.of(
                    "lit", EMPTY,
                    "lit_tint", EMPTY,
                    "lit_tint_glow", EMPTY,
                    "unlit", EMPTY
            )));
        }

        @Override
        public void renderToBuffer(final PoseStack matrix, final VertexConsumer builder, final int light, final int overlay, final float r, final float g, final float b, final float a) {}
    }

    private final ILightModelProvider<ILightBehavior> defaultLight = ILightModelProvider.of(new DefaultModel());
    private final Map<LightVariant<?>, ILightModelProvider<?>> lights;

    public LightRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        this.lights = new ImmutableMap.Builder<LightVariant<?>, ILightModelProvider<?>>()
                .put(LightVariant.FAIRY_LIGHT, ILightModelProvider.of(new FairyLightModel(baker.apply(FLRModelLayers.FAIRY_LIGHT))))
                .put(LightVariant.PAPER_LANTERN, ILightModelProvider.of(new PaperLanternModel(baker.apply(FLRModelLayers.PAPER_LANTERN))))
                .put(LightVariant.ORB_LANTERN, ILightModelProvider.of(new OrbLanternModel(baker.apply(FLRModelLayers.ORB_LANTERN))))
                .put(LightVariant.FLOWER_LIGHT, ILightModelProvider.of(new FlowerLightModel(baker.apply(FLRModelLayers.FLOWER_LIGHT))))
                .put(LightVariant.CANDLE_LANTERN_LIGHT, ILightModelProvider.of(new ColorCandleLanternModel(baker.apply(FLRModelLayers.CANDLE_LANTERN_LIGHT))))
                .put(LightVariant.OIL_LANTERN_LIGHT, ILightModelProvider.of(new ColorOilLanternModel(baker.apply(FLRModelLayers.OIL_LANTERN_LIGHT))))
                .put(LightVariant.JACK_O_LANTERN, ILightModelProvider.of(new JackOLanternLightModel(baker.apply(FLRModelLayers.JACK_O_LANTERN))))
                .put(LightVariant.SKULL_LIGHT, ILightModelProvider.of(new SkullLightModel(baker.apply(FLRModelLayers.SKULL_LIGHT))))
                .put(LightVariant.GHOST_LIGHT, ILightModelProvider.of(new GhostLightModel(baker.apply(FLRModelLayers.GHOST_LIGHT))))
                .put(LightVariant.SPIDER_LIGHT, ILightModelProvider.of(new SpiderLightModel(baker.apply(FLRModelLayers.SPIDER_LIGHT))))
                .put(LightVariant.WITCH_LIGHT, ILightModelProvider.of(new WitchLightModel(baker.apply(FLRModelLayers.WITCH_LIGHT))))
                .put(LightVariant.SNOWFLAKE_LIGHT, ILightModelProvider.of(new SnowflakeLightModel(baker.apply(FLRModelLayers.SNOWFLAKE_LIGHT))))
                .put(LightVariant.HEART_LIGHT, ILightModelProvider.of(new HeartLightModel(baker.apply(FLRModelLayers.HEART_LIGHT))))
                .put(LightVariant.MOON_LIGHT, ILightModelProvider.of(new MoonLightModel(baker.apply(FLRModelLayers.MOON_LIGHT))))
                .put(LightVariant.STAR_LIGHT, ILightModelProvider.of(new StarLightModel(baker.apply(FLRModelLayers.STAR_LIGHT))))
                .put(LightVariant.ICICLE_LIGHTS, ILightModelProvider.of(
                        new IcicleLightsModel[] {
                                new IcicleLightsModel(baker.apply(FLRModelLayers.ICICLE_LIGHTS_1), 1),
                                new IcicleLightsModel(baker.apply(FLRModelLayers.ICICLE_LIGHTS_2), 2),
                                new IcicleLightsModel(baker.apply(FLRModelLayers.ICICLE_LIGHTS_3), 3),
                                new IcicleLightsModel(baker.apply(FLRModelLayers.ICICLE_LIGHTS_4), 4)
                        },
                        (models, i) -> models[i < 0 ? 3 : Math.floorMod(FLRMth.hash(i), 4)]
                ))
                .put(LightVariant.METEOR_LIGHT, ILightModelProvider.of(new MeteorLightModel(baker.apply(FLRModelLayers.METEOR_LIGHT))))
                .put(LightVariant.OIL_LANTERN, ILightModelProvider.of(new OilLanternModel(baker.apply(FLRModelLayers.OIL_LANTERN))))
                .put(LightVariant.INCANDESCENT_LIGHT, ILightModelProvider.of(new IncandescentLightModel(baker.apply(FLRModelLayers.INCANDESCENT_LIGHT))))
                .build();
    }

    public Data start(final MultiBufferSource source) {
        final VertexConsumer buf = source.getBuffer(UnsortedRenderType.entityTranslucent(TEXTURE));
        ForwardingVertexConsumer translucent = new ForwardingVertexConsumer() {
            @Override
            protected VertexConsumer delegate() {
                return buf;
            }

            @Override
            public @NotNull VertexConsumer setNormal(float x, float y, float z) {
                return super.setNormal(0.0F, 1.0F, 0.0F);
            }
        };

        return new Data(buf, translucent);
    }

    public <T extends ILightBehavior> LightModel<T> getModel(final Light<?> light, final int index) {
        return this.getModel(light.getVariant(), index);
    }

    @SuppressWarnings("unchecked")
    public <T extends ILightBehavior> LightModel<T> getModel(final LightVariant<?> variant, final int index) {
        return (LightModel<T>) this.lights.getOrDefault(variant, this.defaultLight).get(index);
    }

    public void render(final PoseStack matrix, final Data data, final Light<?> light, final int index, final float delta, final int packedLight, final int packedOverlay) {
        this.render(matrix, data, light, this.getModel(light, index), delta, packedLight, packedOverlay);
    }

    public <T extends ILightBehavior> void render(final PoseStack matrix, final Data data, final Light<T> light, final LightModel<T> model, final float delta, final int packedLight, final int packedOverlay) {
        model.animate(light, light.getBehavior(), delta);
        model.renderToBuffer(matrix, data.solid, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        model.renderTranslucent(matrix, data.translucent, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    interface ILightModelProvider<T extends ILightBehavior> {
        LightModel<T> get(final int index);

        static <T extends ILightBehavior> ILightModelProvider<T> of(final LightModel<T> model) {
            return i -> model;
        }

        static <T extends ILightBehavior> ILightModelProvider<T> of(final Supplier<LightModel<T>> model) {
            return i -> model.get();
        }

        static <T extends ILightBehavior, D> ILightModelProvider<T> of(final D data, final BiFunction<? super D, Integer, LightModel<T>> function) {
            return i -> function.apply(data, i);
        }
    }

    public record Data(VertexConsumer solid, VertexConsumer translucent) {}
}
