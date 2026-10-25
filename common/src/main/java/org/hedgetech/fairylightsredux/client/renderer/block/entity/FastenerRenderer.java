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
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.hedgetech.fairylightsredux.client.ConnectionHitHandler;
import org.hedgetech.fairylightsredux.client.FLRModelLayers;
import org.hedgetech.fairylightsredux.server.connection.*;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.fastener.FenceFastener;

import java.util.function.Function;

public class FastenerRenderer {
    private final HangingLightsRenderer hangingLights;
    private final PennantBuntingRenderer pennants;
    private final LetterBuntingRenderer letters;
    private final GarlandTinselRenderer tinsel;
    private final GarlandVineRenderer garland;
    private final ModelPart bow;

    public FastenerRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        this.hangingLights = new HangingLightsRenderer(baker);
        this.pennants = new PennantBuntingRenderer(baker);
        this.letters = new LetterBuntingRenderer(baker);
        this.tinsel = new GarlandTinselRenderer(baker);
        this.garland = new GarlandVineRenderer(baker);
        this.bow = baker.apply(FLRModelLayers.BOW);
    }

    public void render(final Fastener<?> fastener, final float delta, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {
        final ConnectionHitHandler.Hit hit = ConnectionHitHandler.getHit();

        for (final Connection conn : fastener.getOwnConnections()) {
            if (conn instanceof final HangingLightsConnection hl) {
                this.hangingLights.render(hl, delta, matrix, source, packedLight, packedOverlay);
            } else if (conn instanceof final PennantBuntingConnection pb) {
                this.pennants.render(pb, delta, matrix, source, packedLight, packedOverlay);
            } else if (conn instanceof final LetterBuntingConnection lb) {
                this.letters.render(lb, delta, matrix, source, packedLight, packedOverlay);
            } else if (conn instanceof final GarlandTinselConnection gt) {
                this.tinsel.render(gt, delta, matrix, source, packedLight, packedOverlay);
            } else if (conn instanceof final GarlandVineConnection gv) {
                this.garland.render(gv, delta, matrix, source, packedLight, packedOverlay);
            }

            if (hit != null && hit.connection() == conn) {
                ConnectionHighlightRenderer.render(hit, fastener.getConnectionPoint(), matrix, source);
            }
        }

        if (fastener.getAllConnections().stream().anyMatch(GarlandVineConnection.class::isInstance)) {
            this.renderBows(fastener, matrix, source.getBuffer(RenderType.entityCutout(ConnectionRenderer.TEXTURE)), packedLight, packedOverlay);
        }
    }

    private void renderBows(final Fastener<?> fastener, final PoseStack matrix, final VertexConsumer buf, final int packedLight, final int packedOverlay) {
        if (fastener instanceof FenceFastener) {
            final BlockState state = fastener.getWorld().getBlockState(fastener.getPos());
            if (!(state.getBlock() instanceof FenceBlock)) return;

            final float offset = -1.5F / 16.0F;
            final boolean north = state.getValue(FenceBlock.NORTH);
            final boolean east = state.getValue(FenceBlock.EAST);
            final boolean south = state.getValue(FenceBlock.SOUTH);
            final boolean west = state.getValue(FenceBlock.WEST);
            boolean tryDirX = true;

            if (!north && (east || west)) {
                this.renderBow(matrix, Direction.NORTH, offset, buf, packedLight, packedOverlay);
                tryDirX = false;
            }

            if (!south && (east || west)) {
                this.renderBow(matrix, Direction.SOUTH, offset, buf, packedLight, packedOverlay);
                tryDirX = false;
            }

            if (tryDirX) {
                if (!east && (north || south)) {
                    this.renderBow(matrix, Direction.EAST, offset, buf, packedLight, packedOverlay);
                }

                if (!west && (north || south)) {
                    this.renderBow(matrix, Direction.WEST, offset, buf, packedLight, packedOverlay);
                }
            }
        } else if (fastener.getFacing().getAxis() != Direction.Axis.Y) {
            this.renderBow(matrix, fastener.getFacing(), 0.0F, buf, packedLight, packedOverlay);
        }
    }

    private void renderBow(final PoseStack matrix, final Direction dir, final float offset, final VertexConsumer buf, final int packedLight, final int packedOverlay) {
        matrix.pushPose();
        matrix.mulPose(Axis.YP.rotationDegrees(180.0F - dir.toYRot()));
        matrix.translate(0.0F, 0.0F, offset);
        this.bow.render(matrix, buf, packedLight, packedOverlay);
        matrix.popPose();
    }

    public static LayerDefinition bowLayer() {
        final MeshDefinition mesh = new MeshDefinition();
        final PartDefinition root = mesh.getRoot().addOrReplaceChild("root", CubeListBuilder.create()
                .texOffs(6, 72)
                .addBox(-2.0F, -1.5F, -1.0F, 4.0F, 3.0F, 2.0F),
            PartPose.offset(0.0F, 0.5F, -3.25F));

        root.addOrReplaceChild("bone", CubeListBuilder.create()
                .texOffs(0, 77)
                .addBox(-5.0F, -4.0F, 0.0F, 5.0F, 5.0F, 1.0F),
            PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, 0.0F, 0.1745F, -0.5236F));

        root.addOrReplaceChild("bone2", CubeListBuilder.create()
                .texOffs(0, 77)
                .addBox(0.0F, -4.0F, 0.0F, 5.0F, 5.0F, 1.0F),
            PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, -0.1745F, 0.5236F));

        root.addOrReplaceChild("bone3", CubeListBuilder.create()
                .texOffs(0, 72)
                .addBox(-2.0F, -4.0F, 0.0F, 2.0F, 4.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0873F, 0.0873F, -0.1745F));

        root.addOrReplaceChild("bone4", CubeListBuilder.create()
                .texOffs(0, 72)
                .addBox(0.0F, -4.0F, 0.0F, 2.0F, 4.0F, 1.0F),
            PartPose.offsetAndRotation(0.0F, -1.0F, 0.0F, 0.0873F, -0.0873F, 0.1745F));

        return LayerDefinition.create(mesh, 128, 128);
    }
}
