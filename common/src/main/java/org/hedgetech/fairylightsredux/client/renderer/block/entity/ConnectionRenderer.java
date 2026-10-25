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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.util.FLRMth;
import org.hedgetech.fairylightsredux.util.ICurve;

import java.util.function.Function;

public abstract class ConnectionRenderer<C extends Connection> {
    protected static final ResourceLocation TEXTURE = Constants.id("textures/entity/connections.png");

    private final ModelPart wire;
    private final float wireInflate;

    protected ConnectionRenderer(final Function<ModelLayerLocation, ModelPart> baker, final ModelLayerLocation wireModelLocation) {
        this(baker, wireModelLocation, 0.0F);
    }

    protected ConnectionRenderer(final Function<ModelLayerLocation, ModelPart> baker, final ModelLayerLocation wireModelLocation, final float wireInflate) {
        this.wire = baker.apply(wireModelLocation);
        this.wireInflate = wireInflate;
    }

    public void render(final C conn, final float delta, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {
        final ICurve currCat = conn.getCatenary();
        final ICurve prevCat = conn.getPrevCatenary();

        if (currCat != null && prevCat != null) {
            final ICurve cat = prevCat.lerp(currCat, delta);
            final ICurve.ISegmentIterator it = cat.iterator();
            final VertexConsumer buf = source.getBuffer(RenderType.entityCutout(TEXTURE));
            final int color = ARGB.opaque(this.getWireColor(conn));

            while (it.next()) {
                matrix.pushPose();
                matrix.translate(it.getX(0.0F), it.getY(0.0F), it.getZ(0.0F));
                matrix.mulPose(Axis.YP.rotation(FLRMth.PI / 2.0F - it.getYaw()));
                matrix.mulPose(Axis.XP.rotation(-it.getPitch()));
                matrix.scale(1.0F + this.wireInflate, 1.0F, it.getLength() * 16.0F);
                this.wire.render(matrix, buf, packedLight, packedOverlay, color);
                matrix.popPose();
            }

            this.renderAlongCurve(conn, cat, matrix, source, packedLight, packedOverlay);
        }
    }

    protected void renderAlongCurve(final C conn, final ICurve catenary, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {}

    protected int getWireColor(final C conn) {
        return 0xFFFFFF;
    }

    public static LayerDefinition createWireLayer(final int u, final int v, final int size) {
        final MeshDefinition mesh = new MeshDefinition();
        mesh.getRoot().addOrReplaceChild("root", CubeListBuilder.create()
                .texOffs(u,v)
                .addBox(-size * 0.5F, -size * 0.5F, 0.0F, size, size, 1.0F),
            PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
    }
}
