package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.jetbrains.annotations.NotNull;

public final class TinselConnectionItem extends ConnectionItem {
    public TinselConnectionItem(final Properties properties) {
        super(properties, () -> ConnectionTypes.TINSEL_GARLAND);
    }

    @Override
    public @NotNull Component getName(final @NotNull ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
