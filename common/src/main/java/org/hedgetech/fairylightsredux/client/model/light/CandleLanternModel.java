package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.util.Mth;
import org.hedgetech.fairylightsredux.server.feature.light.IBrightnessLightBehavior;
import org.hedgetech.fairylightsredux.server.feature.light.Light;

public class CandleLanternModel extends LightModel<IBrightnessLightBehavior> {
    public CandleLanternModel(final ModelPart root) {
        super(root);
    }

    @Override
    public void animate(final Light<?> light, final IBrightnessLightBehavior behavior, final float delta) {
        super.animate(light, behavior, delta);
        this.brightness = behavior.getBrightness(delta);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        helper.unlit()
                .setTextureOffset(21, 0)
                .addBox(-1, 0.5F, -1, 2, 1, 2, -0.05F)
                .setTextureOffset(0, 3)
                .addBox(-1.5F, -1.5F, -1.5F, 3, 2, 3)
                .setTextureOffset(43, 15)
                .addBox(-2.5F, -1.75F, -2.5F, 5, 1, 5)
                .setTextureOffset(23, 27)
                .addBox(-3, -2.5F, -3, 6, 1, 6)
                .setTextureOffset(43, 21)
                .addBox(-2.5F, -8.5F, -2.5F, 5, 1, 5);

        for (int i = 0; i < 4; i++) {
            final EasyMeshBuilder frame = new EasyMeshBuilder("frame_" + i, 4 * i + 47, 27);
            frame.addBox(-0.5F, 0, -0.5F, 1, 6, 1)
                    .setRotationPoint(2.1F * ((i & 2) == 0 ? 1 : -1), -8F, 2.1F * ((i + 1 & 2) == 0 ? 1 : -1))
                    .setRotationAngles(5 * Mth.DEG_TO_RAD, (90 * i + 45) * Mth.DEG_TO_RAD);
            helper.unlit().addChild(frame);
        }

        helper.lit()
                .setTextureOffset(63, 26)
                .addBox(-2, -7.5F, -2, 4, 5, 4)
                .setTextureOffset(79, 28)
                .addBox(-1, -7.5F, -1, 2, 2, 2)
                .setTextureOffset(81, 26)
                .addBox(-1, -5.5F, 0, 2, 2, 0);

        return helper.build();
    }
}
