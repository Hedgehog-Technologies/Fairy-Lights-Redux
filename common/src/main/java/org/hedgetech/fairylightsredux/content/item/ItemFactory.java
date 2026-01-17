package org.hedgetech.fairylightsredux.content.item;

import net.minecraft.world.item.Item;

public interface ItemFactory {
    Item create(Item.Properties props, ItemDef def);
}
