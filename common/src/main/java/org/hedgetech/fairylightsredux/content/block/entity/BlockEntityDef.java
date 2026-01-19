package org.hedgetech.fairylightsredux.content.block.entity;

import net.minecraft.world.level.block.entity.BlockEntity;

public record BlockEntityDef<T extends BlockEntity>(
        String id,
        BlockEntityFactory<T> factory,
        TickerFactory<T> ticker,
        boolean clientSync
) {}
