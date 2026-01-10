package org.hedgetech.fairylightsredux.server.connection;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.server.item.PennantBuntingConnectionItem;

public final class ConnectionTypes {
    public static final DeferredRegister<ConnectionType<?>> REG = DeferredRegister.create(Constants.CONNECTION_TYPE, Constants.MOD_ID);

    public static final RegistryObject<ConnectionType<HangingLightConnection>> HANGING_LIGHTS = REG.register("hanging_lights",
            () -> ConnectionType.Builder.create(HangingLightConnection::new).item(FLRItems.HANGING_LIGHTS).build()
    );

    public static final RegistryObject<ConnectionType<GarlandVineConnection>> VINE_GARLAND = REG.register("vine_garland",
            () -> ConnectionType.Builder.create(GarlandVineConnection::new).item(FLRItems.GARLAND).build()
    );

    public static final RegistryObject<ConnectionType<GarlandVineConnection>> TINSEL_GARLAND = REG.register("tinsel_garland",
            ()  -> ConnectionType.Builder.create(GarlandTinselConnection::new).item(FLRItems.TINSEL).build()
    );

    public static final RegistryObject<ConnectionType<PennantBuntingConnection>> PENNANT_BUNTING = REG.register("pennant_bunting",
            () -> ConnectionType.Builder.create(PennantBuntingConnection::new).item(FLRItems.PENNANT_BUNTING).build()
    );

    public static final RegistryObject<ConnectionType<LetterBuntingConnection>> LETTER_BUNTING = REG.register("letter_bunting",
            () -> ConnectionType.Builder.create(LetterBuntingConnection::new).item(FLRItems.LETTER_BUNTING).build()
    );

    private ConnectionTypes() {}
}
