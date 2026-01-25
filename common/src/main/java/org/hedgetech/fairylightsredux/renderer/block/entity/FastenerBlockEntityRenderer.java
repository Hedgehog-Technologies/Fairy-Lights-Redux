package org.hedgetech.fairylightsredux.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.block.entity.FastenerBlockEntity;
import org.hedgetech.fairylightsredux.fastener.BlockView;
import org.hedgetech.fairylightsredux.fastener.Fastener;

public final class FastenerBlockEntityRenderer implements BlockEntityRenderer<FastenerBlockEntity> {
    private final BlockView view;
    private final FastenerRenderer renderer;

    public FastenerBlockEntityRenderer(final BlockEntityRendererProvider.Context context, final BlockView view) {
        this.view = view;
        this.renderer = new FastenerRenderer(context::bakeLayer);
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    @Override
    public void render(final FastenerBlockEntity fastener, final float delta, final PoseStack matrix, final MultiBufferSource bufferSource, final int packedLight, final int packedOverlay, Vec3 cameraPos) {
        Fastener<?> f = fastener.getFastener();
        matrix.pushPose();
        final Vec3 offset = fastener.getOffset();
        matrix.translate(offset.x, offset.y, offset.z);
        this.renderer.render(f, delta, matrix, bufferSource, packedLight, packedOverlay);
        matrix.popPose();
    }
}
