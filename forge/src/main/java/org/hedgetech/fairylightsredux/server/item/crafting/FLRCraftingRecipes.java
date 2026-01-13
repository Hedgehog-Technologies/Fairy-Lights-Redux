package org.hedgetech.fairylightsredux.server.item.crafting;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.data.GenericRecipeBuilder;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.hedgetech.fairylightsredux.util.OreDictUtils;
import org.hedgetech.fairylightsredux.util.crafting.GenericRecipe;
import org.hedgetech.fairylightsredux.util.crafting.ingredient.BasicRegularIngredient;
import org.hedgetech.fairylightsredux.util.crafting.ingredient.LazyTagIngredient;
import org.hedgetech.fairylightsredux.util.crafting.ingredient.RegularIngredient;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public final class FLRCraftingRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> REG = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MOD_ID);

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> HANGING_LIGHTS = REG.register("crafting_special_hanging_lights", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createHangingLights));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> HANGING_LIGHTS_AUGMENTATION = REG.register("crafting_special_hanging_lights_augmentation", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createHangingLightsAugmentation));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> TINSEL_GARLAND = REG.register("crafting_special_tinsel_garland", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createTinselGarland));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> PENNANT_BUNTING = REG.register("crafting_special_pennant_bunting", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createPennantBunting));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> PENNANT_BUNTING_AUGMENTATION = REG.register("crafting_special_pennant_bunting_augmentation", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createPennantBuntingAugmentation));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> TRIANGLE_PENNANT = REG.register("crafting_special_triangle_pennant", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createTrianglePennant));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> SPEARHEAD_PENNANT = REG.register("crafting_special_spearhead_pennant", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createSpearheadPennant));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> SWALLOWTAIL_PENNANT = REG.register("crafting_special_swallowtail_pennant", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createSwallowtailPennant));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> SQUARE_PENNANT = REG.register("crafting_special_square_pennant", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createSquarePennant));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> FAIRY_LIGHT = REG.register("crafting_special_fairy_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createFairyLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> PAPER_LANTERN = REG.register("crafting_special_paper_lantern", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createPaperLantern));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> ORB_LANTERN = REG.register("crafting_special_orb_lantern", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createOrbLantern));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> FLOWER_LIGHT = REG.register("crafting_special_flower_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createFlowerLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> CANDLE_LANTERN_LIGHT = REG.register("crafting_special_candle_lantern_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createCandleLanternLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> OIL_LANTERN_LIGHT = REG.register("crafting_special_oil_lantern_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createOilLanternLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> JACK_O_LANTERN = REG.register("crafting_special_jack_o_lantern", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createJackOLantern));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> SKULL_LIGHT = REG.register("crafting_special_skull_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createSkullLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> GHOST_LIGHT = REG.register("crafting_special_ghost_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createGhostLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> SPIDER_LIGHT = REG.register("crafting_special_spider_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createSpiderLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> WITCH_LIGHT = REG.register("crafting_special_witch_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createWitchLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> SNOWFLAKE_LIGHT = REG.register("crafting_special_snowflake_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createSnowflakeLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> HEART_LIGHT = REG.register("crafting_special_heart_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createHeartLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> MOON_LIGHT = REG.register("crafting_special_moon_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createMoonLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> STAR_LIGHT = REG.register("crafting_special_star_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createStarLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> ICICLE_LIGHTS = REG.register("crafting_special_icicle_lights", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createIcicleLights));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> METEOR_LIGHT = REG.register("crafting_special_meteor_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createMeteorLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> LIGHT_TWINKLE = REG.register("crafting_special_light_twinkle", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createLightTwinkle));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> COLOR_CHANGING_LIGHT = REG.register("crafting_special_color_changing_light", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createColorChangingLight));

    public static final RegistryObject<RecipeSerializer<GenericRecipe>> EDIT_COLOR = REG.register("crafting_special_edit_color", () -> new SimpleCraftingRecipeSerializer<>(FLRCraftingRecipes::createDyeColor));

    public static final RegistryObject<? extends RecipeSerializer<CustomRecipe>> COPY_COLOR = REG.register("crafting_special_copy_color", () -> new SimpleCraftingRecipeSerializer<>(CopyColorRecipe::new));

    public static final TagKey<Item> LIGHTS = ItemTags.create(ResourceLocation.parse(Constants.MOD_ID + ":lights"));

    public static final TagKey<Item> TWINKLING_LIGHTS = ItemTags.create(ResourceLocation.parse(Constants.MOD_ID + ":twinkling_lights"));

    public static final TagKey<Item> PENNANTS = ItemTags.create(ResourceLocation.parse(Constants.MOD_ID + ":pennants"));

    public static final TagKey<Item> DYEABLE = ItemTags.create(ResourceLocation.parse(Constants.MOD_ID + ":dyeable"));

    public static final TagKey<Item> DYEABLE_LIGHTS = ItemTags.create(ResourceLocation.parse(Constants.MOD_ID + ":dyeable_lights"));

    public static final RegularIngredient DYE_SUBTYPE_INGREDIENT = new BasicRegularIngredient(LazyTagIngredient.of(Tags.Items.DYES)) {
        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            return DyeableItem.getDyeColor(output).map(dye -> ImmutableList.of(OreDictUtils.getDyes(dye))).orElse(ImmutableList.of());
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public void matched(final ItemStack ingredient, final CompoundTag nbt) {
            throw new NotImplementedException("DYE_SUBTYPE_INGREDIENT.matched");
        }
    };

    private static GenericRecipe createDyeColor(final ResourceLocation name, CraftingBookCategory category) {
        return new GenericRecipeBuilder(name, EDIT_COLOR)
                .withShape("I")
                .withIngredient
    }

    private FLRCraftingRecipes() {}
}
