package org.hedgetech.fairylightsredux.registry;

import net.minecraft.world.item.Item;
import org.hedgetech.fairylightsredux.content.item.ItemDef;
import org.hedgetech.fairylightsredux.content.item.ItemDefs;

import java.util.HashMap;
import java.util.Map;

public final class FLRItems {
    private static final Map<String, Item> ITEMS = new HashMap<>();

    public static void registerAll(RegistryBridge bridge) {
        for (ItemDef def : ItemDefs.ITEMS) {
            Item item = bridge.registerItem(
                    def.id(),
                    def::createItem
            );
            ITEMS.put(def.id(), item);
        }
    }

    public static Item get(String id) {
        return ITEMS.get(id);
    }

    private FLRItems() {}
}
