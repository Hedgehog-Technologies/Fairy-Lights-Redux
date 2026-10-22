package org.hedgetech.fairylightsredux.server.creativeTabs;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.component.HangingLights;
import org.hedgetech.fairylightsredux.server.component.PennantBunting;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.string.StringType;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public final class FLRCreativeTabs {

    public static CreativeModeTab GENERAL;

    public static void register(final BiConsumer<ResourceLocation, CreativeModeTab> registry) {
        GENERAL = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemGroup.fairylightsredux"))
                .icon(() -> DyeableItem.setColor(new ItemStack(FLRItems.FAIRY_LIGHT), DyeColor.YELLOW))
                .displayItems((parameters, output) -> {
                    for (final DyeColor color: DyeColor.values()) {
                        output.accept(makeHangingLights(color));
                    }

                    for (final DyeColor color: DyeColor.values()) {
                        output.accept(makePennantBunting(color));
                    }

                    acceptColors(output, FLRItems.TINSEL);
                    output.accept(new ItemStack(FLRItems.LETTER_BUNTING));
                    output.accept(new ItemStack(FLRItems.GARLAND));
                    acceptColors(output, FLRItems.TRIANGLE_PENNANT);
                    acceptColors(output, FLRItems.SPEARHEAD_PENNANT);
                    acceptColors(output, FLRItems.SWALLOWTAIL_PENNANT);
                    acceptColors(output, FLRItems.SQUARE_PENNANT);
                    acceptColors(output, FLRItems.FAIRY_LIGHT);
                    acceptColors(output, FLRItems.PAPER_LANTERN);
                    acceptColors(output, FLRItems.ORB_LANTERN);
                    acceptColors(output, FLRItems.FLOWER_LIGHT);
                    acceptColors(output, FLRItems.CANDLE_LANTERN_LIGHT);
                    acceptColors(output, FLRItems.OIL_LANTERN_LIGHT);
                    acceptColors(output, FLRItems.JACK_O_LANTERN);
                    acceptColors(output, FLRItems.SKULL_LIGHT);
                    acceptColors(output, FLRItems.GHOST_LIGHT);
                    acceptColors(output, FLRItems.SPIDER_LIGHT);
                    acceptColors(output, FLRItems.WITCH_LIGHT);
                    acceptColors(output, FLRItems.SNOWFLAKE_LIGHT);
                    acceptColors(output, FLRItems.HEART_LIGHT);
                    acceptColors(output, FLRItems.MOON_LIGHT);
                    acceptColors(output, FLRItems.STAR_LIGHT);
                    acceptColors(output, FLRItems.ICICLE_LIGHTS);
                    acceptColors(output, FLRItems.METEOR_LIGHT);
                    output.accept(new ItemStack(FLRItems.OIL_LANTERN));
                    output.accept(new ItemStack(FLRItems.CANDLE_LANTERN));
                    output.accept(new ItemStack(FLRItems.INCANDESCENT_LIGHT));
                })
                .build();
        registry.accept(Constants.id("general"), GENERAL);
    }

    private static void acceptColors(final CreativeModeTab.Output output, final Item item) {
        for (final DyeColor color : DyeColor.values()) {
            output.accept(DyeableItem.setColor(new ItemStack(item), color));
        }
    }

    private static ItemStack makeHangingLights(final DyeColor... colors) {
        final ItemStack stack = new ItemStack(FLRItems.HANGING_LIGHTS);
        final List<ItemStack> pattern = new ArrayList<>();

        for (final DyeColor color : colors) {
            pattern.add(DyeableItem.setColor(new ItemStack(FLRItems.FAIRY_LIGHT), color));
        }

        stack.set(FLRDataComponents.HANGING_LIGHTS, new HangingLights(StringType.BLACK_STRING, pattern));
        return stack;
    }

    private static ItemStack makePennantBunting(final DyeColor... colors) {
        final ItemStack stack = new ItemStack(FLRItems.PENNANT_BUNTING);
        final List<ItemStack> pattern = new ArrayList<>();

        for (final DyeColor color: colors) {
            pattern.add(DyeableItem.setColor(new ItemStack(FLRItems.TRIANGLE_PENNANT), color));
        }

        stack.set(FLRDataComponents.PENNANT_BUNTING, new PennantBunting(pattern, StyledString.EMPTY));
        return stack;
    }

    private FLRCreativeTabs() {}
}
