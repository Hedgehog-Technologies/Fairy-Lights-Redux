package org.hedgetech.fairylightsredux.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.ConnectionHighlightRenderer;
import org.hedgetech.fairylightsredux.client.renderer.block.entity.FastenerRenderer;
import org.hedgetech.fairylightsredux.server.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.jetbrains.annotations.NotNull;

public final class FenceFastenerRenderer extends EntityRenderer<FenceFastenerEntity, FenceFastenerRenderer.State> {
    private static final ResourceLocation TEXTURE = Constants.id("textures/block/fastener.png");
    private static final float R = 3.0F / 16.0F;

    private final FastenerRenderer renderer;

    public FenceFastenerRenderer(final EntityRendererProvider.Context context) {
        super(context);
        this.renderer = new FastenerRenderer(context::bakeLayer);
    }

    @Override
    public @NotNull State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(final @NotNull FenceFastenerEntity entity, final @NotNull State state, final float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.fastener = entity.getFastener();
        state.partialTick = partialTick;
        state.highlighted = Minecraft.getInstance().crosshairPickEntity == entity;
    }

    @Override
    protected @NotNull AABB getBoundingBoxForCulling(final FenceFastenerEntity entity) {
        return entity.getFastener().getBounds().inflate(1);
    }

    @Override
    public void render(final State state, final PoseStack matrix, final MultiBufferSource source, final int packedLight) {
        this.renderKnot(matrix.last(), source.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), packedLight);
        this.renderer.render(state.fastener, state.partialTick, matrix, source, packedLight, OverlayTexture.NO_OVERLAY);
        if (state.highlighted) {
            ConnectionHighlightRenderer.renderBox(matrix, source, new AABB(-R, -R, -R, R, R, R).inflate(0.002D));
        }
        super.render(state, matrix, source, packedLight);
    }

    private void renderKnot(final PoseStack.Pose pose, final VertexConsumer buf, final int light) {
        quad(pose, buf, light, 0, -1, 0, new float[][]{{-R, -R, -R}, {-R, -R, R}, {R, -R, R}, {R, -R, -R}}, 8, 4, 14, 10);
        quad(pose, buf, light, 0, 1, 0, new float[][]{{-R, R, -R}, {-R, R, R}, {R, R, R}, {R, R, -R}}, 8, 4, 14, 10);
        quad(pose, buf, light, 0, 0, -1, new float[][]{{-R, R, -R}, {-R, -R, -R}, {R, -R, -R}, {R, R, -R}}, 2, 8, 8, 14);
        quad(pose, buf, light, 0, 0, 1, new float[][]{{-R, R, R}, {-R, -R, R}, {R, -R, R}, {R, R, R}}, 2, 8, 8, 14);
        quad(pose, buf, light, -1, 0, 0, new float[][]{{-R, R, -R}, {-R, -R, -R}, {-R, -R, R}, {-R, R, R}}, 8, 10, 14, 16);
        quad(pose, buf, light, 1, 0, 0, new float[][]{{R, R, -R}, {R, -R, -R}, {R, -R, R}, {R, R, R}}, 8, 10, 14, 16);
    }

    private static void quad(final PoseStack.Pose pose, final VertexConsumer buf, final int light, final float nx, final float ny, final float nz, final float[][] v, final float u0, final float v0, final float u1, final float v1) {
        final float[][] uv = { { u0, v0 }, { u0, v1 }, { u1, v1 }, { u1, v0 } };

        for (int i = 0; i < 4; i++) {
            buf.addVertex(pose, v[i][0], v[i][1], v[i][2])
                    .setColor(-1)
                    .setUv(uv[i][0] / 16.0F, uv[i][1] / 16.0F)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, nx, ny, nz);
        }
    }

    public static final class State extends EntityRenderState {
        public Fastener<?> fastener;
        public float partialTick;
        public boolean highlighted;
    }
}
