package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.server.datacomponent.ColorComponent;

import java.util.Objects;
import java.util.Optional;

public final class DyeableItem {
    public static Component getColorName(final int color) {
        ColorDistance colorDist = getColor(color);
        final Component colorName = Component.translatable("color.fairylightsredux." + colorDist.getColor().getName());
        return colorDist.getDistance() == 0 ? colorName : Component.translatable("format.fairylightsredux.dyed_colored", colorName);
    }

    private static ColorDistance getColor(final int color) {
        final int r = color >> 16 & 0xFF;
        final int g = color >> 8 & 0xFF;
        final int b = color & 0xFF;
        DyeColor closest = DyeColor.WHITE;
        int closestDist = Integer.MAX_VALUE;
        for (final DyeColor dye : DyeColor.values()) {
            final int dyeColor = getColor(dye);
            if (dyeColor == color) {
                closest = dye;
                closestDist = 0;
                break;
            }
            final int dr = dyeColor >> 16 & 0xFF;
            final int dg = dyeColor >> 8 & 0xFF;
            final int db = dyeColor & 0xFF;
            final int dist = (dr - r) * (dr - r) + (dg - g) * (dg - g) + (db - b) * (db - b);
            if (dist < closestDist) {
                closest = dye;
                closestDist = dist;
            }
        }
        return new ColorDistance(closest, closestDist);
    }

    private static class ColorDistance {
        DyeColor color;
        int distance;

        ColorDistance(DyeColor color, int distance) {
            this.color = color;
            this.distance = distance;
        }

        public DyeColor getColor() {
            return color;
        }

        public int getDistance() {
            return distance;
        }
    }

    public static Component getDisplayName(final ItemStack stack, final Component name) {
        return Component.translatable("format.fairylights.colored", getColorName(getColor(stack)), name);
    }

    public static int getColor(final DyeColor color) {
        if (color == DyeColor.BLACK) {
            return 0x323232;
        }
        if (color == DyeColor.GRAY) {
            return 0x606060;
        }
        return color.getTextureDiffuseColor();
    }

    public static int getColor(final ItemStack stack) {
        return getColor(stack.getComponents());
    }

    public static int getColor(final DataComponentMap map) {
        Optional<DyeColor> dyeColor = getDyeColor(map);
        return dyeColor.map(DyeableItem::getColor).orElse(0xFFFFFF);
    }

    public static Optional<DyeColor> getDyeColor(final ItemStack stack) {
        return getDyeColor(stack.getComponents());
    }

    public static Optional<DyeColor> getDyeColor(final DataComponentMap map) {
        if (map.has(ColorComponent.TYPE)) {
            return Optional.of(Objects.requireNonNull(map.get(ColorComponent.TYPE)).color());
        }
        return Optional.empty();
    }

    public static ItemStack setColor(final ItemStack stack, final DyeColor color) {
        stack.set(ColorComponent.TYPE, new ColorComponent(color));
        return stack;
    }

    public static ItemStack setColor(final ItemStack stack, final int color) {
        ColorDistance colorDist = getColor(color);
        return setColor(stack, colorDist.getColor());
    }

    public static DataComponentMap setColor(final DataComponentMap map, final DyeColor color) {
        return DataComponentMap.builder().addAll(map).set(ColorComponent.TYPE, new ColorComponent(color)).build();
    }

    public static DataComponentMap setColor(final DataComponentMap map, final int color) {
        ColorDistance colorDist = getColor(color);
        return setColor(map, colorDist.getColor());
    }

    private DyeableItem() {}
}
