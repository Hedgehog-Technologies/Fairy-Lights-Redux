package org.hedgetech.fairylightsredux.util.styledstring;

import net.minecraft.ChatFormatting;

public final class Style {
    public static final Style PLAIN = new Style(ChatFormatting.WHITE.getId());

    private static final int COLOR_MASK = 0xF;
    private static final int OBFUSCATED_MASK = 0x10;
    private static final int BOLD_MASK = 0x20;
    private static final int STRIKETHROUGH_MASK = 0x40;
    private static final int UNDERLINE_MASK = 0x80;
    private static final int ITALIC_MASK = 0x100;
    private static final int FANCY_MASK = OBFUSCATED_MASK | BOLD_MASK | STRIKETHROUGH_MASK | UNDERLINE_MASK | ITALIC_MASK;

    private final int value;

    public Style(final ChatFormatting color, final boolean isBold, final boolean isStrikethrough, final boolean isUnderline, final boolean isItalic, final boolean isObfuscated) {
        int value = color.getId();

        if (isObfuscated) value |= OBFUSCATED_MASK;
        if (isBold) value |= BOLD_MASK;
        if (isStrikethrough) value |= STRIKETHROUGH_MASK;
        if (isUnderline) value |= UNDERLINE_MASK;
        if (isItalic) value |= ITALIC_MASK;

        this.value = value;
    }

    public Style(final int value) {
        this.value = value & (COLOR_MASK | FANCY_MASK);
    }

    public int packed() {
        return this.value;
    }

    public ChatFormatting getColor() {
        return ChatFormatting.getById(this.value & COLOR_MASK);
    }

    public boolean isObfuscated() {
        return (this.value & OBFUSCATED_MASK) != 0;
    }

    public boolean isBold() {
        return (this.value & BOLD_MASK) != 0;
    }

    public boolean isStrikethrough() {
        return (this.value & STRIKETHROUGH_MASK) != 0;
    }

    public boolean isUnderline() {
        return (this.value & UNDERLINE_MASK) != 0;
    }

    public boolean isItalic() {
        return (this.value & ITALIC_MASK) != 0;
    }

    public boolean isPlain() {
        return (this.value & FANCY_MASK) == 0;
    }

    public Style withColor(final ChatFormatting color) {
        return new Style(color.getId() | this.value & FANCY_MASK);
    }

    public Style withStyling(final ChatFormatting formatting, final boolean state) {
        if (formatting.isColor()) return this.withColor(formatting);

        final int mask = switch (formatting) {
            case OBFUSCATED -> OBFUSCATED_MASK;
            case BOLD -> BOLD_MASK;
            case STRIKETHROUGH -> STRIKETHROUGH_MASK;
            case UNDERLINE -> UNDERLINE_MASK;
            case ITALIC -> ITALIC_MASK;
            default -> throw new IllegalArgumentException("Invalid fancy formatting: " + formatting.getName());
        };

        return new Style(state ? this.value | mask : this.value & ~mask);
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof final Style other
                && this.value == other.value;
    }
}
