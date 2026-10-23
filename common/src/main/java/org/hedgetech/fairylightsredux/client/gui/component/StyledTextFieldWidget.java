package org.hedgetech.fairylightsredux.client.gui.component;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import org.hedgetech.fairylightsredux.client.gui.EditLetteredConnectionScreen;
import org.hedgetech.fairylightsredux.util.styledstring.Style;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.hedgetech.fairylightsredux.util.styledstring.StyledStringBuilder;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class StyledTextFieldWidget extends AbstractWidget {
    private static final int MAX_LENGTH = 32;
    private static final int MULTI_CLICK_INTERVAL = 250;
    private static final int TEXT_COLOR = 0xFFE0E0E0;

    private final Font font;
    private final ColorButton colorBtn;
    private final ToggleButton boldBtn;
    private final ToggleButton italicBtn;
    private final ToggleButton underlineBtn;
    private final ToggleButton strikethroughBtn;

    private StyledString value = StyledString.EMPTY;
    private Style currentStyle;
    private int tick;
    private int lineScrollOffset;
    private int caret;
    private int selectionEnd;
    private boolean isDraggingSelection;
    private boolean hasDraggedSelection;
    private boolean isPressed;
    private long lastClickTime;
    private int multiClicks;

    private UnaryOperator<String> charInputTransformer = UnaryOperator.identity();
    private Consumer<StyledString> changeListener = value -> {};

    public StyledTextFieldWidget(
            final Font font,
            final ColorButton colorBtn,
            final ToggleButton boldBtn,
            final ToggleButton italicBtn,
            final ToggleButton underlineBtn,
            final ToggleButton strikethroughBtn,
            final int x, final int y, final int width, final int height,
            final Component msg
    ) {
        super(x, y, width, height, msg);
        this.font = font;
        this.colorBtn = colorBtn;
        this.boldBtn = boldBtn;
        this.italicBtn = italicBtn;
        this.underlineBtn = underlineBtn;
        this.strikethroughBtn = strikethroughBtn;
        this.setStyle(Style.PLAIN);
    }

    public void updateStyling(final ChatFormatting styling, final boolean state) {
        if (this.caret == this.selectionEnd) {
            this.setStyle(this.currentStyle.withStyling(styling, state));
        } else {
            this.setValue0(this.value.withStyling(Math.min(this.caret, this.selectionEnd), Math.max(this.caret, this.selectionEnd), styling, state));
            this.updateSelectionControls();
        }
    }

    private void setStyle(final Style style) {
        this.currentStyle = style;
        this.colorBtn.setDisplayColor(style.getColor());
        this.boldBtn.setValue(style.isBold());
        this.italicBtn.setValue(style.isItalic());
        this.underlineBtn.setValue(style.isUnderline());
        this.strikethroughBtn.setValue(style.isStrikethrough());
    }

    public void setValue(final StyledString value) {
        this.setValue0(value.length() > MAX_LENGTH ? value.substring(0, MAX_LENGTH) : value);
    }

    private void setValue0(final StyledString value) {
        this.value = value;
        this.changeListener.accept(value);
    }

    public StyledString getValue() {
        return this.value;
    }

    private StyledString getSelectedText() {
        return this.value.substring(Math.min(this.caret, this.selectionEnd), Math.max(this.caret, this.selectionEnd));
    }

    public void setCaretStart() {
        this.setCaret(0);
    }

    private void setCaretEnd() {
        this.setCaret(this.value.length());
    }

    private void setCaret(final int pos) {
        this.setCaret(pos, true);
    }

    private void setCaret(final int pos, final boolean changeColor) {
        this.caret = Mth.clamp(pos, 0, this.value.length());
        this.setSelectionPos(this.caret);
        if (changeColor) this.setCurrentStyleByIndex(this.caret);
        this.tick = 0;
    }

    private void setCurrentStyleByIndex(final int index) {
        if (!this.value.isEmpty()) {
            this.setStyle(this.value.styleAt(index <= 0 ? 0 : index -1));
        }
    }

    public void setCharInputTransformer(final UnaryOperator<String> charInputTransformer) {
        this.charInputTransformer = charInputTransformer;
    }

    public void setChangeListener(final Consumer<StyledString> listener) {
        this.changeListener = listener;
        listener.accept(this.value);
    }

    private int getInnerWidth() {
        return this.width - 8;
    }

    private void setSelectionPos(final int pos) {
        final int len = this.value.length();
        this.selectionEnd = Mth.clamp(pos, 0, len);
        if (this.lineScrollOffset > len) this.lineScrollOffset = len;

        final int w = trimToWidth(this.value.substring(this.lineScrollOffset), this.font, this.getInnerWidth(), true).length();

        if (this.selectionEnd > w + this.lineScrollOffset) {
            this.lineScrollOffset = this.selectionEnd - w;
        } else if (this.selectionEnd <= this.lineScrollOffset) {
            this.lineScrollOffset = this.selectionEnd;
        }

        this.lineScrollOffset = Mth.clamp(this.lineScrollOffset, 0, len);

        if (this.caret != this.selectionEnd) {
            this.updateSelectionControls();
        }
    }

    private void updateSelectionControls() {
        final StyledString selected = this.getSelectedText();
        ChatFormatting color = null;
        boolean consistentColor = true;
        boolean bold = true, italic = true, underline = true, strikethrough = true;

        for (int i = 0; i < selected.length(); i++) {
            final Style s = selected.styleAt(i);

            if (color != null && color != s.getColor()) {
                color = null;
                consistentColor = false;
            }

            if (consistentColor) color = s.getColor();

            bold &= s.isBold();
            italic &= s.isItalic();
            underline &= s.isUnderline();
            strikethrough &= s.isStrikethrough();
        }

        this.setStyle(new Style(color == null ? this.currentStyle.getColor() : color, bold, strikethrough, underline, italic, false));

        if (!consistentColor) this.colorBtn.removeDisplayColor();
    }

    public void tick() {
        this.tick++;
    }

    @Override
    public void mouseMoved(final double x, final double y) {
        final int mouseX = (int) x;
        if (!this.isPressed || this.caret == this.selectionEnd) return;

        boolean scrolled = false;
        if (mouseX < this.getX() + 15) {
            if (this.lineScrollOffset > 0) {
                final int rate = (2 - (mouseX - this.getX()) / 5) * 2 + 2;
                this.lineScrollOffset = Math.max(this.lineScrollOffset - rate, 0);
                scrolled = true;
            }
        } else if (mouseX > this.getX() + this.width - 16) {
            final int max = this.value.length() - trimToWidth(this.value, this.font, this.getInnerWidth(), true).length();

            if (this.lineScrollOffset < max) {
                final int rate = (2 + (mouseX - this.getX() - this.width + 1) / 5) * 2 + 2;
                this.lineScrollOffset = Math.min(this.lineScrollOffset + rate, max);
                scrolled = true;
            }
        }

        if (scrolled && !this.hasDraggedSelection) {
            this.setSelectionPos(this.getIndexInTextByX(mouseX - this.getX() - 2));
        }
    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
        if (Screen.isSelectAll(keyCode)) {
            this.setCaretEnd();
            this.setSelectionPos(0);
        } else if (Screen.isCopy(keyCode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText().toString());
        } else if (Screen.isPaste(keyCode)) {
            final StyledString str = this.getClipboardString();

            if (Screen.hasShiftDown()) this.writeText(str.toString());
            else this.writeText(str, true);
        } else if (Screen.isCut(keyCode)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getSelectedText().toString());
            this.writeText("");
        } else if (EditLetteredConnectionScreen.isControlOp(keyCode, GLFW.GLFW_KEY_BACKSLASH)) {
            if (this.caret == this.selectionEnd) {
                this.setStyle(Style.PLAIN);
                this.tick = 0;
            } else {
                this.setValue0(this.value.withStyling(Math.min(this.caret, this.selectionEnd), Math.max(this.caret, this.selectionEnd), Style.PLAIN));
            }
        } else {
            switch (keyCode) {
                case GLFW.GLFW_KEY_BACKSPACE -> {
                    if (Screen.hasControlDown()) {
                        this.deleteWords(-1);
                    } else {
                        this.deleteFromCursor(-1);
                    }
                }

                case GLFW.GLFW_KEY_DELETE -> {
                    if (Screen.hasControlDown()) {
                        this.deleteWords(1);
                    } else {
                        this.deleteFromCursor(1);
                    }
                }

                case GLFW.GLFW_KEY_HOME -> {
                    if (Screen.hasShiftDown()) {
                        this.setSelectionPos(0);
                    } else {
                        this.setCaretStart();
                    }
                }

                case GLFW.GLFW_KEY_END -> {
                    if (Screen.hasShiftDown()) {
                        this.setSelectionPos(this.value.length());
                    } else {
                        this.setCaretEnd();
                    }
                }

                case GLFW.GLFW_KEY_LEFT -> this.moveHorizontally(-1);
                case GLFW.GLFW_KEY_RIGHT -> this.moveHorizontally(1);

                default -> { return false; }
            }
        }

        return true;
    }

    private void moveHorizontally(final int dir) {
        if (Screen.hasShiftDown()) {
            this.setSelectionPos(Screen.hasControlDown() ? this.skipWords(dir, this.selectionEnd) : this.selectionEnd + dir);
        } else if (Screen.hasControlDown()) {
            this.setCaret(this.skipWords(dir));
        } else if (this.caret == this.selectionEnd) {
            this.moveCursorBy(dir, true);
        } else {
            this.setCaret(dir < 0
                    ? Math.min(this.caret, this.selectionEnd)
                    : Math.max(this.caret, this.selectionEnd));
        }
    }

    @Override
    public boolean charTyped(final char typedChar, final int modifiers) {
        if (StringUtil.isAllowedChatCharacter(typedChar)) {
            this.writeText(this.charInputTransformer.apply(Character.toString(typedChar)));
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
        if (!this.isMouseOver(mouseX, mouseY)) {
            this.setCaret(Math.min(this.caret, this.selectionEnd));
            return false;
        }

        if (button != 0) return false;

        final int idx = this.getIndexInTextByX(Mth.floor(mouseX - this.getX()) - 2);
        final long now = Util.getMillis();

        if (now - this.lastClickTime <= MULTI_CLICK_INTERVAL) {
            this.multiClicks = this.multiClicks % 3 + 1;
        } else {
            this.multiClicks = 1;
        }

        this.lastClickTime = now;

        if (Screen.hasShiftDown()) {
            final int end = this.selectionEnd;
            this.setCaret(idx);
            this.setSelectionPos(end);
        } else {
            this.clickIndex(idx);
        }

        this.isPressed = true;
        return true;
    }

    private void clickIndex(final int pos) {
        switch (this.multiClicks) {
            case 1 -> {
                final int start = Math.min(this.caret, this.selectionEnd);
                final int end = Math.max(this.caret, this.selectionEnd);
                this.isDraggingSelection = this.caret != this.selectionEnd && pos >= start && pos < end;

                if (!this.isDraggingSelection) {
                    this.setCaret(pos);
                    this.hasDraggedSelection = false;
                }
            }

            case 2 -> {
                if (pos < this.value.length() && this.value.charAt(pos) == ' ') {
                    int low = pos;
                    while (low > 0 && this.value.charAt(low - 1) == ' ') {
                        low--;
                    }

                    int high = pos;
                    while (high < this.value.length() && this.value.charAt(high) == ' ') {
                        high++;
                    }

                    this.setCaret(high);
                    this.setSelectionPos(low);
                } else {
                    final int low = this.value.lastIndexOf(' ', pos);
                    final int high = this.value.indexOf(' ', pos);
                    this.setCaret(high == -1 ? this.value.length() : high);
                    this.setSelectionPos(low + 1);
                }
            }

            default -> {
                this.setCaretEnd();
                this.setSelectionPos(0);
            }
        }
    }

    @Override
    public boolean mouseReleased(final double mouseX, final double mouseY, final int button) {
        if (button != 0) return false;

        this.isPressed = false;
        if (!this.isDraggingSelection) return false;

        if (this.isMouseOver(mouseX, mouseY)) {
            int pos = this.getIndexInTextByX(Mth.floor(mouseX - this.getX()) - 2);

            if ((pos - this.caret) * (pos - this.selectionEnd) > 0) {
                final StyledString selection = this.getSelectedText();
                this.writeText("");

                if ((pos - this.caret) - (this.selectionEnd - pos) > 0) {
                    pos -= selection.length();
                }

                this.setCaret(pos);
                this.writeText(selection, true);
                this.setCaret(pos);
                this.setSelectionPos(pos + selection.length());
            } else {
                this.setCaret(pos);
            }
        }

        this.isDraggingSelection = false;
        this.hasDraggedSelection = false;

        return true;
    }

    @Override
    public boolean mouseDragged(final double mouseX, final double mouseY, final int button, final double dx, final double dy) {
        if (!this.isPressed || button != 0) return false;

        if (this.isDraggingSelection) {
            this.hasDraggedSelection = true;
        } else {
            this.setSelectionPos(this.getIndexInTextByX(Mth.floor(mouseX - this.getX()) - 2));
        }

        return true;
    }

    private void writeText(final String input) {
        if (!this.colorBtn.hasDisplayColor()) {
            this.setCurrentStyleByIndex(Math.min(this.selectionEnd, this.caret));
        }

        this.writeText(new StyledString(input, this.currentStyle), false);
    }

    private void writeText(final StyledString input, final boolean changeColor) {
        final StyledString filtered = filterAllowedCharacters(input);
        final int start = Math.min(this.caret, this.selectionEnd);
        final int end = Math.max(this.caret, this.selectionEnd);
        final int length = Math.min(MAX_LENGTH - this.value.length() + end - start, filtered.length());
        final StyledStringBuilder val = new StyledStringBuilder()
                .append(this.value.substring(0, start))
                .append(filtered.substring(0, length))
                .append(this.value.substring(end));
        this.setValue0(val.toStyledString());
        this.moveCursorBy(start - this.selectionEnd + length, changeColor);
    }

    private static StyledString filterAllowedCharacters(final StyledString str) {
        final StyledStringBuilder builder = new StyledStringBuilder();

        for (int i = 0; i < str.length(); i++) {
            final char chr = str.charAt(i);

            if (chr >= ' ' && chr != '\u007F') {
                builder.append(chr, str.styleAt(i));
            }
        }

        return builder.toStyledString();
    }

    private void deleteWords(final int num) {
        this.deleteFromCursor(this.skipWords(num) - this.caret);
    }

    private void deleteFromCursor(final int num) {
        if (this.value.isEmpty()) return;

        if (this.selectionEnd != this.caret) {
            this.writeText("");
            return;
        }

        final boolean reverse = num < 0;
        final int from = reverse ? this.caret + num : this.caret;
        final int to = reverse ? this.caret : this.caret + num;
        final StyledStringBuilder val = new StyledStringBuilder();
        Style style = null;

        if (from > 0) {
            if (from < this.value.length()) {
                style = this.value.styleAt(from);
            }

            val.append(this.value.substring(0, from));
        }

        if (to < this.value.length()) {
            if (to > 0 && style == null) {
                style = this.value.styleAt(from);
            }

            val.append(this.value.substring(0, from));
        }

        this.setValue0(val.toStyledString());
        if (reverse) this.moveCursorBy(num, false);
        this.setStyle(style == null ? this.currentStyle : style);
    }

    private int skipWords(final int num) {
        return this.skipWords(num, num < 0 ? Math.min(this.selectionEnd, this.caret) : Math.max(this.selectionEnd, this.caret));
    }

    private int skipWords(final int num, final int pos) {
        int idx = pos;
        final int len = this.value.length();

        for (int word = 0; word < Math.abs(num); word++) {
            if (num < 0) {
                while (idx > 0 && this.value.charAt(idx - 1) == ' ') {
                    idx--;
                }

                while (idx > 0 && this.value.charAt(idx - 1) != ' ') {
                    idx--;
                }
            } else {
                while (idx < len && this.value.charAt(idx) == ' ') {
                    idx++;
                }

                while (idx < len && this.value.charAt(idx) != ' ') {
                    idx++;
                }
            }
        }

        return idx;
    }

    private void moveCursorBy(final int num, final boolean changeColor) {
        this.setCaret(this.selectionEnd + num, changeColor);
    }

    private int getIndexInTextByX(final int x) {
        final StyledString visible = trimToWidth(this.value.substring(this.lineScrollOffset), this.font, this.getInnerWidth(), false);
        return trimToWidth(visible, this.font, x, false).length() + this.lineScrollOffset;
    }

    @Override
    protected void renderWidget(final GuiGraphics graphics, final int mouseX, final int mouseY, final float delta) {
        graphics.fill(this.getX() - 1, this.getY() - 1, this.getX() + this.width + 1, this.getY() + this.height + 1, 0xAAA0A0A0);
        graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0xFF000000);

        final int visibleCaret = this.caret - this.lineScrollOffset;
        final StyledString visibleText = trimToWidth(this.value.substring(this.lineScrollOffset), this.font, this.getInnerWidth(), false);
        final int visibleSelectionEnd = Math.min(this.selectionEnd - this.lineScrollOffset, visibleText.length());
        final boolean isCaretVisible = visibleCaret >= 0 && visibleCaret <= visibleText.length();
        final boolean drawSelection = visibleSelectionEnd != visibleCaret;
        final int offsetX = this.getX() + 4;
        final int offsetY = this.getY() + (this.height - 8) / 2;
        final int caretColor = ARGB.opaque(this.currentStyle.getColor().getColor());
        graphics.drawString(this.font, visibleText.toTextComponent(), offsetX, offsetY, TEXT_COLOR, true);
        final int caretX;

        if (isCaretVisible) {
            caretX = offsetX + getWidth(visibleText.substring(0, visibleCaret), this.font);
        } else {
            caretX = visibleCaret > 0 ? offsetX + this.width - 6 : offsetX;
        }

        if (!drawSelection && this.tick / 6 % 2 == 0 && isCaretVisible) {
            graphics.fill(caretX, offsetY - 2, caretX + 1, offsetY + 1 + this.font.lineHeight, caretColor);
        }

        if (drawSelection) {
            final int selectionX = offsetX + (visibleSelectionEnd < 0 ? 0 : getWidth(visibleText.substring(0, visibleSelectionEnd), this.font));
            final int right = this.getX() + this.width;
            graphics.fill(Math.min(Math.min(caretX, selectionX) - 1, right), offsetY - 2, Math.min(Math.max(caretX, selectionX), right), offsetY + 1 + this.font.lineHeight, 0x33FFFFFF);
        }

        if (this.hasDraggedSelection) {
            if (this.isHovered) {
                final int pos = this.getIndexInTextByX(mouseX - this.getX() - 2) - this.lineScrollOffset;

                if (pos >= 0 && pos <= visibleText.length()) {
                    final int x = offsetX + getWidth(visibleText.substring(0, pos), this.font);
                    graphics.fill(x, offsetY - 2, x + 1, offsetY + 1 + this.font.lineHeight, caretColor);
                }
            }

            graphics.drawString(this.font, this.getSelectedText().toTextComponent(), mouseX + 5, mouseY + 5, 0xBFE0E0E0, true);
        }
    }

    private StyledString getClipboardString() {
        final String str = Minecraft.getInstance().keyboardHandler.getClipboard();

        if (str.indexOf(ChatFormatting.PREFIX_CODE) == -1) {
            return new StyledString(str, this.currentStyle);
        }

        return StyledString.valueOf(str);
    }

    @Override
    protected @NotNull MutableComponent createNarrationMessage() {
        return Component.translatable("gui.narrate.editBox", this.getMessage(), this.value.toString());
    }

    @Override
    protected void updateWidgetNarration(final NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, Component.translatable("narration.edit_box", this.value.toString()));
    }

    private static int getWidth(final StyledString str, final Font font) {
        int w = 0;

        for (int i = 0; i < str.length(); i++) {
            w += font.width(Character.toString(str.charAt(i)));
            if (str.styleAt(i).isBold()) w++;
        }

        return w;
    }

    private static StyledString trimToWidth(final StyledString str, final Font font, final int width, final boolean reverse) {
        final StyledStringBuilder builder = new StyledStringBuilder();
        final int len = str.length();
        int w = 0;

         for (int i = reverse ? len - 1 : 0; i >= 0 && i < len; i += reverse ? -1 : 1) {
             w += font.width(Character.toString(str.charAt(i)));

             if (str.styleAt(i).isBold()) w++;
             if (w > width) break;

             if (reverse) {
                 builder.prepend(str.substring(i, i + 1));
             } else {
                 builder.append(str.substring(i, i + 1));
             }
         }

         return builder.toStyledString();
    }
}
