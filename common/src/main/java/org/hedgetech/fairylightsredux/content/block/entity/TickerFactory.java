package org.hedgetech.fairylightsredux.content.block.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

@FunctionalInterface
public interface TickerFactory<T extends BlockEntity> {
    BlockEntityTicker<T> create(Level world);
}
