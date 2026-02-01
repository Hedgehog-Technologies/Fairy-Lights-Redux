package org.hedgetech.fairylightsredux.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.hedgetech.fairylightsredux.client.model.FLRModelLayers;
import org.hedgetech.fairylightsredux.client.model.light.BowModel;
import org.hedgetech.fairylightsredux.connection.Connection;
import org.hedgetech.fairylightsredux.connection.HangingLightsConnection;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.fastener.FenceFastener;
import org.hedgetech.fairylightsredux.network.NetworkClientProxy;

import java.util.function.Function;

public class FastenerRenderer {
    private final HangingLightsRenderer hangingLights;
    private final GarlandVineRenderer garland;
    private final GarlandTinselRenderer tinsel;
    private final PennantBuntingRenderer pennants;
    private final LetterBuntingRenderer letters;
    private final BowModel bow;

    public FastenerRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        this.hangingLights = new HangingLightsRenderer(baker);
        this.garland = new GarlandVineRenderer(baker);
        this.tinsel = new GarlandTinselRenderer(baker);
        this.pennants = new PennantBuntingRenderer(baker);
        this.letters = new LetterBuntingRenderer(baker);
        this.bow = new BowModel(baker.apply(FLRModelLayers.BOW));
    }

    public void render(final Fastener<?> fastener, final float delta, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {
        boolean renderBow = true;
        for (final Connection conn : fastener.getAllConnections()) {
            if (conn.getFastener() == fastener) {
                this.renderConnection(delta, matrix, source, packedLight, packedOverlay, conn);
            }
            if (renderBow
                    && conn instanceof GarlandVineConnection
                    && this.renderBow(fastener, matrix, source, packedLight, packedOverlay)
            ) {
                renderBow = false;
            }
        }
    }

    private boolean renderBow(Fastener<?> fastener, PoseStack matrix, MultiBufferSource source, int packedLight, int packedOverlay) {
        if (fastener instanceof FenceFastener) {
            final Level world = fastener.getWorld();
            if (world == null) return false;
            final BlockState state = world.getBlockState(fastener.getPos());
            if (!state.is(BlockTags.FENCES)) return false;
            final VertexConsumer buf = NetworkClientProxy.SOLID_TEXTURE.buffer(source, RenderType::entityCutout);
            final float offset = -1.5F / 16.0F;
            final boolean north = state.getValue(FenceBlock.NORTH);
            final boolean east = state.getValue(FenceBlock.EAST);
            final boolean south = state.getValue(FenceBlock.SOUTH);
            final boolean west = state.getValue(FenceBlock.WEST);
            boolean tryDirX = true;
            boolean bow = false;
            if (!north && (east || west)) {
                this.bow(matrix, Direction.NORTH, offset, buf, packedLight, packedOverlay);
                tryDirX = false;
                bow = true;
            }
            if (!south && (east || west)) {
                this.bow(matrix, Direction.SOUTH, offset, buf, packedLight, packedOverlay);
                tryDirX = false;
                bow = true;
            }
            if (tryDirX) {
                if (!east && (north || south)) {
                    this.bow(matrix, Direction.EAST, offset, buf, packedLight, packedOverlay);
                    bow = true;
                }
                if (!west && (north || south)) {
                    this.bow(matrix, Direction.WEST, offset, buf, packedLight, packedOverlay);
                    bow = true;
                }
            }
            return bow;
        } else if (fastener.getFacing().getAxis() != Direction.Axis.Y) {
            final VertexConsumer buf = NetworkClientProxy.SOLID_TEXTURE.buffer(source, RenderType::entityCutout);
            this.bow(matrix, fastener.getFacing(), 0.0F, buf, packedLight, packedOverlay);
            return true;
        }
        return false;
    }

    private void bow(PoseStack matrix, Direction dir, float offset, VertexConsumer buf, int packedLight, int packedOverlay) {
        matrix.pushPose();;
        matrix.mulPose(Axis.YP.rotationDegrees(180.0F - dir.toYRot()));
        if (offset != 0.0F) {
            matrix.translate(0.0D, 0.0D, offset);
        }
        this.bow.renderToBuffer(matrix, buf, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrix.popPose();
    }

    private void renderConnection(final float delta, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay, final Connection conn) {
        if (conn instanceof HangingLightsConnection hlc) {
            this.hangingLights.render(hlc, delta, matrix, source, packedLight, packedOverlay);
        } else if (conn instanceof GarlandVineConnection gvc) {
            this.garland.render(gvc, delta, matrix, source, packedLight, packedOverlay);
        } else if (conn instanceof GarlandTinselConnection gtc) {
            this.tinsel.render(gtc, delta, matrix, source, packedLight, packedOverlay);
        } else if (conn instanceof PennantBuntingConnection pbc) {
            this.pennants.render(pbc, delta, matrix, source, packedLight, packedOverlay);
        } else if (conn instanceof LetterBuntingConnection lbc) {
            this.letters.render(lbc, delta, matrix, source, packedLight, packedOverlay);
        }
    }

    public static void renderItemModel(ResourceLocation path, ItemDisplayContext context, PoseStack pose, MultiBufferSource source, int packedLight, int packedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        ModelManager models = mc.getModelManager();
        ItemModel model = models.getItemModel(path);
        renderItemModel(model, context, pose, source, packedLight, packedOverlay);
    }

    public static void renderItemModel(ItemModel model, ItemDisplayContext context, PoseStack pose, MultiBufferSource source, int packedLight, int packedOverlay) {
        Minecraft mc = Minecraft.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();
        ItemStackRenderState state = new ItemStackRenderState();

        model.update(state, ItemStack.EMPTY, mc.getItemModelResolver(), context, mc.level, null, 0);
        pose.pushPose();
        // TODO - Is this even right?!
        state.render(pose, source, packedLight, packedOverlay);
        pose.popPose();
    }

//    public static void renderBakedModel(final ResourceLocation path, final PoseStack matrix, final VertexConsumer buf, final float r, final float g, final float b, final int packedLight, final int packedOverlay) {
//        renderBakedModel(Minecraft.getInstance().getModelManager().getItemModel(path), matrix, buf, r, g, b, packedLight, packedOverlay);
//    }
}
