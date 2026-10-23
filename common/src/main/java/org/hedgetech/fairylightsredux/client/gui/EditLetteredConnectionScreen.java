package org.hedgetech.fairylightsredux.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.client.gui.component.ColorButton;
import org.hedgetech.fairylightsredux.client.gui.component.PaletteButton;
import org.hedgetech.fairylightsredux.client.gui.component.StyledTextFieldWidget;
import org.hedgetech.fairylightsredux.client.gui.component.ToggleButton;
import org.hedgetech.fairylightsredux.network.EditLetteredConnectionPayload;
import org.hedgetech.fairylightsredux.platform.Services;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.connection.ILettered;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.hedgetech.fairylightsredux.util.styledstring.StylingPresence;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public final class EditLetteredConnectionScreen extends Screen {
    public static final ResourceLocation WIDGETS_TEXTURE = Constants.id("textures/gui/widgets.png");

    private final Connection connection;
    private final ILettered lettered;

    private StyledTextFieldWidget textField;
    private Button doneBtn;
    private Button cancelBtn;
    private ColorButton colorBtn;
    private ToggleButton boldBtn;
    private ToggleButton italicBtn;
    private ToggleButton underlineBtn;
    private ToggleButton strikethroughBtn;
    private PaletteButton paletteBtn;

    private EditLetteredConnectionScreen(final Connection connection, final ILettered lettered) {
        super(Component.translatable("gui.fairylightsredux.edit_lettered_connection"));
        this.connection = connection;
        this.lettered = lettered;
    }

    public static void open(final Connection connection) {
        if (connection instanceof final ILettered lettered) {
            Minecraft.getInstance().setScreen(new EditLetteredConnectionScreen(connection, lettered));
        }
    }

    @Override
    public void init() {
        final int pad = 4;
        final int buttonWidth = 150;
        final int textFieldX = this.width / 2 - 150;
        final int textFieldY = this.height / 2 - 10;
        final int actionY = Math.max(this.height / 4 + 120 + 12, textFieldY + 44);

        this.doneBtn = this.addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> {
            Services.PLATFORM.sendToServer(new EditLetteredConnectionPayload(this.connection.saveFastener(), this.connection.getUUID(), this.textField.getValue()));
            this.onClose();
        }).pos(this.width / 2 - pad - buttonWidth, actionY).size(buttonWidth, 20).build());

        this.cancelBtn = this.addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b -> this.onClose())
                .pos(this.width / 2 + pad, actionY).size(buttonWidth, 20).build());

        int buttonX = textFieldX;
        final int buttonY = textFieldY - 25;
        final int bInc = 24;

        this.colorBtn = this.addRenderableWidget(new ColorButton(buttonX, buttonY, Component.empty(), b -> this.paletteBtn.visible = !this.paletteBtn.visible));
        this.paletteBtn = this.addRenderableWidget(new PaletteButton(buttonX - 4, buttonY - 30, this.colorBtn, Component.translatable("gui.fairylightsredux.color"), b -> this.textField.updateStyling(this.colorBtn.getDisplayColor(), true)));
        this.boldBtn = this.addRenderableWidget(new ToggleButton(buttonX += bInc, buttonY, 40, 0, Component.empty(), b -> this.updateStyleButton(ChatFormatting.BOLD, this.boldBtn)));
        this.italicBtn = this.addRenderableWidget(new ToggleButton(buttonX += bInc, buttonY, 60, 0, Component.empty(), b -> this.updateStyleButton(ChatFormatting.ITALIC, this.italicBtn)));
        this.underlineBtn = this.addRenderableWidget(new ToggleButton(buttonX += bInc, buttonY, 80, 0, Component.empty(), b -> this.updateStyleButton(ChatFormatting.UNDERLINE, this.underlineBtn)));
        this.strikethroughBtn = this.addRenderableWidget(new ToggleButton(buttonX += bInc, buttonY, 100, 0, Component.empty(), b -> this.updateStyleButton(ChatFormatting.STRIKETHROUGH, this.strikethroughBtn)));
        this.textField = this.addRenderableWidget(new StyledTextFieldWidget(this.font, this.colorBtn, this.boldBtn, this.italicBtn, this.underlineBtn, this.strikethroughBtn, textFieldX, textFieldY, 300, 20, Component.translatable("gui.fairylightsredux.lettered_text")));

        this.textField.setValue(this.lettered.getText());
        this.textField.setCaretStart();
        this.textField.setChangeListener(this::validateText);
        this.textField.setCharInputTransformer(this.lettered.getInputTransformer());
        this.paletteBtn.visible = false;

        final StylingPresence styling = this.lettered.getSupportedStyling();
        this.colorBtn.visible = styling.hasColor();
        this.boldBtn.visible = styling.hasBold();
        this.italicBtn.visible = styling.hasItalic();
        this.underlineBtn.visible = styling.hasUnderline();
        this.strikethroughBtn.visible = styling.hasStrikethrough();
        this.setInitialFocus(this.textField);
    }

    private void validateText(final StyledString text) {
        this.doneBtn.active = this.lettered.isSupportedText(text) && !this.lettered.getText().equals(text);
    }

    @Override
    public void mouseMoved(final double x, final double y) {
        this.textField.mouseMoved(x, y);
    }

    @Override
    public void tick() {
        this.textField.tick();
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        this.paletteBtn.visible = false;

        if (isControlOp(keyCode, GLFW.GLFW_KEY_B)) {
            this.toggleStyleButton(ChatFormatting.BOLD, this.boldBtn);
            return true;
        }

        if (isControlOp(keyCode, GLFW.GLFW_KEY_I)) {
            this.toggleStyleButton(ChatFormatting.ITALIC, this.italicBtn);
            return true;
        }

        if (isControlOp(keyCode, GLFW.GLFW_KEY_U)) {
            this.toggleStyleButton(ChatFormatting.UNDERLINE, this.underlineBtn);
            return true;
        }

        if (isControlOp(keyCode, GLFW.GLFW_KEY_S)) {
            this.toggleStyleButton(ChatFormatting.STRIKETHROUGH, this.strikethroughBtn);
            return true;
        }

        if (this.textField.keyPressed(keyCode, scanCode, modifiers)) return true;

        if ((keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) && this.doneBtn.active) {
            this.doneBtn.onPress();
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.cancelBtn.onPress();
            return true;
        }

        return false;
    }

    @Override
    public boolean charTyped(final char typedChar, final int modifiers) {
        return this.textField.charTyped(typedChar, modifiers);
    }

    private void toggleStyleButton(final ChatFormatting styling, final ToggleButton btn) {
        btn.setValue(!btn.getValue());
        this.updateStyleButton(styling, btn);
    }

    private void updateStyleButton(final ChatFormatting styling, final ToggleButton btn) {
        if (btn.visible) {
            this.textField.updateStyling(styling, btn.getValue());
        }
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;

        this.paletteBtn.visible = false;
        return false;
    }

    @Override
    public void render(final @NotNull GuiGraphics graphics, final int mouseX, final int mouseY, final float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFFFF);

        final String allowed = this.lettered.getAllowedDescription();
        if (!allowed.isEmpty()) {
            graphics.drawString(this.font, Component.translatable("gui.fairylightsredux.edit_lettered_connection.allowed_characters", allowed).withStyle(ChatFormatting.GRAY), this.textField.getX(), this.textField.getY() + 24, 0xFFFFFFFF);
        }
    }

    public static boolean isControlOp(final int key, final int controlKey) {
        return key == controlKey
                && Screen.hasControlDown()
                && !Screen.hasShiftDown()
                && !Screen.hasAltDown();
    }
}
