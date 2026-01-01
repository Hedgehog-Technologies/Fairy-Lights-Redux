package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.Constants;

public final class FLRItems {
    public static final DeferredRegister<Item> REG = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);

    public static final RegistryObject<Item> HANGING_LIGHTS = REG.register("hanging_lights", () -> new HangingLightsConnectionItem(defaultProperties()));

    private static Item.Properties defaultProperties() {
        return new Item.Properties();
    }

    private FLRItems() {}
}
