package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class FairyLightModel extends ColorLightModel {
    public FairyLightModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        helper.createBulb()
                .setUV(46, 0)
                .addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F);
        return helper.build();
    }
}
