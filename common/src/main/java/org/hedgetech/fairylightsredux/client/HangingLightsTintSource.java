package org.hedgetech.fairylightsredux.client;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
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
import org.hedgetech.fairylightsredux.server.component.HangingLights;
import org.hedgetech.fairylightsredux.util.CalendarEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record HangingLightsTintSource(int index) implements ItemTintSource {
    public static final ResourceLocation ID = Constants.id(FLRNames.HANGING_LIGHTS);

    public static final MapCodec<HangingLightsTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("index").forGetter(HangingLightsTintSource::index)
    ).apply(instance, HangingLightsTintSource::new));

    private static final int DEFAULT_LIGHT_COLOR = 0xFFD584;

    @Override
    public int calculate(final ItemStack stack, final ClientLevel world, final LivingEntity entity) {
        final HangingLights lights = stack.getOrDefault(FLRDataComponents.HANGING_LIGHTS, HangingLights.DEFAULT);

        if (this.index == 0) {
            return ARGB.opaque(lights.string().getColor());
        }

        final List<ItemStack> pattern = lights.pattern();
        if (pattern.isEmpty()) {
            // Patternless lights dress up for the holidays: blinking red and green at Christmas, orange and purple at Halloween
            if (CalendarEvent.CHRISTMAS.isOccurringNow()) {
                return ARGB.opaque((this.index + Util.getMillis() / 2000) % 2 == 0 ? 0x993333 : 0x7FCC19);
            }

            if (CalendarEvent.HALLOWEEN.isOccurringNow()) {
                return ARGB.opaque(this.index % 2 == 0 ? 0xF9801D : 0x8932B8);
            }

            return ARGB.opaque(DEFAULT_LIGHT_COLOR);
        }

        return LightTintSource.getColor(pattern.get((this.index - 1) % pattern.size()));
    }

    @Override
    public @NotNull MapCodec<HangingLightsTintSource> type() {
        return MAP_CODEC;
    }
}
