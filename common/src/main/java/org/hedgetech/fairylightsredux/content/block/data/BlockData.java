package org.hedgetech.fairylightsredux.content.block.data;

import org.hedgetech.fairylightsredux.content.block.BlockDef;

public sealed interface BlockData
    permits CandleData {
    static <T extends BlockData> T requireData(BlockDef def, Class<T> type) {
        return type.cast(def.data());
    }
}
