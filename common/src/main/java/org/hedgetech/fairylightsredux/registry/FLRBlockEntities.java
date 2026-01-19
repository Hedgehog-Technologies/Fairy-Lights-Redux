package org.hedgetech.fairylightsredux.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.hedgetech.fairylightsredux.content.block.BlockDef;
import org.hedgetech.fairylightsredux.content.block.BlockDefs;
import org.hedgetech.fairylightsredux.content.block.entity.BlockEntityDef;

import java.util.HashMap;
import java.util.Map;

public final class FLRBlockEntities {
    private static final Map<String, BlockEntityType<?>> TYPES = new HashMap<>();

    public static void registerAll(RegistryBridge bridge) {
        for (BlockDef def : BlockDefs.BLOCKS) {
            if (def.blockEntity() == null) continue;

            BlockEntityDef<?> beDef = def.blockEntity();
            Block block = FLRBlocks.get(def.id());
            BlockEntityType<?> type = bridge.registerBlockEntityType(
                    beDef.id(),
                    beDef,
                    () -> new Block[]{ block }
            );
            TYPES.put(def.id(), type);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends BlockEntity> BlockEntityType<T> get(String id) {
        return (BlockEntityType<T>) TYPES.get(id);
    }

    private FLRBlockEntities() {}
}
