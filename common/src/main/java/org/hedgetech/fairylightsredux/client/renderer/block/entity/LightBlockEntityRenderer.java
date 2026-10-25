package org.hedgetech.fairylightsredux.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.client.model.light.LightModel;
import org.hedgetech.fairylightsredux.server.block.LightBlock;
import org.hedgetech.fairylightsredux.server.block.entity.LightBlockEntity;
import org.hedgetech.fairylightsredux.server.feature.light.ILightBehavior;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.jetbrains.annotations.NotNull;

public class LightBlockEntityRenderer implements BlockEntityRenderer<LightBlockEntity> {
    private final LightRenderer lights;

    public LightBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        this.lights = new LightRenderer(context::bakeLayer);
    }

    @Override
    public void render(final @NotNull LightBlockEntity entity, final float delta, final @NotNull PoseStack matrix, final @NotNull MultiBufferSource source, final int packedLight, final int packedOverlay, final @NotNull Vec3 cameraPos) {
        this.render(entity, delta, matrix, source, packedLight, packedOverlay, entity.getLight());
    }

    private <T extends ILightBehavior> void render(final LightBlockEntity entity, final float delta, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay, final Light<T> light) {
        final LightModel<T> model = this.lights.getModel(light, -1);
        final AABB box = model.getBounds();
        final BlockState state = entity.getBlockState();
        final AttachFace face = state.getValue(LightBlock.FACE);
        final float rotation = state.getValue(LightBlock.FACING).toYRot();

        matrix.pushPose();
        matrix.translate(0.5D, 0.5D, 0.5D);
        matrix.mulPose(Axis.YP.rotationDegrees(180.0F - rotation));

        if (light.getVariant().isOrientable()) {
            if (face == AttachFace.WALL) {
                matrix.mulPose(Axis.XP.rotationDegrees(90.0F));
            } else if (face == AttachFace.FLOOR) {
                matrix.mulPose(Axis.XP.rotationDegrees(-180.0F));
            }

            matrix.translate(0.0D, 0.51D, 0.0D);
        } else {
            if (face == AttachFace.CEILING) {
                matrix.translate(0.0D, 0.25D, 0.0D);
            } else if (face == AttachFace.WALL) {
                matrix.translate(0.0D, 3.0D / 16.0D, 0.125D);
            } else {
                matrix.translate(0.0D, -box.minY - model.getFloorOffset() - 0.5D, 0.0D);
            }
        }

        this.lights.render(matrix, this.lights.start(source), light, model, delta, packedLight, packedOverlay);
        matrix.popPose();
    }
}
