package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.hedgetech.fairylightsredux.util.styledstring.StyledString;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class LetterBuntingConnectionItem extends ConnectionItem {
    public LetterBuntingConnectionItem(final Properties properties) {
        super(properties, () -> ConnectionTypes.LETTER_BUNTING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(final ItemStack stack, final @NotNull TooltipContext context, final @NotNull TooltipDisplay tooltipDisplay, final @NotNull Consumer<Component> tooltip, final @NotNull TooltipFlag flag) {
        final StyledString text = stack.getOrDefault(FLRDataComponents.LETTER_BUNTING, StyledString.EMPTY);
        if (!text.isEmpty()) {
            tooltip.accept(Component.translatable("format.fairylightsredux.text", text.toTextComponent()).withStyle(ChatFormatting.GRAY));
        }
    }
}
