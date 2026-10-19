package org.hedgetech.fairylightsredux.server.Fastener.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.hedgetech.fairylightsredux.server.Fastener.BlockFastener;
import org.hedgetech.fairylightsredux.server.Fastener.Fastener;
import org.hedgetech.fairylightsredux.server.Fastener.FastenerType;
import org.hedgetech.fairylightsredux.server.Fastener.IFastenerHolder;

import java.util.Optional;

public final class BlockFastenerAccessor implements IFastenerAccessor {
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
        if ((load || world.isLoaded(this.pos)) && world.getBlockEntity(this.pos) instanceof final IFastenerHolder holder) {
            return Optional.of(holder.getFastener());
        }
        return Optional.empty();
    }

    @Override
    public boolean isGone(final Level world) {
        if (world.isClientSide() || !world.isLoaded(this.pos)) return false;
        return !(world.getBlockEntity(this.pos) instanceof IFastenerHolder);
    }

    @Override
    public FastenerType getType() {
        return FastenerType.BLOCK;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj instanceof final BlockFastenerAccessor other) return this.pos.equals(other.pos);
        return false;
    }

    @Override
    public int hashCode() {
        return this.pos.hashCode();
    }

    @Override
    public void save(final ValueOutput output) {
        output.store("pos", BlockPos.CODEC, this.pos);
    }

    @Override
    public boolean load(final ValueInput input) {
        final Optional<BlockPos> pos = input.read("pos", BlockPos.CODEC);
        pos.ifPresent(p -> this.pos = p);
        return pos.isPresent();
    }
}
