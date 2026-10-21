package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.server.component.HangingLights;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Properties properties) {
        super(properties, () -> ConnectionTypes.HANGING_LIGHTS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(final ItemStack stack, final @NotNull TooltipContext context, final @NotNull TooltipDisplay tooltipDisplay, final @NotNull Consumer<Component> tooltip, final @NotNull TooltipFlag flag) {
        final HangingLights lights = stack.get(FLRDataComponents.HANGING_LIGHTS);
        if (lights == null) return;

        tooltip.accept(Component.translatable(lights.string().getTranslationKey()).withStyle(ChatFormatting.GRAY));

        if (!lights.pattern().isEmpty()) {
            tooltip.accept(Component.empty());
        }

        for (final ItemStack lightStack : lights.pattern()) {
            tooltip.accept(lightStack.getHoverName());
            lightStack.getItem().appendHoverText(lightStack, context, tooltipDisplay, tooltip, flag);
        }
    }
}
