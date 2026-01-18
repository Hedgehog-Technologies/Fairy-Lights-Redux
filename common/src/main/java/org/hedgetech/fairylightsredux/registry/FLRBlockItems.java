package org.hedgetech.fairylightsredux.registry;

import net.minecraft.world.item.BlockItem;
import org.hedgetech.fairylightsredux.content.block.BlockDef;
import org.hedgetech.fairylightsredux.content.block.BlockDefs;

import java.util.HashMap;
import java.util.Map;

public final class FLRBlockItems {
    private static final Map<String, BlockItem> BLOCK_ITEMS = new HashMap<>();

    public static void registerAll(RegistryBridge bridge) {
        for (BlockDef def : BlockDefs.BLOCKS) {
            BlockItem item = bridge.registerBlockItem(
                    def.id(),
                    def::createBlockItem
            );
            BLOCK_ITEMS.put(def.id(), item);
        }
    }

    public static BlockItem get(String id) {
        return BLOCK_ITEMS.get(id);
    }

    private FLRBlockItems() {}
}
