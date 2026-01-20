package org.hedgetech.fairylightsredux.server.fastener.accessor;

import org.hedgetech.fairylightsredux.server.capability.CapabilityHandler;
import org.hedgetech.fairylightsredux.server.fastener.BlockFastener;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;
import org.hedgetech.fairylightsredux.util.NbtHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

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
    public LazyOptional<Fastener<?>> get(final Level world, final boolean load) {
        if (load || world.isLoaded(this.pos)) {
            final BlockEntity entity = world.getBlockEntity(this.pos);
            if (entity != null) {
                return entity.getCapability(CapabilityHandler.FASTENER_CAP);
            }
        }
        return LazyOptional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        if (world.isClientSide() || !world.isLoaded(this.pos)) return false;
        final BlockEntity entity = world.getBlockEntity(this.pos);
        return entity == null || !entity.getCapability(CapabilityHandler.FASTENER_CAP).isPresent();
    }

    @Override
    public FastenerType getType() {
        return FastenerType.BLOCK;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof BlockFastenerAccessor accessor) {
            return this.pos.equals(accessor.pos);
        }
        return false;
    }

    @Override
    public CompoundTag serialize() {
        return NbtHelpers.writeBlockPos(this.pos);
    }

    @Override
    public void deserialize(final CompoundTag nbt) {
        this.pos = NbtHelpers.readBlockPos(nbt);
    }
}
