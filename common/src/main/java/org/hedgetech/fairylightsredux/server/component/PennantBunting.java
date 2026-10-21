package org.hedgetech.fairylightsredux.server.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;

import java.util.List;
import java.util.stream.IntStream;

public record PennantBunting(List<ItemStack> pattern, StyledString text) {
    public static final PennantBunting DEFAULT = new PennantBunting(List.of(), StyledString.EMPTY);

    public static final Codec<PennantBunting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.listOf().optionalFieldOf("pattern", List.of()).forGetter(PennantBunting::pattern),
            StyledString.CODEC.optionalFieldOf("text", StyledString.EMPTY).forGetter(PennantBunting::text)
    ).apply(instance, PennantBunting::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PennantBunting> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), PennantBunting::pattern,
            StyledString.STREAM_CODEC, PennantBunting::text,
            PennantBunting::new
    );

    public PennantBunting {
        pattern = pattern.stream().map(ItemStack::copy).toList();
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof PennantBunting(List<ItemStack> pattern1, StyledString text1)
                && this.text.equals(text1)
                && this.pattern.size() == pattern1.size()
                && IntStream.range(0, this.pattern.size()).allMatch(i ->
                    ItemStack.matches(this.pattern.get(i), pattern1.get(i)));
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (ItemStack stack : this.pattern) {
            hash = 31 * hash + ItemStack.hashItemAndComponents(stack);
        }
        return 31 * hash + this.text.hashCode();
    }
}
