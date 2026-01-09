package org.hedgetech.fairylightsredux.server.connection;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.item.FLRItems;

public final class ConnectionTypes {
    public static final DeferredRegister<ConnectionType<?>> REG = DeferredRegister.create(Constants.CONNECTION_TYPE, Constants.MOD_ID);

    public static final RegistryObject<ConnectionType<HangingLightConnection>> HANGING_LIGHTS = REG.register("hanging_lights",
            () -> ConnectionType.Builder.create(HangingLightConnection::new).item(FLRItems.HANGING_LIGHTS).build()
    );

    public static final RegistryObject<ConnectionType<GarlandVineConnection>> VINE_GARLAND = REG.register("vine_garland",
            () -> ConnectionType.Builder.create(GarlandVineConnection::new).item(FLRItems.GARLAND).build()
    );

    private ConnectionTypes() {}
}
