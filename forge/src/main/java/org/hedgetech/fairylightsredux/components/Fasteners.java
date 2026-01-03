package org.hedgetech.fairylightsredux.components;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Helper accessors for FastenerComponent instances. In a full implementation this would
 * register components with the appropriate component system or attach them to entities/block entities.
 * For migration and minimal compatibility we provide simple lazy creation methods.
 */
public final class Fasteners {

    private Fasteners() {}

    public static FastenerComponent getOrCreate(final Entity entity) {
        final FastenerComponent comp = new FastenerComponent();
        // In a full migration we'd retrieve an existing component attached to the entity.
        // For now, return a new instance and attempt migration using existing entity NBT.
        comp.migrateIfNeeded(entity);
        return comp;
    }

    public static FastenerComponent getOrCreate(final BlockEntity be) {
        final FastenerComponent comp = new FastenerComponent();
        comp.migrateIfNeeded(be);
        return comp;
    }

    public static FastenerComponent getOrCreate(final Object obj) {
        if (obj instanceof Entity) return getOrCreate((Entity) obj);
        if (obj instanceof BlockEntity) return getOrCreate((BlockEntity) obj);
        return null;
    }

    public static FastenerComponent get(final Entity entity) {
        // Placeholder for retrieving existing component; returns null until proper component system is wired.
        return null;
    }

}

