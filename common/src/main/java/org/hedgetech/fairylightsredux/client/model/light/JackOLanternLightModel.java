package org.hedgetech.fairylightsredux.client.model.light;

import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.core.Direction;
import org.hedgetech.fairylightsredux.util.FLRMth;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Set;

public class JackOLanternLightModel extends ColorLightModel {
    public JackOLanternLightModel(final ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        final LightMeshHelper helper = LightMeshHelper.create();
        helper.createBulb().createChild("pumpkin", 28, 42)
                .addBox(-3, 0, -3, 6, 6, 6, 0)
                .setAngles(FLRMth.PI, 0.0F, 0.0F);

        final EasyMeshBuilder leaf1 = new EasyMeshBuilder("leaf1", 12, 18)
                .setRotationPoint(0.5F, 0, 0.5F)
                .addBox(0, -0.5F, 0, 2, 1, 2, 0);

        final Vector3f vec = new Vector3f(-1.0F, 0.0F, 1.0F).normalize();
        final Quaternionf droop = Axis.of(vec).rotation(FLRMth.PI / 12.0F);
        float[] leafAngles = toEuler(droop);
        leaf1.setRotationAngles(leafAngles[0], leafAngles[1], leafAngles[2]);
        helper.unlit().addChild(leaf1);

        final EasyMeshBuilder leaf2 = new EasyMeshBuilder("leaf2", 12, 18)
                .setRotationPoint(-0.5F, 0, -0.5F)
                .addBox(0, -0.5F, 0, 2, 1, 2, 0);
        final Quaternionf q = Axis.YP.rotation(FLRMth.PI).mul(droop);
        leafAngles = toEuler(q);
        leaf2.setRotationAngles(leafAngles[0], leafAngles[1], leafAngles[2]);
        helper.unlit().addChild(leaf2);

        final EasyMeshBuilder stem = new EasyMeshBuilder("stem", 21, 41)
                .setRotationPoint(0, 2, 0)
                .addBox(-1, 0, -1, 2, 2, 2, -0.05F);
        stem.xRot = FLRMth.PI;
        helper.unlit().addChild(stem);

        final EasyMeshBuilder face = new EasyMeshBuilder("face", 56, 34)
                .setRotationPoint(0, -3, -3.25F)
                .addBox(-3, -3, 0, 6, 6, 0, Set.of(Direction.NORTH))
                .setRotationAngles(FLRMth.PI, FLRMth.PI);
        helper.lit().addChild(face);

        return helper.build();
    }
}
