package org.hedgetech.fairylightsredux.client.gui.component;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;

import java.util.List;

public class PaletteButton extends Button {
    private static final int TEX_U = 0;
    private static final int TEX_V = 40;
    private static final int SELECT_U = 28;
    private static final int SELECT_V = 40;
    private static final int COLOR_U = 34;
    private static final int COLOR_V = 40;
    private static final int COLOR_SIZE = 6;

    private static final List<ChatFormatting> COLORS = List.of(
            ChatFormatting.WHITE, ChatFormatting.GRAY, ChatFormatting.DARK_GRAY, ChatFormatting.BLACK,
            ChatFormatting.RED, ChatFormatting.DARK_RED, ChatFormatting.YELLOW, ChatFormatting.GOLD,
            ChatFormatting.LIGHT_PURPLE, ChatFormatting.DARK_PURPLE, ChatFormatting.GREEN, ChatFormatting.DARK_GREEN,
            ChatFormatting.BLUE, ChatFormatting.DARK_BLUE, ChatFormatting.AQUA, ChatFormatting.DARK_AQUA
    );

    private final ColorButton colorBtn;

    public PaletteButton(final int x, final int y, final ColorButton colorBtn, final Component msg, final Button.OnPress onPress) {
        super(x, y, 28, 28, msg, onPress, DEFAULT_NARRATION);
        this.colorBtn = colorBtn;
    }

    @Override
    public void onPress() {
        this.colorBtn.setDisplayColor(COLORS.get((COLORS.indexOf(this.colorBtn.getDisplayColor()) + 1) % COLORS.size()));
        super.onPress();
    }

    @Override
    public void onClick(final double mouseX, final double mouseY) {
        final int idx = this.getMouseOverIndex(mouseX, mouseY);
        if (idx > -1) {
            this.colorBtn.setDisplayColor(COLORS.get(idx));
            super.onPress();
        }
    }

    @Override
    public void renderWidget(final GuiGraphics graphics, final int mouseX, final int mouseY, final float delta) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), TEX_U, TEX_V, this.width, this.height, 256, 256);

        if (this.colorBtn.hasDisplayColor()) {
            this.blitCell(graphics, COLORS.indexOf(this.colorBtn.getDisplayColor()), SELECT_U, SELECT_V, -1);
        }

        for (int i = 0; i < COLORS.size(); i++) {
            this.blitCell(graphics, i, COLOR_U, COLOR_V, ARGB.opaque(COLORS.get(i).getColor()));
        }

        final int hovered = this.getMouseOverIndex(mouseX, mouseY);
        if (hovered > -1) {
            this.blitCell(graphics, hovered, SELECT_U, SELECT_V, ARGB.white(0.5F));
        }
    }

    private void blitCell(final GuiGraphics graphics, final int index, final int u, final int v, final int color) {
        final int x = this.getX() + 2 + index % 4 * COLOR_SIZE;
        final int y = this.getY() + 2 + index / 4 * COLOR_SIZE;
        graphics.blit(RenderPipelines.GUI_TEXTURED, EditLetteredConnectionScreen.WIDGETS_TEXTURE, x, y, u, v, COLOR_SIZE, COLOR_SIZE, 256, 256, color);
    }

    private int getMouseOverIndex(final double mouseX, final double mouseY) {
        final int relX = Mth.floor(mouseX - this.getX() - 3);
        final int relY = Mth.floor(mouseY - this.getY() - 3);

        if (relX < 0 || relY < 0 || relX > 22 || relY > 22 || relX % 6 > 3 || relY % 6 > 3) {
            return -1;
        }

        return relX / 6 + relY / 6 * 4;
    }
}
