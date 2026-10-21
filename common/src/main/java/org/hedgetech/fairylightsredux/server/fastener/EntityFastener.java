package org.hedgetech.fairylightsredux.server.fastener;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.fastener.accessor.EntityFastenerAccessor;

public abstract class EntityFastener<E extends Entity> extends Fastener<EntityFastenerAccessor<E>> {
    protected final E entity;

    public EntityFastener(final E entity) {
        this.entity = entity;
        this.setWorld(entity.level());
    }

    @Override
    public Direction getFacing() {
        return Direction.UP;
    }

    public E getEntity() {
        return this.entity;
    }

    @Override
    public BlockPos getPos() {
        return this.entity.blockPosition();
    }

    @Override
    public Vec3 getConnectionPoint() {
        return this.entity.position();
    }

    @Override
    public boolean update() {
        if (this.getWorld() != this.entity.level()) {
            this.setWorld(this.entity.level());
        }
        return super.update();
    }
}
