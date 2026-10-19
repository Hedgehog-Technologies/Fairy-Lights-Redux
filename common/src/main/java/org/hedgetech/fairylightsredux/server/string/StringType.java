package org.hedgetech.fairylightsredux.server.string;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import org.hedgetech.fairylightsredux.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public enum StringType implements StringRepresentable {
    BLACK_STRING("black_string", 0x323232),
    WHITE_STRING("white_string", 0xF0F0F0);

    private static final IntFunction<StringType> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

    public static final Codec<StringType> CODEC = StringRepresentable.fromEnum(StringType::values);
    public static final StreamCodec<ByteBuf, StringType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

    private final String name;
    private final int color;

    StringType(final String name, final int color) {
        this.name = name;
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public String getTranslationKey() {
        return "item." + Constants.MOD_ID + "." + this.name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }
}
