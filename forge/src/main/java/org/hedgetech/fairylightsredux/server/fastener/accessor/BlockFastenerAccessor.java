package org.hedgetech.fairylightsredux.server.fastener.accessor;

import net.minecraft.core.BlockPos;

public final class BlockFastenerAccessor implements FastenerAccessor {
    private BlockPos pos = BlockPos.ZERO;

    public BlockFastenerAccessor() {}

    public BlockFastenerAccessor(final BlockFastener fastener) {
        this(fastener.getPos());
    }
}
