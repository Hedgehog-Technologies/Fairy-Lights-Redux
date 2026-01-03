package org.hedgetech.fairylightsredux.server.entity;

import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;

/**
 * Minimal stub for FenceFastenerEntity used by legacy handlers during migration.
 * Replace with full entity port later.
 */
public class FenceFastenerEntity extends HangingEntity {
    protected FenceFastenerEntity() { super(null, 0, 0); }

    @Override
    public void tick() { }

    public static HangingEntity findHanging(final Level world, final BlockPos pos) {
        // naive stub — real implementation should search for a hanging entity
        return null;
    }

    public static FenceFastenerEntity create(final Level world, final BlockPos pos) {
        // stub
        return null;
    }
}

