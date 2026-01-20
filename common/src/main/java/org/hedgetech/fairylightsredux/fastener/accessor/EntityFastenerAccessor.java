package org.hedgetech.fairylightsredux.fastener.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.fastener.EntityFastener;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public abstract class EntityFastenerAccessor<E extends Entity> implements FastenerAccessor {
    private final Class<? extends E> entityClass;
    private @Nullable UUID uuid;
    private @Nullable E entity;
    private @Nullable Vec3 pos;

    public EntityFastenerAccessor(final Class<? extends E> entityClass) {
        this(entityClass, (UUID) null);
    }

    public EntityFastenerAccessor(final Class<? extends E> entityClass, final EntityFastener<E> fastener) {
        this(entityClass, fastener.getEntity().getUUID());
        this.entity = fastener.getEntity();
        this.pos = this.entity.position();
    }

    public EntityFastenerAccessor(final Class<? extends E> entityClass, final @Nullable UUID uuid) {
        this.entityClass = entityClass;
        this.uuid = uuid;
    }

    @Override
    public Optional<Fastener<?>> get(final Level world, final boolean load) {
        if (this.entity == null && this.uuid != null) {
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
            // FIXME - implement retrieval of Fastener from Entity
            throw new NotImplementedException("EntityFastenerAccessor.get");
//            this.pos = this.entity.position();
//            return this.entity.getCapability(CapabilityHandler.FASTENER_CAP);
        }
        return Optional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        return !world.isClientSide()
                && this.entity != null
                // FIXME - implement retrieval of Fastener from Entity
                /*&& (!this.entity.getCapability(CapabilityHandler.FASTENER_CAP).isPresent()
                    || this.entity.level() != world)*/;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) return true;
        if (this.uuid != null && obj instanceof EntityFastenerAccessor<?> efa) {
            return this.uuid.equals(efa.uuid);
        }
        return false;
    }

    // FIXME Deprecated as of "Compound tags be damned"
//    @Override
//    public CompoundTag serialize() {
//        final CompoundTag tag = new CompoundTag();
//        tag.putUUID("UUID", this.uuid);
//        if (this.pos != null) {
//            final ListTag pos = new ListTag();
//            pos.add(DoubleTag.valueOf(this.pos.x));
//            pos.add(DoubleTag.valueOf(this.pos.y));
//            pos.add(DoubleTag.valueOf(this.pos.z));
//            tag.put("Pos", pos);
//        }
//        return tag;
//    }
//
//    @Override
//    public void deserialize(final CompoundTag tag) {
//        this.uuid = tag.getUUID("UUID");
//        if (tag.contains("Pos", Tag.TAG_LIST)) {
//            final ListTag pos = tag.getList("Pos", Tag.TAG_DOUBLE);
//            this.pos = new Vec3(pos.getDouble(0), pos.getDouble(1), pos.getDouble(2));
//        } else {
//            this.pos = null;
//        }
//        this.entity = null;
//    }
}
