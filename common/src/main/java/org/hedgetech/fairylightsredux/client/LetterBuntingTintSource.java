package org.hedgetech.fairylightsredux.client;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.FLRNames;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.jetbrains.annotations.NotNull;

public record LetterBuntingTintSource(int index) implements ItemTintSource {
    public static final ResourceLocation ID = Constants.id(FLRNames.LETTER_BUNTING);

    public static final MapCodec<LetterBuntingTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("index").forGetter(LetterBuntingTintSource::index)
    ).apply(instance, LetterBuntingTintSource::new));

    @Override
    public int calculate(final ItemStack stack, final ClientLevel world, final LivingEntity entity) {
        final StyledString text = stack.getOrDefault(FLRDataComponents.LETTER_BUNTING, StyledString.EMPTY);
        if (this.index == 0 || text.isEmpty()) return -1;

        int n = (this.index - 1) % text.length();
        ChatFormatting lastColor = null;
        ChatFormatting color = null;

        for (int i = 0; i < text.length(); lastColor = color, i++) {
            color = text.styleAt(i).getColor();
            if (lastColor != color && n-- == 0) break;
        }

        return ARGB.opaque(color.getColor());
    }

    @Override
    public @NotNull MapCodec<LetterBuntingTintSource> type() {
        return MAP_CODEC;
    }
}
