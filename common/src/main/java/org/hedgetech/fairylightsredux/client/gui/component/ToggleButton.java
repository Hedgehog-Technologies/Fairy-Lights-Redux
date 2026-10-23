package org.hedgetech.fairylightsredux.client.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;

public class ToggleButton extends Button {
    private final int u, v;

    private boolean value;
    private boolean pressed;

    public ToggleButton(final int x, final int y, final int u, final int v, final Component msg, final Button.OnPress onPress) {
        super(x, y, 20, 20, msg, onPress, DEFAULT_NARRATION);
        this.u = u;
        this.v = v;
    }

    public void setValue(final boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public void onPress() {
        this.value = !this.value;
        this.pressed = true;
        super.onPress();
    }

    @Override
    public void onRelease(final double mouseX, final double mouseY) {
        this.pressed = false;
    }

    @Override
    public void renderWidget(final GuiGraphics graphics, final int mouseX, final int mouseY, final float delta) {
        final int row = this.isHovered ? (this.pressed ? 2 : 1) : (this.value ? 2 : 0);
        graphics.blit(RenderPipelines.GUI_TEXTURED, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), this.u, this.v + this.height * row, this.width, this.height, 256, 256);
    }
}
