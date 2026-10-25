package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.hedgetech.fairylightsredux.server.feature.light.IBrightnessLightBehavior;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.hedgetech.fairylightsredux.util.FLRMth;

public class OilLanternModel extends LightModel<IBrightnessLightBehavior> {
    public OilLanternModel(final ModelPart root) {
        super(root);
    }

    @Override
    public void animate(final Light<?> light, final IBrightnessLightBehavior behavior, final float delta) {
        super.animate(light, behavior, delta);
        this.brightness = behavior.getBrightness(delta);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();

        helper.unlit().yRot = -FLRMth.PI / 2.0F;
        helper.unlit()
                .setTextureOffset(10, 6)
                .addBox(-1, -0.5F, -1, 2, 2, 2, -0.05F)
                .setTextureOffset(0, 8)
                .addBox(-1.5F, -1, -1.5F, 3, 1, 3)
                .setTextureOffset(16, 18)
                .addBox(-3, 10.5F, -3, 6, 2, 6)
                .setTextureOffset(0, 12)
                .addBox(-1.5F, -9.5F, -1.5F, 3, 2, 3)
                .setTextureOffset(38, 7)
                .addBox(-0.5F, -9, -3.5F, 1, 9, 1)
                .setTextureOffset(42, 7)
                .addBox(-0.5F, -9, 2.5F, 1, 9, 1)
                .setTextureOffset(38, 0)
                .addBox(-0.5F, -0.5F, -3, 1, 1, 6);
        helper.lit()
                .setTextureOffset(63, 16)
                .addBox(-2, -7.5F, -2, 4, 6, 4)
                .setTextureOffset(79, 17)
                .addBox(-1, -1.5F, -1, 2, 1, 2)
                .setTextureOffset(79, 20)
                .addBox(-1.0F, -7.5F, -1.0F, 2.0F, 4.0F, 2.0F);

        return helper.build();
    }
}
