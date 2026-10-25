package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.util.Mth;

public class PaperLanternModel extends ColorLightModel {
    public PaperLanternModel(final ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();

        helper.unlit()
                .setTextureOffset(34, 18)
                .addBox(-1, -0.5F, -1, 2, 2, 2, -0.05F)
                .setTextureOffset(21, 26)
                .addBox(-1, -9.5F, -1, 2, 3, 2)
                .setTextureOffset(58, 0)
                .addBox(-0.5F, -14.5F, -0.5F, 1, 5, 1);

        for (int i = 0; i < 8; i++) {
            final boolean straight = (i & 1) == 0;
            final EasyMeshBuilder hSupport = new EasyMeshBuilder("hSupport_" + i, 28, 34);
            hSupport.addBox(0, 0, -0.5F, straight ? 4 : 5, 7, 1);
            hSupport.yRot = 45 * i * Mth.DEG_TO_RAD;
            hSupport.y = -7;
            helper.unlit().addChild(hSupport);
        }

        helper.createBulb()
                .setUV(0, 41)
                .addBox(-3.5F, -6.5F, -3.5F, 7, 6, 7, 0.0F, 0.3F);

        return helper.build();
    }
}
