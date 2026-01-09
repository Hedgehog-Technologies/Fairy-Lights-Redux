package org.hedgetech.fairylightsredux.server.item;

import org.hedgetech.fairylightsredux.server.connection.ConnectionTypes;

public final class GarlandConnectionItem extends ConnectionItem {
    public GarlandConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.VINE_GARLAND);
    }
}
