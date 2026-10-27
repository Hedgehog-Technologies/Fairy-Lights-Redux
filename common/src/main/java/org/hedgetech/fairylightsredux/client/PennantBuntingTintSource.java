package org.hedgetech.fairylightsredux.client;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import org.hedgetech.fairylightsredux.server.component.PennantBunting;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record PennantBuntingTintSource(int index) implements ItemTintSource {
    public static final ResourceLocation ID = Constants.id(FLRNames.PENNANT_BUNTING);

    public static final MapCodec<PennantBuntingTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("index").forGetter(PennantBuntingTintSource::index)
    ).apply(instance, PennantBuntingTintSource::new));

    @Override
    public int calculate(final ItemStack stack, final ClientLevel world, final LivingEntity entity) {
        final List<ItemStack> pattern = stack.getOrDefault(FLRDataComponents.PENNANT_BUNTING, PennantBunting.DEFAULT).pattern();
        if (this.index == 0 || pattern.isEmpty()) return -1;

        return ARGB.opaque(DyeableItem.getColor(pattern.get((this.index - 1) % pattern.size())));
    }

    @Override
    public @NotNull MapCodec<PennantBuntingTintSource> type() {
        return MAP_CODEC;
    }
}
