package org.hedgetech.fairylightsredux.server.connection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.FLRNames;
import org.hedgetech.fairylightsredux.server.item.FLRItems;

public final class ConnectionTypes {
    private static final BiMap<ResourceLocation, ConnectionType<?>> TYPES = HashBiMap.create();

    public static final ConnectionType<HangingLightsConnection> HANGING_LIGHTS = register(FLRNames.HANGING_LIGHTS, new ConnectionType<>(HangingLightsConnection::new, () -> FLRItems.HANGING_LIGHTS));
    public static final ConnectionType<GarlandVineConnection> VINE_GARLAND = register(FLRNames.VINE_GARLAND, new ConnectionType<>(GarlandVineConnection::new, () -> FLRItems.GARLAND));
    public static final ConnectionType<GarlandTinselConnection> TINSEL_GARLAND = register(FLRNames.TINSEL_GARLAND, new ConnectionType<>(GarlandTinselConnection::new, () -> FLRItems.TINSEL));
    public static final ConnectionType<PennantBuntingConnection> PENNANT_BUNTING = register(FLRNames.PENNANT_BUNTING, new ConnectionType<>(PennantBuntingConnection::new, () -> FLRItems.PENNANT_BUNTING));
    public static final ConnectionType<LetterBuntingConnection> LETTER_BUNTING = register(FLRNames.LETTER_BUNTING, new ConnectionType<>(LetterBuntingConnection::new, () -> FLRItems.LETTER_BUNTING));

    private static <T extends Connection> ConnectionType<T> register(final String name, final ConnectionType<T> type) {
        TYPES.put(Constants.id(name), type);
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
