package org.hedgetech.fairylightsredux.client.model.light;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import org.hedgetech.fairylightsredux.server.feature.light.IBrightnessLightBehavior;
import org.hedgetech.fairylightsredux.server.feature.light.Light;

import java.util.Set;

public class IncandescentLightModel extends LightModel<IBrightnessLightBehavior> {
    private final ModelPart bulb;
    private final ModelPart filament;

    public IncandescentLightModel(final ModelPart root) {
        super(root);
        this.bulb = root.getChild("bulb");
        this.filament = root.getChild("filament");
    }

    @Override
    public void animate(final Light<?> light, final IBrightnessLightBehavior behavior, final float delta) {
        super.animate(light, behavior, delta);
        this.brightness = behavior.getBrightness(delta);
    }

    @Override
    protected int getLight(final int packedLight) {
        return (int) Math.max((this.brightness * 15.0F * 16.0F), packedLight & 255) | packedLight & (255 << 16);
    }

    @Override
    public void renderToBuffer(final PoseStack matrix, final VertexConsumer builder, final int light, final int overlay, final float r, final float g, final float b, final float a) {
        super.renderToBuffer(matrix, builder, light, overlay, r, g, b, a);
        final int emissiveLight = this.getLight(light);
        final float cr = 0.23F, cg = 0.18F, cb = 0.14F;
        final float br = this.brightness;
        this.filament.render(matrix, builder, emissiveLight, overlay, ARGB.colorFromFloat(a, r * (cr * (1.0F - br) + br), g * (cg * (1.0F - br) + br), b * (cb * (1.0F - br) + br)));
    }

    @Override
    public void renderTranslucent(final PoseStack matrix, final VertexConsumer builder, final int light, final int overlay, final float r, final float g, final float b, final float a) {
        final float bi = this.brightness;
        final int emissiveLight = this.getLight(light);
        final float br = 1.0F, bg = 0.73F, bb = 0.3F;
        this.bulb.render(matrix, builder, emissiveLight, overlay, ARGB.colorFromFloat(bi * 0.4F + 0.25F, r * (br * bi + (1.0F - bi)), g * (bg * bi + (1.0F - bi)), b * (bb * bi + (1.0F - bi))));
        super.renderTranslucent(matrix, builder, light, overlay, r, g, b, a);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        helper.unlit()
                .setTextureOffset(90, 10)
                .addBox(-1.0F, -0.01F, -1.0F, 2.0F, 1.0F, 2.0F);

        final EasyMeshBuilder bulb = new EasyMeshBuilder("bulb", 98, 10)
                .addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F);
        helper.extra().add(bulb);

        final EasyMeshBuilder filament = new EasyMeshBuilder("filament", 90, 13)
                .addBox(-1.0F, -3.0F, 0.0F, 2.0F, 3.0F, 0.0F, Set.of(Direction.NORTH));
        helper.extra().add(filament);

        return helper.build();
    }
}
