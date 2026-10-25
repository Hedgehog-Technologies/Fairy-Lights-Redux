package org.hedgetech.fairylightsredux.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.client.FLRModelLayers;
import org.hedgetech.fairylightsredux.server.connection.PennantBuntingConnection;
import org.hedgetech.fairylightsredux.server.feature.Pennant;
import org.hedgetech.fairylightsredux.util.styledstring.Style;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.joml.Matrix3f;
import org.joml.Vector3f;

import java.util.function.Function;

public class PennantBuntingRenderer extends ConnectionRenderer<PennantBuntingConnection> {
    private final ItemStackRenderState item = new ItemStackRenderState();

    public PennantBuntingRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        super(baker, FLRModelLayers.PENNANT_WIRE, 0.25F);
    }

    @Override
    public void render(final PennantBuntingConnection conn, final float delta, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {
        super.render(conn, delta, matrix, source, packedLight, packedOverlay);
        final ItemModelResolver resolver = Minecraft.getInstance().getItemModelResolver();
        final Font font = Minecraft.getInstance().font;
        final Pennant[] pennants = conn.getFeatures();
        StyledString text = conn.getText();

        if (text.length() > pennants.length) {
            text = text.substring(0, pennants.length);
        }

        final int offset = (pennants.length - text.length()) / 2;

        for (int i = 0; i < pennants.length; i++) {
            final Pennant pennant = pennants[i];
            final Vec3 pos = pennant.getPoint(delta);

            matrix.pushPose();
            matrix.translate(pos.x, pos.y, pos.z);
            matrix.mulPose(Axis.YP.rotation(-pennant.getYaw(delta)));
            matrix.mulPose(Axis.ZP.rotation(pennant.getPitch(delta)));
            matrix.mulPose(Axis.XP.rotation(pennant.getRoll(delta)));

            matrix.pushPose();
            matrix.translate(0.5F / 16.0F, -0.5F, 0.0F);
            resolver.updateForTopItem(this.item, pennant.getItem(), ItemDisplayContext.NONE, conn.getWorld(), null, 0);
            matrix.popPose();

            if (i >= offset && i < offset + text.length()) {
                this.drawLetter(matrix, source, pennant, packedLight, font, text, i - offset, 1, delta);
                this.drawLetter(matrix, source, pennant, packedLight, font, text, text.length() - 1 - (i - offset), -1, delta);
            }

            matrix.popPose();
        }
    }

    private void drawLetter(final PoseStack matrix, final MultiBufferSource source, final Pennant pennant, final int packedLight, final Font font, final StyledString text, final int index, final int side, final float delta) {
        final Style style = text.styleAt(index);
        final StringBuilder chr = new StringBuilder();

        if (style.isObfuscated()) chr.append(ChatFormatting.OBFUSCATED);
        if (style.isBold()) chr.append(ChatFormatting.BOLD);
        if (style.isStrikethrough()) chr.append(ChatFormatting.STRIKETHROUGH);
        if (style.isUnderline()) chr.append(ChatFormatting.UNDERLINE);
        if (style.isItalic()) chr.append(ChatFormatting.ITALIC);

        chr.append(text.charAt(index));

        final Vector3f normal = new Matrix3f()
                .rotate(Axis.YP.rotation(pennant.getYaw(delta)))
                .rotate(Axis.ZP.rotation(pennant.getPitch(delta)))
                .rotate(Axis.XP.rotation(pennant.getRoll(delta)))
                .transform(new Vector3f(0.0F, 0.0F, side));
        final int color = ARGB.scaleRGB(ARGB.opaque(style.getColor().getColor()), diffuseLight(normal.x(), normal.y(), normal.z()));
        final float scale = 0.03075F;
        final String str = chr.toString();

        matrix.pushPose();
        matrix.translate(0.0F, -0.25F, 0.04F * side);
        matrix.scale(scale * side, -scale, scale);
        font.drawInBatch(str, -(font.width(str) - 1.0F) / 2.0F, -4.0F, color, false, matrix.last().pose(), source, Font.DisplayMode.NORMAL, 0, packedLight);
        matrix.popPose();
    }

    private static float diffuseLight(final float x, final float y, final float z) {
        return Math.min(x * x * 0.6F + y * y * ((3.0F + y) / 4.0F) + z * z * 0.8F, 1.0F);
    }

    public static LayerDefinition wireLayer() {
        return createWireLayer(0, 17, 1);
    }
}
