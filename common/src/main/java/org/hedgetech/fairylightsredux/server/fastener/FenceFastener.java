package org.hedgetech.fairylightsredux.server.fastener;

import net.minecraft.core.BlockPos;
import org.hedgetech.fairylightsredux.server.fastener.accessor.EntityFastenerAccessor;
import org.hedgetech.fairylightsredux.server.fastener.accessor.FenceFastenerAccessor;
import org.hedgetech.fairylightsredux.server.entity.FenceFastenerEntity;

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
