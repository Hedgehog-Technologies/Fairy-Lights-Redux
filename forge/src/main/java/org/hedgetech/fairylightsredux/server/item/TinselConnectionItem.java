package org.hedgetech.fairylightsredux.server.item;


import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.jspecify.annotations.NonNull;

public final class TinselConnectionItem extends ConnectionItem {
    public TinselConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.TINSEL_GARLAND);
    }

    @Override
    public @NonNull Component getName(final @NonNull ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }
}
