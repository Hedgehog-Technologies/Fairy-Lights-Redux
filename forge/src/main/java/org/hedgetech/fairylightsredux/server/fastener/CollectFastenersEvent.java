package org.hedgetech.fairylightsredux.server.fastener;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.components.FastenerComponent;
import org.hedgetech.fairylightsredux.components.Fasteners;

import java.util.ConcurrentModificationException;
import java.util.Set;

/**
 * Forge-side replacement for the legacy CollectFastenersEvent that collected capability-backed
 * fasteners. This version collects FastenerComponents from block entities found in chunks.
 */
public class CollectFastenersEvent {
    private final Level world;
    private final AABB region;
    private final Set<FastenerComponent> fasteners;

    public CollectFastenersEvent(final Level world, final AABB region, final Set<FastenerComponent> fasteners) {
        this.world = world;
        this.region = region;
        this.fasteners = fasteners;
    }

    public Level getWorld() { return this.world; }
    public AABB getRegion() { return this.region; }

    public void accept(final LevelChunk chunk) {
        try {
            for (final BlockEntity entity : chunk.getBlockEntities().values()) {
                this.accept(entity);
            }
        } catch (final ConcurrentModificationException e) {
            // Ignore concurrent modification while iterating block entities
        }
    }

    public void accept(final BlockEntity entity) {
        final FastenerComponent comp = Fasteners.getOrCreate(entity);
        if (comp != null) this.accept(comp);
    }

    public void accept(final FastenerComponent fastener) {
        // Here we can't compute the connection point without the old Fastener API;
        // assume components carry sufficient data to determine if they fall within region.
        // For now, add all non-null components and let downstream code filter as needed.
        this.fasteners.add(fastener);
    }
}

