package org.hedgetech.fairylightsredux.server.fastener.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.hedgetech.fairylightsredux.server.capability.CapabilityHandler;
import org.hedgetech.fairylightsredux.server.fastener.EntityFastener;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityFastenerAccessor<E extends Entity> implements FastenerAccessor {
    private final Class<? extends E> entityClass;
    private UUID uuid;
    @Nullable
    private E entity;
    @Nullable
    private Vec3 pos;

    public EntityFastenerAccessor(final Class<? extends E> entityClass) {
        this(entityClass, (UUID) null);
    }

    public EntityFastenerAccessor(final Class<? extends E> entityClass, final EntityFastener<E> fastener) {
        this(entityClass, fastener.getEntity().getUUID());
        this.entity = fastener.getEntity();
        this.pos = this.entity.position();
    }

    public EntityFastenerAccessor(final Class<? extends E> entityClass, final UUID uuid) {
        this.entityClass = entityClass;
        this.uuid = uuid;
    }

    @Override
    public LazyOptional<Fastener<?>> get(final Level world, final boolean load) {
        if (this.entity == null) {
            if (world instanceof ServerLevel sWorld) {
                final Entity e = sWorld.getEntity(this.uuid);
                if (this.entityClass.isInstance(e)) {
                    this.entity = this.entityClass.cast(e);
                }
            } else if (this.pos != null) {
                for (final E entity : world.getEntitiesOfClass(this.entityClass, new AABB(this.pos.subtract(1.0D, 1.0D, 1.0D), this.pos.add(1.0D, 1.0D, 1.0D)))) {
                    if (this.uuid.equals(entity.getUUID())) {
                        this.entity = entity;
                        break;
                    }
                }
            }
        }
        if (this.entity != null && this.entity.level() == world) {
            this.pos = this.entity.position();
            return this.entity.getCapability(CapabilityHandler.FASTENER_CAP);
        }
        return LazyOptional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        return !world.isClientSide() && this.entity != null && (!this.entity.getCapability(CapabilityHandler.FASTENER_CAP).isPresent() || this.entity.level() != world);
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) return true;
        if (obj instanceof EntityFastenerAccessor<?> efa) {
            return this.uuid.equals(efa.uuid);
        }
        return false;
    }

    /// Deprecated methods for serialization compatibility
    /// Use EntityFastenerAccessorData DataComponentType instead
    @Deprecated
    @Override
    public CompoundTag serialize() { return new CompoundTag(); }

    /// Deprecated methods for deserialization compatibility
    /// Use EntityFastenerAccessorData DataComponentType instead
    @Deprecated
    @Override
    public void deserialize(final CompoundTag tag) {}
}
