package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.hedgetech.fairylightsredux.server.block.LightBlock;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class LightItem extends BlockItem {
    private final LightBlock light;

    public LightItem(final LightBlock light, final Properties properties) {
        super(light, properties);
        this.light = light;
    }

    @Override
    public @NotNull LightBlock getBlock() {
        return this.light;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(final @NotNull ItemStack stack, final @NotNull TooltipContext context, final @NotNull TooltipDisplay display, final @NotNull Consumer<Component> tooltip, final @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);

        if (stack.has(FLRDataComponents.TWINKLE)) {
            tooltip.accept(Component.translatable("item.fairylightsredux.twinkle").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }

        for (final int color : stack.getOrDefault(FLRDataComponents.COLORS, List.<Integer>of())) {
            tooltip.accept(DyeableItem.getColorName(color).copy().withStyle(ChatFormatting.GRAY));
        }
    }
}
