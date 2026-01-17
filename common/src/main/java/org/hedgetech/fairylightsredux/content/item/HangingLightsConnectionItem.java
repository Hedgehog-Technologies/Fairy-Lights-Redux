package org.hedgetech.fairylightsredux.content.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.type.StringType;
import org.hedgetech.fairylightsredux.type.StringTypes;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(Properties props) {
        super(props);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext ctx, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flag) {
        // TODO What goes here? OR where does it move?
        throw new NotImplementedException("HangingLightsConnectionItem.appendHoverText");
    }

    public static StringType getString(ItemStack stack) {
        return stack.getOrDefault(StringType.TYPE, StringTypes.BLACK_STRING.get());
    }

    public static void setString(ItemStack stack, StringType stringType) {
        stack.set(StringType.TYPE, stringType);
    }
}
