package org.hedgetech.fairylightsredux.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.content.tag.TagDefs;

public final class OreDictUtils {
    private OreDictUtils() {}

    public static boolean isDye(final ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof DyeItem) {
                return true;
            }
            // check tags for dyes
            for (final Dye dye : Dye.values()) {
                if (stack.is(dye.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static DyeColor getDyeColor(final ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof DyeItem) {
                return ((DyeItem) stack.getItem()).getDyeColor();
            }
            for (final Dye dye : Dye.values()) {
                if (stack.is(dye.getName())) {
                    return dye.getColor();
                }
            }
        }
        return DyeColor.YELLOW;
    }

    public static ImmutableList<ItemStack> getDyes(final DyeColor color) {
        return getDyeItemStacks().get(color).asList();
    }

    public static ImmutableList<ItemStack> getAllDyes() {
        return getDyeItemStacks().values().asList();
    }

    private static ImmutableMultimap<DyeColor, ItemStack> getDyeItemStacks() {
        final ImmutableMultimap.Builder<DyeColor, ItemStack> bob = ImmutableMultimap.builder();
        for (final Dye dye : Dye.values()) {
            for (final Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(dye.getName())) {
                bob.put(dye.getColor(), new ItemStack(holder));
            }
        }
        return bob.build();
    }

    private enum Dye {
        WHITE(TagDefs.DYES_WHITE.key(), DyeColor.WHITE),
        ORANGE(TagDefs.DYES_ORANGE.key(), DyeColor.ORANGE),
        MAGENTA(TagDefs.DYES_MAGENTA.key(), DyeColor.MAGENTA),
        LIGHT_BLUE(TagDefs.DYES_LIGHT_BLUE.key(), DyeColor.LIGHT_BLUE),
        YELLOW(TagDefs.DYES_YELLOW.key(), DyeColor.YELLOW),
        LIME(TagDefs.DYES_LIME.key(), DyeColor.LIME),
        PINK(TagDefs.DYES_PINK.key(), DyeColor.PINK),
        GRAY(TagDefs.DYES_GRAY.key(), DyeColor.GRAY),
        LIGHT_GRAY(TagDefs.DYES_LIGHT_GRAY.key(), DyeColor.LIGHT_GRAY),
        CYAN(TagDefs.DYES_CYAN.key(), DyeColor.CYAN),
        PURPLE(TagDefs.DYES_PURPLE.key(), DyeColor.PURPLE),
        BLUE(TagDefs.DYES_BLUE.key(), DyeColor.BLUE),
        BROWN(TagDefs.DYES_BROWN.key(), DyeColor.BROWN),
        GREEN(TagDefs.DYES_GREEN.key(), DyeColor.GREEN),
        RED(TagDefs.DYES_RED.key(), DyeColor.RED),
        BLACK(TagDefs.DYES_BLACK.key(), DyeColor.BLACK);

        private final TagKey<Item> name;

        private final DyeColor color;

        Dye(final TagKey<Item> name, final DyeColor color) {
            this.name = name;
            this.color = color;
        }

        private TagKey<Item> getName() {
            return this.name;
        }

        private DyeColor getColor() {
            return this.color;
        }
    }
}
