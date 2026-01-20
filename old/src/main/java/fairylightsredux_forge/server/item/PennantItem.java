package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class PennantItem extends Item {
    public PennantItem(final Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull Component getName(final @NonNull ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
