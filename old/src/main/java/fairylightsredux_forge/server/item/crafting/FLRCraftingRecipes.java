package org.hedgetech.fairylightsredux.server.item.crafting;

import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.hedgetech.fairylightsredux.server.item.FLRItems;
import org.hedgetech.fairylightsredux.util.Blender;
import org.hedgetech.fairylightsredux.util.OreDictUtils;
import org.hedgetech.fairylightsredux.util.Utils;
import org.hedgetech.fairylightsredux.util.crafting.GenericRecipe;
import org.hedgetech.fairylightsredux.util.crafting.ingredient.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

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

    private static GenericRecipe createDyeColor(CraftingBookCategory category) {
        return new GenericRecipeBuilder(EDIT_COLOR)
                .withShape("I")
                .withIngredient('I', DYEABLE)
                .withOutput('I')
                .withAuxiliaryIngredient(new BasicAuxiliaryIngredient<Blender>(LazyTagIngredient.of(Tags.Items.DYES), true, 8) {
                    @Override
                    public Blender accumulator() {
                        return new Blender();
                    }

                    @Override
                    public void consume(final Blender data, final ItemStack ingredient) {
                        data.add(DyeableItem.getColor(OreDictUtils.getDyeColor(ingredient)));
                    }

                    @Override
                    public boolean finish(final Blender data, final CompoundTag nbt) {
                        throw new NotImplementedException("createDyeColor.BasicAuxiliaryIngredient.finish");
                    }
                })
                .build();
    }

    private static GenericRecipe createLightTwinkle(CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(LIGHT_TWINKLE)
                .withShape("L")
                .withIngredient('L', TWINKLING_LIGHTS).withOutput('L')
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(LazyTagIngredient.of(Tags.Items.DUSTS_GLOWSTONE), true, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return useInputsForTagBool(output, "twinkle", true) ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final CompoundTag nbt) {
                        nbt.putBoolean("twinkle", true);
                    }

                    @Override
                    public void absent(final CompoundTag nbt) {
                        nbt.putBoolean("twinkle", false);
                    }

                    @Override
                    public void addTooltip(final List<Component> tooltip) {
                        super.addTooltip(tooltip);
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.twinkling_lights.glowstone"));
                    }
                })
                .build();
    }

    private static GenericRecipe createColorChangingLight(CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(COLOR_CHANGING_LIGHT)
                .withShape("IG")
                .withIngredient('I', DYEABLE_LIGHTS).withOutput('I')
                .withIngredient('G', Tags.Items.NUGGETS_GOLD)
                .withAuxiliaryIngredient(new BasicAuxiliaryIngredient<ListTag>(LazyTagIngredient.of(Tags.Items.DYES), true, 8) {
                    @Override
                    public ListTag accumulator() {
                        return new ListTag();
                    }

                    @Override
                    public void consume(final ListTag data, final ItemStack ingredient) {
                        data.add(IntTag.valueOf(DyeableItem.getColor(OreDictUtils.getDyeColor(ingredient))));
                    }

                    @Override
                    public boolean finish(final ListTag data, final CompoundTag nbt) {
                        throw new NotImplementedException("createColorChangingLight.BasicAuxiliaryIngredient.finish");
                    }
                })
                .build();
    }

    private static GenericRecipe createHangingLights(CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(HANGING_LIGHTS, FLRItems.HANGING_LIGHTS.get())
                .withShape("I-I")
                .withIngredient('I', Tags.Items.INGOTS_IRON)
                .withIngredient('-', Tags.Items.STRINGS)
                .withAuxiliaryIngredient(new LightIngredient(true))
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(LazyTagIngredient.of(Tags.Items.DYES_WHITE), false, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
//                        final CompoundTag tag = output.getTag();
//                        return tag != null && HangingLightsConnectionItem.getString(tag) == StringTypes.WHITE_STRING.get() ? super.getInput(output) : ImmutableList.of();
                        throw new NotImplementedException("createHangingLights.BasicAuxiliaryIngredient.getInput");
                    }

                    @Override
                    public void present(final CompoundTag nbt) {
                        throw new NotImplementedException("createHangingLights.BasicAuxiliaryIngredient.present");
                    }

                    @Override
                    public void absent(final CompoundTag nbt) {
                        throw new NotImplementedException("createHangingLights.BasicAuxiliaryIngredient.absent");
                    }

                    @Override
                    public void addTooltip(final List<Component> tooltip) {
                        super.addTooltip(tooltip);
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.string"));
                    }
                })
                .build();
    }

    private static boolean useInputsForTagBool(final ItemStack output, final String key, final boolean value) {
        throw new NotImplementedException("FLRCraftingRecipes.useInputsForTagBool");
    }

    /*
     *  The JEI shown recipe is adding glowstone, eventually I should allow a recipe to provide a number of
     *  different recipe layouts the the input ingredients can be generated for so I could show applying a
     *  new light pattern as well.
     */
    private static GenericRecipe createHangingLightsAugmentation(CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(HANGING_LIGHTS_AUGMENTATION, FLRItems.HANGING_LIGHTS.get())
                .withShape("F")
                .withIngredient('F', new BasicRegularIngredient(Ingredient.of(FLRItems.HANGING_LIGHTS.get())) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        throw new NotImplementedException("createHangingLightsAugmentation.BasicRegularIngredient.getInputs");
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        throw new NotImplementedException("createHangingLightsAugmentation.BasicRegularIngredient.getInput");
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final CompoundTag nbt) {
                        throw new NotImplementedException("createHangingLightsAugmentation.BasicRegularIngredient.matched");
                    }
                })
                .withAuxiliaryIngredient(new LightIngredient(true) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return ImmutableList.of();
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return ImmutableList.of();
                    }
                })
                .build();
    }

    private static ImmutableList<ItemStack> makeHangingLightsExamples(final ItemStack stack) {
        return ImmutableList.of(
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.MAGENTA, DyeColor.CYAN, DyeColor.WHITE),
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.LIGHT_BLUE, DyeColor.CYAN, DyeColor.LIGHT_BLUE),
                makeHangingLights(stack, DyeColor.LIGHT_GRAY, DyeColor.PINK, DyeColor.CYAN, DyeColor.GREEN),
                makeHangingLights(stack, DyeColor.LIGHT_GRAY, DyeColor.PURPLE, DyeColor.LIGHT_GRAY, DyeColor.GREEN),
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.YELLOW, DyeColor.CYAN, DyeColor.PURPLE)
        );
    }

    public static ItemStack makeHangingLights(final ItemStack base, final DyeColor... colors) {
        throw new NotImplementedException("FLRCraftingRecipes.makeHangingLights");
    }

    private static GenericRecipe createTinselGarland(CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(TINSEL_GARLAND, FLRItems.TINSEL.get())
                .withShape(" P ", "I-I", " D ")
                .withIngredient('P', Items.PAPER)
                .withIngredient('I', Tags.Items.INGOTS_IRON)
                .withIngredient('-', Tags.Items.STRINGS)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .build();
    }

    private static GenericRecipe createPennantBunting(CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(PENNANT_BUNTING, FLRItems.PENNANT_BUNTING.get())
                .withShape("I-I")
                .withIngredient('I', Tags.Items.INGOTS_IRON)
                .withIngredient('-', Tags.Items.STRINGS)
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }

    private static GenericRecipe createPennantBuntingAugmentation(CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(PENNANT_BUNTING_AUGMENTATION, FLRItems.PENNANT_BUNTING.get())
                .withShape("B")
                .withIngredient('B', new BasicRegularIngredient(Ingredient.of(FLRItems.PENNANT_BUNTING.get())) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        throw new NotImplementedException("createPennantBuntingAugmentation.BasicRegularIngredient.getInputs");
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        throw new NotImplementedException("createPennantBuntingAugmentation.BasicRegularIngredient.getInput");
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final CompoundTag nbt) {
                        throw new NotImplementedException("createPennantBuntingAugmentation.BasicRegularIngredient.matched");
                    }
                })
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }

    private static ImmutableList<ItemStack> makePennantExamples(final ItemStack stack) {
        return ImmutableList.of(
                makePennant(stack, DyeColor.BLUE, DyeColor.YELLOW, DyeColor.RED),
                makePennant(stack, DyeColor.PINK, DyeColor.LIGHT_BLUE),
                makePennant(stack, DyeColor.ORANGE, DyeColor.WHITE),
                makePennant(stack, DyeColor.LIME, DyeColor.YELLOW)
        );
    }

    public static ItemStack makePennant(final ItemStack base, final DyeColor... colors) {
        throw new NotImplementedException("FLRCraftingRecipes.makePennant");
    }

    private static GenericRecipe createPennant(final Supplier<RecipeSerializer<GenericRecipe>> serializer, final Item item, final String pattern) {
        return new GenericRecipeBuilder(serializer, item)
                .withShape("- -", "PDP", pattern)
                .withIngredient('P', Items.PAPER)
                .withIngredient('-', Tags.Items.STRINGS)
                .withIngredient('D', DYE_SUBTYPE_INGREDIENT)
                .build();
    }

    private static GenericRecipe createTrianglePennant(CraftingBookCategory craftingBookCategory) {
        return createPennant(TRIANGLE_PENNANT, FLRItems.TRIANGLE_PENNANT.get(), " P ");
    }

    private static GenericRecipe createSpearheadPennant(CraftingBookCategory craftingBookCategory) {
        return createPennant(SPEARHEAD_PENNANT, FLRItems.SPEARHEAD_PENNANT.get(), " PP");
    }

    private static GenericRecipe createSwallowtailPennant(CraftingBookCategory craftingBookCategory) {
        return createPennant(SWALLOWTAIL_PENNANT, FLRItems.SWALLOWTAIL_PENNANT.get(), "P P");
    }

    private static GenericRecipe createSquarePennant(CraftingBookCategory craftingBookCategory) {
        return createPennant(SQUARE_PENNANT, FLRItems.SQUARE_PENNANT.get(), "PPP");
    }

    private static GenericRecipe createFairyLight(CraftingBookCategory craftingBookCategory) {
        return createLight(FAIRY_LIGHT, FLRItems.FAIRY_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Tags.Items.GLASS_PANES_COLORLESS)
        );
    }

    private static GenericRecipe createPaperLantern(CraftingBookCategory craftingBookCategory) {
        return createLight(PAPER_LANTERN, FLRItems.PAPER_LANTERN, b -> b
                .withShape(" I ", "PDP", "PPP")
                .withIngredient('P', Items.PAPER)
        );
    }

    private static GenericRecipe createOrbLantern(CraftingBookCategory craftingBookCategory) {
        return createLight(ORB_LANTERN, FLRItems.ORB_LANTERN, b -> b
                .withShape(" I ", "SDS", " W ")
                .withIngredient('S', Tags.Items.STRINGS)
                .withIngredient('W', Items.WHITE_WOOL)
        );
    }

    private static GenericRecipe createFlowerLight(CraftingBookCategory craftingBookCategory) {
        return createLight(FLOWER_LIGHT, FLRItems.FLOWER_LIGHT, b -> b
                .withShape(" I ", "RDB", " Y ")
                .withIngredient('R', Items.POPPY)
                .withIngredient('Y', Items.DANDELION)
                .withIngredient('B', Items.BLUE_ORCHID)
        );
    }

    private static GenericRecipe createCandleLanternLight(CraftingBookCategory craftingBookCategory) {
        return createLight(CANDLE_LANTERN_LIGHT, FLRItems.CANDLE_LANTERN_LIGHT, b -> b
                .withShape(" I ", "GDG", "IGI")
                .withIngredient('G', Tags.Items.NUGGETS_GOLD)
        );
    }

    private static GenericRecipe createOilLanternLight(CraftingBookCategory craftingBookCategory) {
        return createLight(OIL_LANTERN_LIGHT, FLRItems.OIL_LANTERN_LIGHT, b -> b
                .withShape(" I ", "SDS", "IGI")
                .withIngredient('S', Items.STICK)
                .withIngredient('G', Tags.Items.GLASS_PANES_COLORLESS)
        );
    }

    private static GenericRecipe createJackOLantern(CraftingBookCategory craftingBookCategory) {
        return createLight(JACK_O_LANTERN, FLRItems.JACK_O_LANTERN, b -> b
                .withShape(" I ", "SDS", "GPG")
                .withIngredient('S', ItemTags.WOODEN_SLABS)
                .withIngredient('G', Items.TORCH)
                .withIngredient('P', Items.JACK_O_LANTERN)
        );
    }

    private static GenericRecipe createSkullLight(CraftingBookCategory craftingBookCategory) {
        return createLight(SKULL_LIGHT, FLRItems.SKULL_LIGHT, b -> b
                .withShape(" I ", "IDI", " B ")
                .withIngredient('B', Tags.Items.BONES)
        );
    }

    private static GenericRecipe createGhostLight(CraftingBookCategory craftingBookCategory) {
        return createLight(GHOST_LIGHT, FLRItems.GHOST_LIGHT, b -> b
                .withShape(" I ", "PDP", "IGI")
                .withIngredient('P', Items.PAPER)
                .withIngredient('G', Arrays.asList(Tags.Items.GLASS_PANES, Tags.Items.DYED_WHITE))
        );
    }

    private static GenericRecipe createSpiderLight(CraftingBookCategory craftingBookCategory) {
        return createLight(SPIDER_LIGHT, FLRItems.SPIDER_LIGHT, b -> b
                .withShape(" I ", "WDW", "SES")
                .withIngredient('W', Items.COBWEB)
                .withIngredient('S', Tags.Items.STRINGS)
                .withIngredient('E', Items.SPIDER_EYE)
        );
    }

    private static GenericRecipe createWitchLight(CraftingBookCategory craftingBookCategory) {
        return createLight(WITCH_LIGHT, FLRItems.WITCH_LIGHT, b -> b
                .withShape(" I ", "BDW", " S ")
                .withIngredient('B', Items.GLASS_BOTTLE)
                .withIngredient('W', Items.WHEAT)
                .withIngredient('S', Items.STICK)
        );
    }

    private static GenericRecipe createSnowflakeLight(CraftingBookCategory craftingBookCategory) {
        return createLight(SNOWFLAKE_LIGHT, FLRItems.SNOWFLAKE_LIGHT, b -> b
                .withShape(" I ", "SDS", " G ")
                .withIngredient('S', Items.SNOWBALL)
                .withIngredient('G', Arrays.asList(Tags.Items.GLASS_PANES, Tags.Items.DYED_WHITE))
        );
    }

    private static GenericRecipe createHeartLight(CraftingBookCategory craftingBookCategory) {
        return createLight(HEART_LIGHT, FLRItems.HEART_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Arrays.asList(Tags.Items.GLASS_PANES, Tags.Items.DYED_RED))
        );
    }

    private static GenericRecipe createMoonLight(CraftingBookCategory craftingBookCategory) {
        return createLight(MOON_LIGHT, FLRItems.MOON_LIGHT, b -> b
                .withShape(" I ", "GDG", " C ")
                .withIngredient('G', Arrays.asList(Tags.Items.GLASS_PANES, Tags.Items.DYED_WHITE))
                .withIngredient('C', Items.CLOCK)
        );
    }


    private static GenericRecipe createStarLight(CraftingBookCategory craftingBookCategory) {
        return createLight(STAR_LIGHT, FLRItems.STAR_LIGHT, b -> b
                .withShape(" I ", "PDP", " G ")
                .withIngredient('P', Arrays.asList(Tags.Items.GLASS_PANES, Tags.Items.DYED_WHITE))
                .withIngredient('G', Tags.Items.NUGGETS_GOLD)
        );
    }

    private static GenericRecipe createIcicleLights(CraftingBookCategory craftingBookCategory) {
        return createLight(ICICLE_LIGHTS, FLRItems.ICICLE_LIGHTS, b -> b
                .withShape(" I ", "GDG", " B ")
                .withIngredient('G', Tags.Items.GLASS_PANES_COLORLESS)
                .withIngredient('B', Items.WATER_BUCKET)
        );
    }

    private static GenericRecipe createMeteorLight(CraftingBookCategory craftingBookCategory) {
        return createLight(METEOR_LIGHT, FLRItems.METEOR_LIGHT, b -> b
                .withShape(" I ", "GDG", "IPI")
                .withIngredient('G', Tags.Items.DUSTS_GLOWSTONE)
                .withIngredient('P', Items.PAPER)
        );
    }

    private static GenericRecipe createLight(final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer, final Supplier<? extends Item> variant, final UnaryOperator<GenericRecipeBuilder> recipe) {
        return recipe.apply(new GenericRecipeBuilder(serializer))
                .withIngredient('I', Tags.Items.INGOTS_IRON)
                .withIngredient('D', FLRCraftingRecipes.DYE_SUBTYPE_INGREDIENT)
                .withOutput(variant.get(), 4)
                .build();
    }

    private static class LightIngredient extends BasicAuxiliaryIngredient<ListTag> {
        private LightIngredient(final boolean isRequired) {
            super(LazyTagIngredient.of(LIGHTS), isRequired, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            throw new NotImplementedException("LightIngredient.getInput");
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public ListTag accumulator() {
            return new ListTag();
        }

        @Override
        public void consume(final ListTag patternList, final ItemStack ingredient) {
            throw new NotImplementedException("LightIngredient.consume");
        }

        @Override
        public boolean finish(final ListTag pattern, final CompoundTag nbt) {
            throw new NotImplementedException("LightIngredient.finish");
        }

        @Override
        public void addTooltip(final List<Component> tooltip) {
            tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.light"));
        }
    }

    private static class PennantIngredient extends BasicAuxiliaryIngredient<ListTag> {
        private PennantIngredient() {
            super(LazyTagIngredient.of(PENNANTS), true, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            throw new NotImplementedException("PennantIngredient.getInput");
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public ListTag accumulator() {
            return new ListTag();
        }

        @Override
        public void consume(final ListTag patternList, final ItemStack ingredient) {
            throw new NotImplementedException("PennantIngredient.consume");
        }

        @Override
        public boolean finish(final ListTag pattern, final CompoundTag nbt) {
            throw new NotImplementedException("PennantIngredient.finish");
        }

        @Override
        public void addTooltip(final List<Component> tooltip) {
            tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.pennantBunting.pennant"));
        }
    }

    private FLRCraftingRecipes() {}
}
