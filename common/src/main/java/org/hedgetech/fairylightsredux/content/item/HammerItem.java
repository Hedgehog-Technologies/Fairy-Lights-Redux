package org.hedgetech.fairylightsredux.content.item;

import net.minecraft.world.item.Item;

public final class HammerItem extends Item {
    private final int tier;

    public HammerItem(Item.Properties props, int tier) {
        super(props);
        this.tier = tier;
    }

    public int getTier() {
        return this.tier;
    }
}
