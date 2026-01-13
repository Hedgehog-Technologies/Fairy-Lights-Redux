package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.server.block.LightBlock;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class LightItem extends BlockItem {
    private final LightBlock light;

    public LightItem(final LightBlock light, final Properties properties) {
        super(light, properties);
        this.light = light;
    }

    @Override
    public @NonNull LightBlock getBlock() {
        return this.light;
    }

    @Override
    public void appendHoverText(final @NonNull ItemStack stack, Item.@NonNull TooltipContext ctx, @NonNull TooltipDisplay display, @NonNull Consumer<Component> tooltip, @NonNull TooltipFlag flag) {
        // TODO - This may nee to be updated and moved ot ItemStack logic?
        throw new NotImplementedException("LightItem.appendHoverText");
    }
}
