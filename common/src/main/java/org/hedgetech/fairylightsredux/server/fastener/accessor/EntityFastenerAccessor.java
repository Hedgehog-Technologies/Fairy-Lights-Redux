package org.hedgetech.fairylightsredux.server.fastener.accessor;

import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.fastener.EntityFastener;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.fastener.IFastenerHolder;

import java.util.Optional;
import java.util.UUID;

public abstract class EntityFastenerAccessor<E extends Entity> implements IFastenerAccessor {
    private final Class<? extends E> entityClass;

    private UUID uuid;
    private E entity;
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
    public Optional<Fastener<?>> get(final Level world, final boolean load) {
        if (this.entity == null) {
            if (world instanceof final ServerLevel serverLevel) {
                final Entity e = serverLevel.getEntity(this.uuid);
                if (this.entityClass.isInstance(e)) {
                    this.entity = this.entityClass.cast(e);
                }
            } else if (this.pos != null) {
                for (final E entity : world.getEntitiesOfClass(this.entityClass, new AABB(this.pos.subtract(1.0D, 1.0D, 1.0D), this.pos.add(1.0D, 1.0D, 1.0d)))) {
                    if (this.uuid.equals(entity.getUUID())) {
                        this.entity = entity;
                        break;
                    }
                }
            }
        }

        if (this.entity != null && this.entity.level() == world && this.entity instanceof final IFastenerHolder holder) {
            this.pos = this.entity.position();
            return Optional.of(holder.getFastener());
        }

        return Optional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        return !world.isClientSide() && this.entity != null
                && (this.entity.isRemoved() || this.entity.level() != world);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj instanceof final EntityFastenerAccessor<?> other) return this.uuid.equals(other.uuid);
        return false;
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }

    @Override
    public void save(final ValueOutput output) {
        output.store("UUID", UUIDUtil.CODEC, this.uuid);
        output.storeNullable("Pos", Vec3.CODEC, this.pos);
    }

    @Override
    public boolean load(final ValueInput input) {
        final Optional<UUID> uuid = input.read("UUID", UUIDUtil.CODEC);
        uuid.ifPresent(u -> this.uuid = u);
        this.pos = input.read("Pos", Vec3.CODEC).orElse(null);
        this.entity = null;
        return uuid.isPresent();
    }
}
