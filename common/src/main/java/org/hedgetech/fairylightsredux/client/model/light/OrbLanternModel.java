package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class OrbLanternModel extends ColorLightModel {
    public OrbLanternModel(final ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();

        helper.unlit()
                .setTextureOffset(30, 6)
                .addBox(-1, -0.5F, -1, 2, 2, 2, -0.05F);
        helper.createBulb()
                .setUV(0, 27)
                .addBox(-3.5F, -7.5F, -3.5F, 7, 7, 7);

        return helper.build();
    }
}
