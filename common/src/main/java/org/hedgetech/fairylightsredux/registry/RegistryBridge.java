package org.hedgetech.fairylightsredux.registry;

import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface RegistryBridge {
    Item registerItem(String name, Supplier<Item> item);
}
