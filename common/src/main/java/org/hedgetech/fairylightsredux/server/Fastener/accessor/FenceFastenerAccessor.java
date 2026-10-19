package org.hedgetech.fairylightsredux.server.Fastener.accessor;

import org.hedgetech.fairylightsredux.server.Fastener.EntityFastener;
import org.hedgetech.fairylightsredux.server.Fastener.FastenerType;
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
