package org.hedgetech.fairylightsredux.util.styledstring;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Arrays;
import java.util.List;

public final class StyledString {
    public static final StyledString EMPTY = new StyledString("", Style.PLAIN);

    public static final Codec<StyledString> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("value", "").forGetter(StyledString::toString),
            Codec.INT.listOf().optionalFieldOf("styling", List.of()).forGetter(StyledString::getPackedStyling)
    ).apply(instance, StyledString::fromPacked));

    public static final StreamCodec<ByteBuf, StyledString> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, StyledString::toString,
            ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()), StyledString::getPackedStyling,
            StyledString::fromPacked
    );

    private final String value;
    private final Style[] styling;

    public StyledString(final String value, final Style style) {
        this.value = value;
        this.styling = new Style[value.length()];
        Arrays.fill(this.styling, style);
    }

    public StyledString(final String value, final Style[] styling) {
        this.value = value;
        this.styling = styling;
    }

    private static StyledString fromPacked(final String value, final List<Integer> packed) {
        final Style[] styling = new Style[value.length()];

        for (int i = 0; i < styling.length; i++) {
            styling[i] = i < packed.size()
                    ? new Style(packed.get(i))
                    : Style.PLAIN;
        }

        return new StyledString(value, styling);
    }

    private List<Integer> getPackedStyling() {
        return Arrays.stream(this.styling)
                .map(Style::packed)
                .toList();
    }

    public int length() {
        return this.value.length();
    }

    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    public char charAt(final int index) {
        return this.value.charAt(index);
    }

    public Style styleAt(final int index) {
        return this.styling[index];
    }

    public int indexOf(final char chr, final int fromIndex) {
        return this.value.indexOf(chr, fromIndex);
    }

    public int lastIndexOf(final char chr, final int fromIndex) {
        return this.value.indexOf(chr, fromIndex);
    }

    public StyledString substring(final int beginIndex) {
        return this.substring(beginIndex, this.length());
    }

    public StyledString substring(final int beginIndex, final int endIndex) {
        return new StyledString(this.value.substring(beginIndex, endIndex), Arrays.copyOfRange(this.styling, beginIndex, endIndex));
    }

    public StyledString withStyling(final int beginIndex, final int endIndex, final Style style) {
        final Style[] styling = this.styling.clone();
        Arrays.fill(styling, beginIndex, endIndex, style);
        return new StyledString(this.value, styling);
    }

    public StyledString withStyling(final int beginIndex, final int endIndex, final ChatFormatting formatting, final boolean state) {
        final Style[] styling = this.styling.clone();
        for (int i = beginIndex; i < endIndex; i++) {
            styling[i] = styling[i].withStyling(formatting, state);
        }

        return new StyledString(this.value, styling);
    }

    public Component toTextComponent() {
        final MutableComponent text = Component.empty();
        int start = 0;

        for (int i = 1; i <= this.length(); i++) {
            if (i == this.length() || !this.styling[i].equals(this.styling[start])) {
                final Style style = this.styling[start];
                final MutableComponent part = Component.literal(this.value.substring(start, i)).withStyle(style.getColor());
                if (style.isObfuscated()) part.withStyle(ChatFormatting.OBFUSCATED);
                if (style.isBold()) part.withStyle(ChatFormatting.BOLD);
                if (style.isStrikethrough()) part.withStyle(ChatFormatting.STRIKETHROUGH);
                if (style.isUnderline()) part.withStyle(ChatFormatting.UNDERLINE);
                if (style.isItalic()) part.withStyle(ChatFormatting.ITALIC);
                text.append(part);
                start = i;
            }
        }

        return text;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof final StyledString other
                && this.value.equals(other.value)
                && Arrays.equals(this.styling, other.styling);
    }

    @Override
    public int hashCode() {
        return 31 * this.value.hashCode() + Arrays.hashCode(this.styling);
    }

    public static StyledString valueOf(final String str) {
        final StyledStringBuilder builder = new StyledStringBuilder();
        Style style = Style.PLAIN;
        boolean hasFToken = false;

        for (final char chr : str.toCharArray()) {
            if (chr == ChatFormatting.PREFIX_CODE) {
                hasFToken = true;
            } else if (hasFToken) {
                final ChatFormatting formatting = ChatFormatting.getByCode(chr);

                if (formatting == ChatFormatting.RESET) {
                    style = Style.PLAIN;
                } else if (formatting != null) {
                    style = style.withStyling(formatting, true);
                } else {
                    builder.append(chr, style);
                }

                hasFToken = false;
            } else {
                builder.append(chr, style);
            }
        }

        return builder.toStyledString();
    }
}
