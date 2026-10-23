package org.hedgetech.fairylightsredux.client.gui.component;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;

public final class ColorButton extends Button {
    private ChatFormatting displayColor;

    public ColorButton(final int x, final int y, final Component msg, final Button.OnPress onPress) {
        super(x, y, 20, 20, msg, onPress, DEFAULT_NARRATION);
    }

    public void setDisplayColor(final ChatFormatting color) {
        this.displayColor = color;
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
    public void renderWidget(final GuiGraphics graphics, final int mouseX, final int mouseY, final float delta) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), 0, this.isHovered ? this.height : 0, this.width, this.height, 256, 256);

        if (this.displayColor != null) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), this.width, 0, this.width, this.height, 256, 256);
            graphics.blit(RenderPipelines.GUI_TEXTURED, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), this.width, this.height, this.width, this.height, 256, 256, ARGB.opaque(this.displayColor.getColor()));
        }
    }
}
