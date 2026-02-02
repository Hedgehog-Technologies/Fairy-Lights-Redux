package org.hedgetech.fairylightsredux.client.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;

public class ToggleButton extends Button {
    private final float u;
    private final float v;
    private boolean value;
    private boolean pressed;

    public ToggleButton(final int x, final int y, final int u, final int v, final Component msg, final Button.OnPress onPress) {
        this(x, y, (float) u, (float) v, msg, onPress);
    }

    public ToggleButton(final int x, final int y, final float u, final float v, final Component msg, final Button.OnPress onPress) {
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
    public void renderWidget(final @NonNull GuiGraphics stack, final int mouseX, final int mouseY, final float delta) {
        final int t;
        if (this.isHovered) {
            if (this.pressed) {
                t = 2;
            } else {
                t = 1;
            }
        } else {
            if (this.value) {
                t = 2;
            } else {
                t = 0;
            }
        }
        stack.blit(RenderPipelines.GUI, EditLetteredConnectionScreen.WIDGETS_TEXTURE, this.getX(), this.getY(), this.u, this.v + this.height * t, this.width, this.height, EditLetteredConnectionScreen.WIDGETS_TEXTURE_WIDTH, EditLetteredConnectionScreen.WIDGETS_TEXTURE_HEIGHT);
    }
}
