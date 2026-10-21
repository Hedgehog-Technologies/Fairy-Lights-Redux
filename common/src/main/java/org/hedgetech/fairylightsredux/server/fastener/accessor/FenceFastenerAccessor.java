package org.hedgetech.fairylightsredux.server.fastener.accessor;

import org.hedgetech.fairylightsredux.server.fastener.EntityFastener;
import org.hedgetech.fairylightsredux.server.fastener.FastenerType;
import org.hedgetech.fairylightsredux.server.entity.FenceFastenerEntity;

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
