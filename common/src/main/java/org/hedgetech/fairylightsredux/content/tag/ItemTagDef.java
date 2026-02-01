package org.hedgetech.fairylightsredux.content.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public final class ItemTagDef extends TagDef<Item, ItemTagDef> {
    public ItemTagDef(ResourceLocation id) {
        super(Registries.ITEM, id);
    }

    public ItemTagDef add(Item item) {
        return add(() -> item);
    }
}
