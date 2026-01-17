package org.hedgetech.fairylightsredux.content.item.data;

import org.hedgetech.fairylightsredux.content.item.ItemDef;

public sealed interface ItemData
    permits HammerData, CandleData {

    static <T extends ItemData> T requireData(ItemDef def, Class<T> type) {
        return type.cast(def.data());
    }
}
