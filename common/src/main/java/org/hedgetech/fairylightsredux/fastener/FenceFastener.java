package org.hedgetech.fairylightsredux.fastener;

import net.minecraft.core.BlockPos;
import org.hedgetech.fairylightsredux.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.fastener.accessor.EntityFastenerAccessor;

public final class FenceFastener extends EntityFastener<FenceFastenerEntity> {
    public FenceFastener(final FenceFastenerEntity entity) {
        super(entity);
    }

    @Override
    public EntityFastenerAccessor<FenceFastenerEntity> createAccessor() {
        return new FenceFastenerAccessor(this);
    }

    @Override
    public BlockPos getPos() {
        return this.entity.getPos();
    }

    @Override
    public boolean isMoving() {
        return false;
    }
}
