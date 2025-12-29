package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.Constants;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class FLRCraftingRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> REG = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<RecipeSerializer<>>

    private FLRCraftingRecipes() {}
}
