package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.Util;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

import java.util.Map;

public final class DyeableItem {
    private static final Map<DyeColor, TagKey<Item>> DYE_TAGS = Util.makeEnumMap(DyeColor.class, color ->
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes/" + color.getSerializedName())));

    public static Component getColorName(final int color) {
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

        final Component colorName = Component.translatable("color.fairylightsredux." + closest.getName());
        return closestDist == 0
                ? colorName
                : Component.translatable("format.fairylightsredux.dyed_colored", colorName);
    }

    public static Component getDisplayName(final ItemStack stack, final Component name) {
        return Component.translatable("format.fairylights.colored", getColorName(getColor(stack)), name);
    }

    public static int getColor(final DyeColor color) {
        if (color == DyeColor.BLACK) return 0x323232;
        if (color == DyeColor.GRAY) return 0x606060;
        return color.getTextureDiffuseColor() * 0xFFFFFF;
    }

    public static int getColor(final ItemStack stack) {
        return DyedItemColor.getOrDefault(stack, 0xFFFFFF) & 0xFFFFFF;
    }

    public static ItemStack setColor(final ItemStack stack, final DyeColor dye) {
        return setColor(stack, getColor(dye));
    }

    public static ItemStack setColor(final ItemStack stack, final int color) {
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color));
        return stack;
    }

    public static DyeColor getDyeColor(final ItemStack stack) {
        if (stack.getItem() instanceof DyeItem dye) return dye.getDyeColor();

        for (final DyeColor color : DyeColor.values()) {
            if (stack.is(DYE_TAGS.get(color))) {
                return color;
            }
        }

        return null;
    }

    private DyeableItem() {}
}
