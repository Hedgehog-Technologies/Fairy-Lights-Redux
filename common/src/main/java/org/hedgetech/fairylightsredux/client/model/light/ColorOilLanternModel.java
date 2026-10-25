package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.hedgetech.fairylightsredux.util.FLRMth;

public class ColorOilLanternModel extends ColorLightModel {
    public ColorOilLanternModel(final ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        helper.unlit().yRot = -FLRMth.PI / 2.0F;
        helper.unlit().setTextureOffset(10, 6)
                .addBox(-1, -0.5F, -1, 2, 2, 2, -0.05F)
                .setTextureOffset(0, 8)
                .addBox(-1.5F, -1, -1.5F, 3, 1, 3)
                .setTextureOffset(16, 18)
                .addBox(-3, -10.5F, -3, 6, 2, 6)
                .setTextureOffset(0, 12)
                .addBox(-1.5F, -9.5F, -1.5F, 3, 2, 3)
                .setTextureOffset(38, 7)
                .addBox(-0.5F, -9, 2.5F, 1, 9, 1)
                .setTextureOffset(38, 0)
                .addBox(-0.5F, -0.5F, -3, 1, 1, 6);

        helper.createBulb()
                .setUV(0, 17)
                .addBox(-2, -7.5F, -2, 4, 6, 4)
                .setUV(6, 0)
                .addBox(-1, -1.5F, -1, 2, 1, 2);

        return helper.build();
    }
}
