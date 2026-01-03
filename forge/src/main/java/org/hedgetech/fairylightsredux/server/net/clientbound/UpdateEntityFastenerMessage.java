package org.hedgetech.fairylightsredux.server.net.clientbound;

import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import org.hedgetech.fairylightsredux.components.Fasteners;
import org.hedgetech.fairylightsredux.components.FastenerComponent;
import net.minecraft.world.level.Level;

/**
 * Minimal stub of the legacy UpdateEntityFastenerMessage for incremental migration.
 * This class should be replaced with a proper network message implementation.
 */
public class UpdateEntityFastenerMessage {
    public final int entityId;
    public final CompoundTag compound;

    public UpdateEntityFastenerMessage(final Entity entity, final CompoundTag compound) {
        this.entityId = entity.getId();
        this.compound = compound;
    }

    public void handleClientSide(final Level world) {
        final Entity entity = world.getEntity(this.entityId);
        if (entity != null) {
            final FastenerComponent comp = Fasteners.getOrCreate(entity);
            if (comp != null) comp.deserializeNBT(this.compound);
        }
    }
}
