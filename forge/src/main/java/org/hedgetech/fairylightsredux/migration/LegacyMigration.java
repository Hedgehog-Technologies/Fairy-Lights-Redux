package org.hedgetech.fairylightsredux.migration;

import net.minecraftforge.event.entity.EntityEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import org.hedgetech.fairylightsredux.components.FastenerComponent;
import org.hedgetech.fairylightsredux.components.Fasteners;

/**
 * Handles lazy migration from legacy capability/NBT storage to the new FastenerComponent system.
 * This class intentionally has minimal logic: it checks for legacy data on entity/blockentity load
 * and delegates conversion to the FastenerComponent when present.
 */
public class LegacyMigration {

    @SubscribeEvent
    public void onEntityConstruct(final EntityEvent.EntityConstructing event) {
        final Entity entity = event.getEntity();
        // Attempt to attach or migrate fastener data for entities
        final FastenerComponent comp = Fasteners.getOrCreate(entity);
        if (comp != null) {
            comp.migrateIfNeeded(entity);
        }
    }

    @SubscribeEvent
    public void onBlockEntityLoad(final EntityEvent.EntityConstructing event) {
        // BlockEntity lifecycle events differ; this placeholder keeps symmetry for future expansion
        // Real BlockEntity migration should be performed in the BlockEntity's load method or a dedicated mod event
    }
}

