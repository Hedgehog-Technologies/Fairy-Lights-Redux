package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.hedgetech.fairylightsredux.server.string.StringType;
import org.hedgetech.fairylightsredux.server.string.StringTypes;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.HANGING_LIGHTS);
    }

    @Override
    public void appendHoverText(final @NonNull ItemStack stack, Item.@NonNull TooltipContext ctx, @NonNull TooltipDisplay display, @NonNull Consumer<Component> tooltip, @NonNull TooltipFlag flag) {
        // TODO - This may need to be updated and moved to ItemStack logic?
        throw new NotImplementedException("HangingLightsConnectionItem.appendHoverText");
    }

    public static StringType getString(final ItemStack stack) {
        return stack.getOrDefault(StringType.TYPE, StringTypes.BLACK_STRING.get());
    }

    public static void setString(final ItemStack stack, final StringType string) {
        stack.set(StringType.TYPE, string);
    }
}
