package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public final class LetterBuntingConnectionItem extends ConnectionItem {
    public LetterBuntingConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.LETTER_BUNTING);
    }

    @Override
    public void appendHoverText(final @NonNull ItemStack stack, Item.@NonNull TooltipContext ctx, @NonNull TooltipDisplay display, @NonNull Consumer<Component> tooltip, @NonNull TooltipFlag flag) {
        // TODO - This may nee to be updated and moved ot ItemStack logic?
        throw new NotImplementedException("LetterBuntingConnectionItem.appendHoverText");
    }
}
