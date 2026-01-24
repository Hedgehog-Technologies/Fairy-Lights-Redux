package org.hedgetech.fairylightsredux.fastener.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.content.block.FastenerBlockEntity;
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
            if (entity instanceof FastenerBlockEntity fbe) {
                return Optional.of(fbe.getFastener());
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        if (world.isClientSide() || !world.isLoaded(this.pos)) return false;
        final BlockEntity entity = world.getBlockEntity(this.pos);
        return entity == null || this.get(world, false).isEmpty();
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

    @Override
    public void writeToBuf(final FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public BlockFastenerAccessor readFromBuf(final FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        return this;
    }

    @Override
    public CompoundTag serialize() {
        throw new NotImplementedException("BlockFastenerAccessor.serialize");
    }

    @Override
    public void deserialize(final CompoundTag tag) {
        throw new NotImplementedException("BlockFastenerAccessor.deserialize");
    }
}
