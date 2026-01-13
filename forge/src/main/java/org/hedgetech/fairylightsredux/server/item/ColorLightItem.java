package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.server.block.LightBlock;
import org.jspecify.annotations.NonNull;

public class ColorLightItem extends LightItem {
    public ColorLightItem(final LightBlock light, final Properties properties) {
        super(light, properties);
    }

    @Override
    public @NonNull Component getName(final @NonNull ItemStack stack) {
        throw new NotImplementedException("ColorLightItem.getName");
    }
}
