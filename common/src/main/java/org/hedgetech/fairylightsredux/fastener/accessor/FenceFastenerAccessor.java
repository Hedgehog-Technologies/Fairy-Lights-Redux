package org.hedgetech.fairylightsredux.fastener.accessor;

import org.hedgetech.fairylightsredux.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.fastener.EntityFastener;
import org.hedgetech.fairylightsredux.fastener.FastenerType;

public final class FenceFastenerAccessor extends EntityFastenerAccessor<FenceFastenerEntity> {
    public FenceFastenerAccessor() {
        super(FenceFastenerEntity.class);
    }

    public FenceFastenerAccessor(final EntityFastener<FenceFastenerEntity> fastener) {
        super(FenceFastenerEntity.class, fastener);
    }

    @Override
    public FastenerType getType() {
        return FastenerType.FENCE;
    }
}
