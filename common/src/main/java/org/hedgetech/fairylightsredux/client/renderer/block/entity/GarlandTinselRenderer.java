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
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.hedgetech.fairylightsredux.client.FLRModelLayers;
import org.hedgetech.fairylightsredux.server.connection.GarlandTinselConnection;
import org.hedgetech.fairylightsredux.util.ICurve;
import org.hedgetech.fairylightsredux.util.RandomArray;

import java.util.function.Function;

public class GarlandTinselRenderer extends ConnectionRenderer<GarlandTinselConnection> {
    private static final RandomArray RAND = new RandomArray(9171, 128);

    private final ModelPart strip;

    public GarlandTinselRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        super(baker, FLRModelLayers.TINSEL_WIRE);
        this.strip = baker.apply(FLRModelLayers.TINSEL_STRIP);
    }

    @Override
    protected int getWireColor(final GarlandTinselConnection conn) {
        return conn.getColor();
    }

    @Override
    protected void renderAlongCurve(final GarlandTinselConnection conn, final ICurve catenary, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {
        final int color = ARGB.opaque(conn.getColor());
        final int hash = conn.getUUID().hashCode();
        final VertexConsumer buf = source.getBuffer(RenderType.entityCutout(TEXTURE));
        final ICurve.ISegmentIterator it = catenary.iterator();

        while(it.next()) {
            final int index = it.getIndex();
            final float length = it.getLength();
            final int strips = Mth.ceil(length * 64);

            matrix.pushPose();
            matrix.translate(it.getX(0.0F), it.getY(0.0F), it.getZ(0.0F));
            matrix.mulPose(Axis.YP.rotation(-it.getYaw()));
            matrix.mulPose(Axis.ZP.rotation(it.getPitch()));

            for (int i = 0; i < strips; i++) {
                matrix.pushPose();
                matrix.translate(i / (float) strips * length, 0.0F, 0.0F);
                matrix.mulPose(Axis.XP.rotationDegrees(RAND.get(31 * (index + 7 + 31 * i) + hash) * 180));
                matrix.mulPose(Axis.YP.rotationDegrees(RAND.get(31 * (index + 3 + 31 * i) + hash) * 180));
                matrix.mulPose(Axis.ZP.rotationDegrees(RAND.get(31 * (index + 31 * i) + hash) * 22));
                matrix.scale(1.0F, RAND.get(i * 63) * 0.1F + 1.0F, 0.5F);
                this.strip.render(matrix, buf, packedLight, packedOverlay, color);
                matrix.popPose();
            }

            matrix.popPose();
        }
    }

    public static LayerDefinition wireLayer() {
        return createWireLayer(62, 0, 1);
    }

    public static LayerDefinition stripLayer() {
        final MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("root", CubeListBuilder.create()
                .texOffs(62, 0)
                .addBox(-0.5F, -3.0F, 0.0F, 1.0F, 6.0F, 0.0F),
            PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
    }
}
