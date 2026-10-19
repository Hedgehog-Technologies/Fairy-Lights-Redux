package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.server.block.LightBlock;
import org.hedgetech.fairylightsredux.server.feature.light.ColorChangingBehavior;
import org.jetbrains.annotations.NotNull;

public class ColorLightItem extends LightItem {
    public ColorLightItem(final LightBlock light, final Item.Properties properties) {
        super(light, properties);
    }

    @Override
    public @NotNull Component getName(final @NotNull ItemStack stack) {
        if (ColorChangingBehavior.exists(stack)) {
            return Component.translatable("format.fairylightsredux.color_changing", super.getName(stack));
        }

        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
