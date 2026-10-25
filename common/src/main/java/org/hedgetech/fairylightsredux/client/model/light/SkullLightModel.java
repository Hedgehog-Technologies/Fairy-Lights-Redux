package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.hedgetech.fairylightsredux.util.FLRMth;

public class SkullLightModel extends ColorLightModel {
    public SkullLightModel(final ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        final EasyMeshBuilder apertures = new EasyMeshBuilder("apertures", 12, 10)
                .setRotationPoint(0, -3, -2.75F)
                .addBox(-1.5F, -1, 0, 3, 2, 0, 0)
                .setRotationAngles(FLRMth.PI, FLRMth.PI);
        helper.unlit().addChild(apertures);

        final BulbBuilder bulb = helper.createBulb();
        bulb.createChild("skull", 0, 54)
                .addBox(-2.5F, 0, -2.5F, 5, 4, 5, 0)
                .setAngles(FLRMth.PI, 0, 0);
        bulb.createChild("mandible", 40, 34)
                .setPosition(0, -3.5F, 0.3F)
                .addBox(-2.5F, 0, -3, 5, 2, 3, -0.25F, 0.6F)
                .setAngles(FLRMth.PI / 16 + FLRMth.PI, FLRMth.PI, 0);
        bulb.createChild("maxilla", 46, 7)
                .setPosition(0, -3.875F, -2.125F)
                .setAngles(FLRMth.PI, 0, 0)
                .addBox(-1, 0, -0.5F, 2, 1, 1, -0.125F, 0.6F);

        final EasyMeshBuilder chain = new EasyMeshBuilder("chain", 34, 18)
                .setRotationPoint(0, 2, 0)
                .addBox(-1, 0, -1, 2, 2, 2, -0.05F);
        chain.xRot = FLRMth.PI;
        helper.lit().addChild(chain);

        return helper.build();
    }
}
