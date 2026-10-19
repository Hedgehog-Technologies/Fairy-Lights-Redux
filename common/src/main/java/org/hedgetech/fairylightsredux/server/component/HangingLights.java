package org.hedgetech.fairylightsredux.server.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.server.string.StringType;

import java.util.List;
import java.util.stream.IntStream;

public record HangingLights(StringType string, List<ItemStack> pattern) {
    public static final HangingLights DEFAULT = new HangingLights(StringType.BLACK_STRING, List.of());

    public static final Codec<HangingLights> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            StringType.CODEC.optionalFieldOf("string", StringType.BLACK_STRING).forGetter(HangingLights::string),
            ItemStack.CODEC.listOf().optionalFieldOf("pattern", List.of()).forGetter(HangingLights::pattern)
    ).apply(instance, HangingLights::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, HangingLights> STREAM_CODEC = StreamCodec.composite(
            StringType.STREAM_CODEC, HangingLights::string,
            ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()), HangingLights::pattern,
            HangingLights::new
    );

    public HangingLights {
        pattern = pattern.stream().map(ItemStack::copy).toList();
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof HangingLights(StringType string1, List<ItemStack> pattern1)
                && this.string == string1
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
        return 31 * this.string.hashCode() + hash;
    }
}
