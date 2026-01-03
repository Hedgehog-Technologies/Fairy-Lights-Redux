package org.hedgetech.fairylightsredux.components;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Minimal persistent component to replace legacy capability-based fastener storage.
 * This class provides a compatibility migration hook that will look for legacy NBT keys
 * and migrate them into the component's own CompoundTag storage.
 */
public class FastenerComponent {

    private static final String LEGACY_TAG = "fairylights:fastener";
    private static final String MIGRATED_FLAG = "fl_migrated_fastener";

    private CompoundTag data = new CompoundTag();

    public FastenerComponent() {
    }

    /**
     * Return a copy of the component data for persistence.
     */
    public CompoundTag save() {
        return this.data.copy();
    }

    /**
     * Load component data from a tag.
     */
    public void load(final CompoundTag tag) {
        if (tag != null) {
            this.data = tag.copy();
        }
    }

    /**
     * Write component data to a network buffer.
     */
    public void writeToBuffer(final FriendlyByteBuf buf) {
        buf.writeNbt(this.data);
    }

    /**
     * Read component data from a network buffer.
     */
    public void readFromBuffer(final FriendlyByteBuf buf) {
        final CompoundTag tag = buf.readNbt();
        this.data = tag != null ? tag : new CompoundTag();
    }

    /**
     * Attempt to migrate legacy fastener data from an Entity's saved NBT into this component.
     * This is a lazy migration that will set a migrated flag in the component data so it
     * is only performed once.
     */
    public void migrateIfNeeded(final Entity entity) {
        if (entity == null) return;
        try {
            final CompoundTag persistent = writeEntityToTag(entity);
            if (persistent == null) return;
            if (persistent.contains(MIGRATED_FLAG)) return;

            // getCompound may return CompoundTag or Optional<CompoundTag> depending on mappings
            final Optional<CompoundTag> legacyOpt = getCompoundOptional(persistent, LEGACY_TAG);
            if (legacyOpt.isPresent()) {
                this.load(legacyOpt.get());
                this.data.putBoolean(MIGRATED_FLAG, true);
            }
        } catch (final Throwable t) {
            // Best-effort; do not fail migration on exceptions
        }
    }

    /**
     * Attempt to migrate legacy fastener data from a BlockEntity's saved NBT into this component.
     */
    public void migrateIfNeeded(final BlockEntity blockEntity) {
        if (blockEntity == null) return;
        try {
            final CompoundTag persistent = writeBlockEntityToTag(blockEntity);
            if (persistent == null) return;
            if (persistent.contains(MIGRATED_FLAG)) return;

            final Optional<CompoundTag> legacyOpt = getCompoundOptional(persistent, LEGACY_TAG);
            if (legacyOpt.isPresent()) {
                this.load(legacyOpt.get());
                this.data.putBoolean(MIGRATED_FLAG, true);
            }
        } catch (final Throwable t) {
            // Ignore and continue; migration is best-effort
        }
    }

    private static Optional<CompoundTag> getCompoundOptional(final CompoundTag tag, final String key) {
        // Try reflection first: some mappings expose getCompound(String) which returns either
        // CompoundTag or Optional<CompoundTag>.
        try {
            final Method m = tag.getClass().getMethod("getCompound", String.class);
            final Object res = m.invoke(tag, key);
            if (res instanceof Optional) {
                //noinspection unchecked
                return (Optional<CompoundTag>) res;
            }
            if (res instanceof CompoundTag) {
                return Optional.of((CompoundTag) res);
            }
        } catch (final NoSuchMethodException ignored) {
            // Fall back to other strategies below
        } catch (final Throwable ignored) {
            // Invocation error; fall through to next attempt
        }

        // Fallback: if contains(key) exists and returns true, try to retrieve via reflection
        try {
            final Method containsMethod = tag.getClass().getMethod("contains", String.class);
            final Object has = containsMethod.invoke(tag, key);
            if (has instanceof Boolean && (Boolean) has) {
                try {
                    final Method m2 = tag.getClass().getMethod("get", String.class);
                    final Object got = m2.invoke(tag, key);
                    if (got instanceof CompoundTag) return Optional.of((CompoundTag) got);
                } catch (final NoSuchMethodException ignored) {
                }
            }
        } catch (final Throwable ignored) {
            // Give up and return empty
        }

        return Optional.empty();
    }

    /**
     * Best-effort: attempt to write an Entity's persistent data into a CompoundTag.
     * Different mappings expose different method names/signatures so we try several
     * possibilities via reflection.
     */
    @Nullable
    private static CompoundTag writeEntityToTag(final Entity entity) {
        final CompoundTag tag = new CompoundTag();
        try {
            // Common: save(CompoundTag) -> returns CompoundTag or void
            try {
                final Method m = entity.getClass().getMethod("save", CompoundTag.class);
                final Object res = m.invoke(entity, tag);
                if (res instanceof CompoundTag) return (CompoundTag) res;
                return tag;
            } catch (final NoSuchMethodException ignored) {
            }

            // Some mappings: saveWithoutId(CompoundTag)
            try {
                final Method m = entity.getClass().getMethod("saveWithoutId", CompoundTag.class);
                final Object res = m.invoke(entity, tag);
                if (res instanceof CompoundTag) return (CompoundTag) res;
                return tag;
            } catch (final NoSuchMethodException ignored) {
            }

            // Other mappings: a no-arg save() returning CompoundTag
            try {
                final Method m = entity.getClass().getMethod("save");
                final Object res = m.invoke(entity);
                if (res instanceof CompoundTag) return (CompoundTag) res;
            } catch (final NoSuchMethodException ignored) {
            }

            // Older names: addAdditionalSaveData(CompoundTag)
            try {
                final Method m = entity.getClass().getMethod("addAdditionalSaveData", CompoundTag.class);
                m.invoke(entity, tag);
                return tag;
            } catch (final NoSuchMethodException ignored) {
            }

            // Fallback: try for a method that writes to NBT via other known names
            try {
                final Method m = entity.getClass().getMethod("writeToNbt", CompoundTag.class);
                m.invoke(entity, tag);
                return tag;
            } catch (final NoSuchMethodException ignored) {
            }
        } catch (final Throwable t) {
            // swallow and return null below
        }
        return null;
    }

    /**
     * Best-effort: attempt to write a BlockEntity's persistent data into a CompoundTag.
     * Attempts multiple possible method names/signatures via reflection.
     */
    @Nullable
    private static CompoundTag writeBlockEntityToTag(final BlockEntity be) {
        final CompoundTag tag = new CompoundTag();
        try {
            // Common: save(CompoundTag) -> either returns CompoundTag or void
            try {
                final Method m = be.getClass().getMethod("save", CompoundTag.class);
                final Object res = m.invoke(be, tag);
                if (res instanceof CompoundTag) return (CompoundTag) res;
                return tag;
            } catch (final NoSuchMethodException ignored) {
            }

            // Typical name: saveAdditional(CompoundTag)
            try {
                final Method m = be.getClass().getMethod("saveAdditional", CompoundTag.class);
                m.invoke(be, tag);
                return tag;
            } catch (final NoSuchMethodException ignored) {
            }

            // Some mappings expose a no-arg save() -> CompoundTag
            try {
                final Method m = be.getClass().getMethod("save");
                final Object res = m.invoke(be);
                if (res instanceof CompoundTag) return (CompoundTag) res;
            } catch (final NoSuchMethodException ignored) {
            }

            // Older name: toTag / writeNbt / serializeNBT
            try {
                final Method m = be.getClass().getMethod("toTag");
                final Object res = m.invoke(be);
                if (res instanceof CompoundTag) return (CompoundTag) res;
            } catch (final NoSuchMethodException ignored) {
            }
            try {
                final Method m = be.getClass().getMethod("writeNbt", CompoundTag.class);
                m.invoke(be, tag);
                return tag;
            } catch (final NoSuchMethodException ignored) {
            }
            try {
                final Method m = be.getClass().getMethod("serializeNBT");
                final Object res = m.invoke(be);
                if (res instanceof CompoundTag) return (CompoundTag) res;
            } catch (final NoSuchMethodException ignored) {
            }
        } catch (final Throwable t) {
            // swallow and return null below
        }
        return null;
    }

    // --- Compatibility helpers to make incremental migration easier ---

    /**
     * Compatibility: mirror old Fastener.update() semantics. Default: no-op.
     */
    public boolean update() {
        // No internal ticking yet; return false (no change)
        return false;
    }

    /**
     * Compatibility: provide old serializeNBT name.
     */
    public CompoundTag serializeNBT() {
        return this.save();
    }

    /**
     * Compatibility: provide old deserializeNBT name.
     */
    public void deserializeNBT(final CompoundTag tag) {
        this.load(tag);
    }

    /**
     * Compatibility: set world on backed component; a no-op for now but present for API parity.
     */
    public void setWorld(final Level world) {
        // No-op until component needs world reference
    }

    /**
     * Compatibility: drop items stored by a fastener. No-op placeholder.
     */
    public void dropItems(final Level world, final BlockPos pos) {
        // No-op placeholder until items are modelled in the component
    }

    /**
     * Compatibility: return all connections. Placeholder returns empty list.
     */
    public List<Object> getAllConnections() {
        return Collections.emptyList();
    }

    /**
     * Compatibility: return first connection if present. Placeholder returns empty.
     */
    public Optional<Object> getFirstConnection() {
        return Optional.empty();
    }

    /**
     * Convenience accessor for the raw component data. May return null if data is intentionally empty.
     */
    @Nullable
    public CompoundTag getData() {
        return this.data;
    }
}
