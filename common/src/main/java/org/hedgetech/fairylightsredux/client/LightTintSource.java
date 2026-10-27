package org.hedgetech.fairylightsredux.client;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.FLRNames;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.feature.light.ColorChangingBehavior;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record LightTintSource() implements ItemTintSource {
    public static final ResourceLocation ID = Constants.id(FLRNames.LIGHT);
    public static final MapCodec<LightTintSource> MAP_CODEC = MapCodec.unit(new LightTintSource());

    @Override
    public int calculate(final @NotNull ItemStack stack, final ClientLevel world, final LivingEntity entity) {
        return getColor(stack);
    }

    public static int getColor(final ItemStack stack) {
        if (ColorChangingBehavior.exists(stack)) {
            return ARGB.opaque(ColorChangingBehavior.animate(Objects.requireNonNull(stack.get(FLRDataComponents.COLORS))));
        }

        return ARGB.opaque(DyeableItem.getColor(stack));
    }

    @Override
    public @NotNull MapCodec<LightTintSource> type() {
        return MAP_CODEC;
    }
}
