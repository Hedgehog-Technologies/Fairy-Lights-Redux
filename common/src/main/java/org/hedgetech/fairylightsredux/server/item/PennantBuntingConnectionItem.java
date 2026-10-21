package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.component.PennantBunting;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class PennantBuntingConnectionItem extends ConnectionItem {
    public PennantBuntingConnectionItem(final Properties properties) {
        super(properties, () -> ConnectionTypes.PENNANT_BUNTING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(final ItemStack stack, final @NotNull TooltipContext context, final @NotNull TooltipDisplay tooltipDisplay, final @NotNull Consumer<Component> tooltip, final @NotNull TooltipFlag flag) {
        final PennantBunting bunting = stack.get(FLRDataComponents.PENNANT_BUNTING);
        if (bunting == null) return;

        if (!bunting.text().isEmpty()) {
            tooltip.accept(Component.translatable("format.fairylightsredux.text", bunting.text().toTextComponent()).withStyle(ChatFormatting.GRAY));
        }

        if (!bunting.pattern().isEmpty()) {
            tooltip.accept(Component.empty());
        }

        for (final ItemStack pennant : bunting.pattern()) {
            tooltip.accept(pennant.getHoverName());
        }
    }
}
