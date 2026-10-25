package org.hedgetech.fairylightsredux.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.client.FLRModelLayers;
import org.hedgetech.fairylightsredux.server.connection.LetterBuntingConnection;
import org.hedgetech.fairylightsredux.server.feature.Letter;
import org.hedgetech.fairylightsredux.server.item.FLRItems;

import java.util.Locale;
import java.util.function.Function;

public class LetterBuntingRenderer extends ConnectionRenderer<LetterBuntingConnection> {
    private static final String LETTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ&!?";

    private final ItemStackRenderState item = new ItemStackRenderState();
    private final Long2ObjectMap<ItemStack> stacks = new Long2ObjectOpenHashMap<>();

    public LetterBuntingRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        super(baker, FLRModelLayers.LETTER_WIRE);
    }

    @Override
    public void render(final LetterBuntingConnection conn, final float delta, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {
        super.render(conn, delta, matrix, source, packedLight, packedOverlay);
        final ItemModelResolver resolver = Minecraft.getInstance().getItemModelResolver();

        for (final Letter letter : conn.getLetters()) {
            if (LETTERS.indexOf(letter.getLetter()) == -1) continue;

            final Vec3 pos = letter.getPoint(delta);
            matrix.pushPose();
            matrix.translate(pos.x, pos.y, pos.z);
            matrix.mulPose(Axis.YP.rotation(-letter.getYaw(delta)));
            matrix.mulPose(Axis.ZP.rotation(letter.getPitch(delta)));
            matrix.mulPose(Axis.XP.rotation(letter.getRoll(delta)));
            matrix.translate(0.0F, -0.5F - 0.5F / 16.0F, 0.0F);
            resolver.updateForTopItem(this.item, this.getStack(letter), ItemDisplayContext.NONE, conn.getWorld(), null, 0);
            this.item.render(matrix, source, packedLight, packedOverlay);
            matrix.popPose();
        }
    }

    private ItemStack getStack(final Letter letter) {
        final int color = letter.getStyle().getColor().getColor();

        return this.stacks.computeIfAbsent((long) letter.getLetter() << 32 | color & 0xFFFFFFFFL, key -> {
            final ItemStack stack = new ItemStack(FLRItems.LETTER_BUNTING);
            final String name = Character.getName(letter.getLetter()).toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "_");
            stack.set(DataComponents.ITEM_MODEL, Constants.id("letter/" + name));
            stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color));
            return stack;
        });
    }

    public static LayerDefinition wireLayer() {
        return createWireLayer(0, 17, 1);
    }
}
