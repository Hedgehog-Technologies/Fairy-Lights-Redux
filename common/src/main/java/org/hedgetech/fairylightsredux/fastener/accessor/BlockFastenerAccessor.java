package org.hedgetech.fairylightsredux.fastener.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.fastener.BlockFastener;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.fastener.FastenerType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class BlockFastenerAccessor implements FastenerAccessor {
    private BlockPos pos = BlockPos.ZERO;

    public BlockFastenerAccessor() {}

    public BlockFastenerAccessor(final BlockFastener fastener) {
        this(fastener.getPos());
    }

    public BlockFastenerAccessor(final BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public Optional<Fastener<?>> get(final Level world, final boolean load) {
        if (load || world.isLoaded(this.pos)) {
            final BlockEntity entity = world.getBlockEntity(this.pos);
            if (entity != null) {
                // FIXME - Implement retrieval of Fastener from BlockEntity
                throw new NotImplementedException("BlockFastenerAccessor.get - Fastener retrieval from BlockEntity");
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        if (world.isClientSide() || !world.isLoaded(this.pos)) return false;
        final BlockEntity entity = world.getBlockEntity(this.pos);
        return entity == null /*|| FIXME - implement retrieval of fastener from BlockEntity */;
    }

    @Override
    public FastenerType getType() {
        return FastenerType.BLOCK;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) return true;
        if (obj instanceof BlockFastenerAccessor bfa) return this.pos.equals(bfa.pos);
        return false;
    }

    @Deprecated(since = "Compound tags be damned")
    @Override
    public CompoundTag serialize() {
        throw new NotImplementedException("BlockFastenerAccessor.serialize");
    }

    @Deprecated(since = "Compound tags be damned")
    @Override
    public void deserialize(final CompoundTag tag) {
        throw new NotImplementedException("BlockFastenerAccessor.deserialize");
    }
}
