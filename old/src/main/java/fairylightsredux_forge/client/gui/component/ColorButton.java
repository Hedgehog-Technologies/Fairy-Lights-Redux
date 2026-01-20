package org.hedgetech.fairylightsredux.client.gui.component;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;
import org.hedgetech.fairylightsredux.util.Utils;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.jspecify.annotations.NonNull;

public final class ColorButton extends Button {
    private static final float TEX_U = 0F;
    private static final float TEX_V = 0F;

    private ChatFormatting displayColor;
    private float displayColorR;
    private float displayColorG;
    private float displayColorB;

    public ColorButton(final int x, final int y, final Component msg, final Button.OnPress onPress) {
        super(x, y, 20, 20, msg, onPress, DEFAULT_NARRATION);
    }

    public void setDisplayColor(final ChatFormatting color) {
        this.displayColor = color;
        final int rgb = StyledString.getColor(color);
        this.displayColorR = (rgb >> 16 & 0xFF) / 255F;
        this.displayColorG = (rgb >> 8 & 0xFF) / 255F;
        this.displayColorB = (rgb & 0xFF) / 255F;
    }

    public ChatFormatting getDisplayColor() {
        return this.displayColor;
    }

    public void removeDisplayColor() {
        this.displayColor = null;
    }

    public boolean hasDisplayColor() {
        return this.displayColor != null;
    }

    @Override
    public void renderWidget(final @NonNull GuiGraphics stack, final int mouseX, final int mouseY, final float delta) {
        if (!this.visible) return;

        stack.blit(RenderPipelines.GUI, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), TEX_U, this.isHovered ? TEX_V + this.height : TEX_V, this.width, this.height, EditLetteredConnectionScreen.WIDGETS_TEXTURE_WIDTH, EditLetteredConnectionScreen.WIDGETS_TEXTURE_HEIGHT);

        if (this.displayColor != null) {
            stack.blit(RenderPipelines.GUI, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), TEX_U + this.width, TEX_V, this.width, this.height, EditLetteredConnectionScreen.WIDGETS_TEXTURE_WIDTH, EditLetteredConnectionScreen.WIDGETS_TEXTURE_HEIGHT);
            int tint = Utils.argb(this.displayColorR, this.displayColorG, this.displayColorB, 1.0F);
            stack.blit(RenderPipelines.GUI, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), TEX_U + this.width, TEX_V + this.height, this.width, this.height, EditLetteredConnectionScreen.WIDGETS_TEXTURE_WIDTH, EditLetteredConnectionScreen.WIDGETS_TEXTURE_HEIGHT, tint);
        }
    }
}
