package org.hedgetech.fairylightsredux.content.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

public record BlockEntityDef<T extends BlockEntity>(
        String id,
        BlockEntityFactory<T> factory,
        @Nullable TickerFactory<T> ticker,
        boolean clientSync
) {
    public BlockEntityDef(String id, BlockEntityFactory<T> factory, boolean clientSync) {
        this(id, factory, null, clientSync);
    }
}
