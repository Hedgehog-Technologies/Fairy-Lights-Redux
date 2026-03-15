package org.hedgetech.fairylightsredux.content.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.item.DyeableItem;
import org.jetbrains.annotations.NotNull;

public class PennantItem extends Item {
    public PennantItem(final Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull Component getName(final @NotNull ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
