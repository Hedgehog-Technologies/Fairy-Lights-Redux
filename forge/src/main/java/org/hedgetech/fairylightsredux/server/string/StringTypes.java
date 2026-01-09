package org.hedgetech.fairylightsredux.server.string;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.Constants;

public final class StringTypes {
    public static final DeferredRegister<StringType> REG = DeferredRegister.create(StringType.KEY, Constants.MOD_ID);

    public static final RegistryObject<StringType> BLACK_STRING = REG.register("black_string", () -> new StringType(0x323232));
    public static final RegistryObject<StringType> WHITE_STRING = REG.register("white_string", () -> new StringType(0xF0F0F0));

    private StringTypes() {}
}
