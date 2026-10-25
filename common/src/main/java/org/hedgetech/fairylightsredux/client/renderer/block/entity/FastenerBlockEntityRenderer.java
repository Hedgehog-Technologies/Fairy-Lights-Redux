package org.hedgetech.fairylightsredux.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.block.entity.FastenerBlockEntity;
import org.jetbrains.annotations.NotNull;

public final class FastenerBlockEntityRenderer implements BlockEntityRenderer<FastenerBlockEntity> {
    private final FastenerRenderer renderer;

    public FastenerBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        this.renderer = new FastenerRenderer(context::bakeLayer);
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public void render(final FastenerBlockEntity fastener, final float delta, final PoseStack matrix, final @NotNull MultiBufferSource source, final int packedLight, final int packedOverlay, final @NotNull Vec3 cameraPos) {
        matrix.pushPose();
        final Vec3 offset = fastener.getOffset();
        matrix.translate(offset.x, offset.y, offset.z);
        this.renderer.render(fastener.getFastener(), delta, matrix, source, packedLight, packedOverlay);
        matrix.popPose();;
    }
}
