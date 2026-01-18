package org.hedgetech.fairylightsredux.registry;

import net.minecraft.world.level.block.Block;
import org.hedgetech.fairylightsredux.content.block.BlockDef;
import org.hedgetech.fairylightsredux.content.block.BlockDefs;

import java.util.HashMap;
import java.util.Map;

public final class FLRBlocks {
    private static final Map<String, Block> BLOCKS = new HashMap<>();

    public static void registerAll(RegistryBridge bridge) {
        for (BlockDef def : BlockDefs.BLOCKS) {
            Block block = bridge.registerBlock(
                    def.id(),
                    def::createBlock
            );
            BLOCKS.put(def.id(), block);
        }
    }

    public static Block get(String id) {
        return BLOCKS.get(id);
    }

    private FLRBlocks() {}
}
