package org.hedgetech.fairylightsredux.content;

import net.minecraft.world.item.Item;
import org.hedgetech.fairylightsredux.content.def.ItemDef;

import java.util.HashMap;
import java.util.Map;

public final class FLRItems {
    private static final Map<String, Item> ITEMS = new HashMap<>();

    public static void registerAll(RegistryBridge bridge) {
        for (ItemDef def : ItemDefinitions.ITEMS) {
            Item item = bridge.registerItem(
                    def.name(),
                    () -> new Item(def.props().get())
            );
            ITEMS.put(def.name(), item);
        }
    }

    public static Item get(String name) {
        return ITEMS.get(name);
    }
}
