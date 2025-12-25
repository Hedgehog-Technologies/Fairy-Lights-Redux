package org.hedgetech.fairylightsredux.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public final class NbtHelpers {
    public static CompoundTag writeBlockPos(BlockPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("X", pos.getX());
        tag.putInt("Y", pos.getY());
        tag.putInt("Z", pos.getZ());
        return tag;
    }

    public static BlockPos readBlockPos(CompoundTag tag) {
        var x = tag.getInt("X").orElse(0);
        var y = tag.getInt("Y").orElse(0);
        var z = tag.getInt("Z").orElse(0);
        return new BlockPos(x, y, z);
    }

    private NbtHelpers() {}
}
