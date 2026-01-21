package org.hedgetech.fairylightsredux.fastener;

import javax.annotation.Nullable;

public interface FastenerHolder {
    @Nullable Fastener<?> getFastener();
}
