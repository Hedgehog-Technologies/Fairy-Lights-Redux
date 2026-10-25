package org.hedgetech.fairylightsredux.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.hedgetech.fairylightsredux.client.FLRModelLayers;
import org.hedgetech.fairylightsredux.server.connection.GarlandVineConnection;
import org.hedgetech.fairylightsredux.util.ICurve;
import org.hedgetech.fairylightsredux.util.RandomArray;

import java.util.function.Function;

public class GarlandVineRenderer extends ConnectionRenderer<GarlandVineConnection> {
    private static final int RING_COUNT = 7;
    private static final RandomArray RAND = new RandomArray(8411, RING_COUNT * 4);

    private final ModelPart[] rings = new ModelPart[RING_COUNT];

    public GarlandVineRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        super(baker, FLRModelLayers.VINE_WIRE);
        final ModelPart root = baker.apply(FLRModelLayers.GARLAND_RINGS);

        for (int i = 0; i < RING_COUNT; i++) {
            this.rings[i] = root.getChild(Integer.toString(i));
        }
    }

    @Override
    protected void renderAlongCurve(final GarlandVineConnection conn, final ICurve catenary, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {
        final int hash = conn.getUUID().hashCode();
        final VertexConsumer buf = source.getBuffer(RenderType.entityCutout(TEXTURE));

        catenary.visitPoints(0.25F, false, (index, x, y, z, yaw, pitch) -> {
            matrix.pushPose();
            matrix.translate(x, y, z);
            matrix.mulPose(Axis.YP.rotation(-yaw));
            matrix.mulPose(Axis.ZP.rotation(pitch));
            matrix.mulPose(Axis.ZP.rotationDegrees(RAND.get(index + hash) * 45.0F));
            matrix.mulPose(Axis.YP.rotationDegrees(RAND.get(index + 8 + hash) * 60.0F + 90.0F));
            this.rings[index % RING_COUNT].render(matrix, buf, packedLight, packedOverlay);
            matrix.popPose();
        });
    }

    public static LayerDefinition wireLayer() {
        return createWireLayer(39, 0, 1);
    }

    public static LayerDefinition ringsLayer() {
        final float size = 4.0F;
        final CubeListBuilder ring = CubeListBuilder.create()
                .texOffs(14, 91)
                .addBox(-size / 2.0F, -size / 2.0F, -size / 2.0F, size, size, size);
        final PartPose crossPose = PartPose.rotation(0.0F, 0.0F, Mth.HALF_PI);
        final MeshDefinition mesh = new MeshDefinition();

        for (int i = 0; i < RING_COUNT; i++) {
            mesh.getRoot().addOrReplaceChild(Integer.toString(i), ring, PartPose.ZERO)
                    .addOrReplaceChild("cross_" + i, CubeListBuilder.create()
                            .texOffs(i * 8, 64)
                            .addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F)
                            .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F),
                        crossPose);
        }

        return LayerDefinition.create(mesh, 128, 128);
    }
}
