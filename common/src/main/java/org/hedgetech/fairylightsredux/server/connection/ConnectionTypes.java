package org.hedgetech.fairylightsredux.server.connection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;

public final class ConnectionTypes {
    private static final BiMap<ResourceLocation, ConnectionType<?>> TYPES = HashBiMap.create();

    private static <T extends Connection> ConnectionType<T> register(final String name, final ConnectionType<T> type) {
        TYPES.put(Constants.id("name"), type);
        return type;
    }

    public static ConnectionType<?> get(final ResourceLocation id) {
        return TYPES.get(id);
    }

    public static ResourceLocation getId(final ConnectionType<?> type) {
        return TYPES.inverse().get(type);
    }

    private ConnectionTypes() {}
}
