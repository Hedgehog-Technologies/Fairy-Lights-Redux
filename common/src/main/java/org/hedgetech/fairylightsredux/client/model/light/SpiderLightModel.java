package org.hedgetech.fairylightsredux.client.model.light;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.hedgetech.fairylightsredux.util.FLRMth;

public class SpiderLightModel extends ColorLightModel {
    public SpiderLightModel(final ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        final EasyMeshBuilder string = new EasyMeshBuilder("string", 30, 6)
                .addBox(-1, 0, -1, 2, 2, 2, -0.05F);
        helper.unlit().addChild(string);

        final BulbBuilder bulb = helper.createBulb()
                .setAngles(0.0F, -FLRMth.PI / 2.0F, 0.0F);
        final BulbBuilder abdomen = bulb.createChild("abdomen", 20, 54)
                .addBox(-2.5F, -5.0F, -2.5F, 5, 5, 5, 0);
        final BulbBuilder pedicel = abdomen.createChild("pedicel", 6, 0)
                .setPosition(0, -6.0F, 0)
                .addBox(-1, 0, -1, 2, 1, 2, 0);
        final BulbBuilder cephalothorax = pedicel.createChild("cephalothorax", 40, 57)
                .setPosition(0, -2.25F, 0)
                .addBox(-2, 0, -2, 4, 3, 4, 0);
        final BulbBuilder cheliceraLeft = cephalothorax.createChild("cheliceraLeft", 0, 0)
                .setPosition(0, 0.3F, 0.6F)
                .addBox(-1, -1.5F, 0, 2, 2, 1, 0)
                .setAngles(-0.2617993877991494F, 0, 0);
        final BulbBuilder cheliceraRight = cephalothorax.createChild("cheliceraRight", 0, 0)
                .setPosition(0, 0.3F, -0.6F)
                .addBox(-1, -1.5F, -1, 2, 2, 1, 0)
                .setAngles(0.2617993877991494F, 0, 0);

        createLegs(helper, pedicel, 0);
        createLegs(helper, pedicel, 1);

        return helper.build();
    }

    private static void createLegs(final LightMeshHelper helper, final BulbBuilder bulb, final int side) {
        final BulbBuilder legs = bulb.createChild("legs_" + side, 0, 0)
                .setAngles(0.0F, FLRMth.PI * side, 0.0F);

        legs.createChild("leg1_" + side, 21, 45)
                .setPosition(0, 0.6F, 1.1F)
                .addBox(0, -0.5F, -0.5F, 5, 1, 1, 0)
                .setAngles(-0.13962634015954636F, -0.7853981633974483F, 1.5707963267948966F)
                .createChild("leg1Lower_" + side, 21, 45)
                .setPosition(4.59F, 0, -0.24F)
                .addBox(0, -0.5F, -0.5F, 4, 1, 1, -0.075F)
                .setAngles(0, -1.0471975511965976F, 0);

        legs.createChild("leg2_" + side, 21, 45)
                .setPosition(0, 0.6F, 1.1F)
                .addBox(0, -0.5F, -0.5F, 4, 1, 1, 0)
                .setAngles(0.13962634015954636F, -1.2217304763960306F, 1.5707963267948966F)
                .createChild("leg2Lower_" + side, 21, 45)
                .setPosition(3.65F, 0, -0.13F)
                .addBox(0, -0.5F, -0.5F, 4, 1, 1, -0.075F)
                .setAngles(0, -0.7853981633974483F, 0);

        legs.createChild("leg3_" + side, 21, 45)
                .setPosition(0, 0.3F, 1.1F)
                .addBox(0, -0.5F, -0.5F, 4, 1, 1, 0)
                .setAngles(-0.06981317007977318F, -1.8212510744560826F, 1.5707963267948966F)
                .createChild("leg3Lower_" + side, 21, 45)
                .setPosition(3.81F, 0, -0.05F)
                .addBox(0, -0.5F, -0.5F, 4, 1, 1, -0.075F)
                .setAngles(0, -0.4363323129985824F, 0);

        legs.createChild("leg4_" + side, 21, 45)
                .setPosition(0, -0.5F, 1.1F)
                .addBox(0, -0.5F, -0.5F, 4, 1, 1, 0)
                .setAngles(0.06981317007977318F, -2.1855012893472994F, 1.5707963267948966F)
                .createChild("leg4Lower_" + side, 21, 45)
                .setPosition(3.76F, 0, -0.06F)
                .addBox(0, -0.5F, -0.5F, 4, 1, 1, -0.075F)
                .setAngles(0, -0.5235987755982988F, 0);
    }
}
