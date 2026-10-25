package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class StarLightModel extends ColorLightModel {
    public StarLightModel(final ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        final int u = 101, v = 73;

        helper.unlit()
                .setTextureOffset(u + 12, v)
                .addBox(-0.5F, 5.0F - 7.0F, -0.5F, 1.0F, 3.0F, 1.0F);

        final BulbBuilder bulb = helper.createBulb()
                .setPosition(0.0F, 1.0F - 7.0F, 0.0F);
        bulb.createChild("center", u, v)
                .setAngles(0.0F, 0.0F, -0.7854F)
                .addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, 0.15F);
        bulb.setUV(u, v + 4)
                .addBox(-1.0F, 1.07F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F);
        bulb.createChild("b1", u, v)
                .setAngles(0.0F, 0.0F, -1.2566F)
                .setUV(u, v + 4)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, 0.02F);
        bulb.createChild("b2", u, v)
                .setAngles(0.0F, 0.0F, -2.5133F)
                .setUV(u, v + 4)
                .addBox(-1.0F, 1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F);
        bulb.createChild("b3", u, v)
                .setAngles(0.0F, 0.0F, 2.5133F)
                .setUV(u, v + 4)
                .addBox(-1.0F, 1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.02F);
        bulb.createChild("b4", u, v)
                .setAngles(0.0F, 0.0F, 1.2566F)
                .setUV(u, v + 4)
                .addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, -0.02F);

        return helper.build();
    }
}
