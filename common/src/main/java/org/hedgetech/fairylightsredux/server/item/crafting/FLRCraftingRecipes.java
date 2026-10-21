package org.hedgetech.fairylightsredux.server.item.crafting;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.hedgetech.fairylightsredux.Constants;

import java.util.function.BiConsumer;

public final class FLRCraftingRecipes {
    public static final TagKey<Item> LIGHTS = TagKey.create(Registries.ITEM, Constants.id("lights"));
    public static final TagKey<Item> TWINKLING_LIGHTS = TagKey.create(Registries.ITEM, Constants.id("twinkling_lights"));
    public static final TagKey<Item> DYEABLE = TagKey.create(Registries.ITEM, Constants.id("dyeable"));
    public static final TagKey<Item> DYEABLE_LIGHTS = TagKey.create(Registries.ITEM, Constants.id("dyeable_lights"));
    public static final TagKey<Item> PENNANTS = TagKey.create(Registries.ITEM, Constants.id("pennants"));

    public static final TagKey<Item> IRON_INGOTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "ingots/iron"));
    public static final TagKey<Item> STRINGS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "strings"));
    public static final TagKey<Item> DYES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dyes"));
    public static final TagKey<Item> GLOWSTONE_DUSTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "dusts/glowstone"));
    public static final TagKey<Item> GOLD_NUGGETS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "nuggets/gold"));
    public static final TagKey<Item> BONES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "bones"));
    public static final TagKey<Item> FEATHERS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "feathers"));
    public static final TagKey<Item> COLORLESS_GLASS_PANES = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "glass_panes/colorless"));

    public static RecipeSerializer<HangingLightsRecipe> HANGING_LIGHTS;
    public static RecipeSerializer<HangingLightsAugmentationRecipe> HANGING_LIGHTS_AUGMENTATION;
    public static RecipeSerializer<PennantBuntingRecipe> PENNANT_BUNTING;
    public static RecipeSerializer<PennantBuntingAugmentationRecipe> PENNANT_BUNTING_AUGMENTATION;
    public static RecipeSerializer<LightTwinkleRecipe> LIGHT_TWINKLE;
    public static RecipeSerializer<ColorChangingLightRecipe> COLOR_CHANGING_LIGHT;
    public static RecipeSerializer<EditColorRecipe> EDIT_COLOR;
    public static RecipeSerializer<CopyColorRecipe> COPY_COLOR;
    public static RecipeSerializer<DyedShapedRecipe> DYED_SHAPED;

    public static void register(final BiConsumer<ResourceLocation, RecipeSerializer<?>> registry) {
        HANGING_LIGHTS = register(registry, "crafting_special_hanging_lights", new CustomRecipe.Serializer<>(HangingLightsRecipe::new));
        HANGING_LIGHTS_AUGMENTATION = register(registry, "crafting_special_hanging_lights_augmentation", new CustomRecipe.Serializer<>(HangingLightsAugmentationRecipe::new));
        PENNANT_BUNTING = register(registry, "crafting_special_pennant_bunting", new CustomRecipe.Serializer<>(PennantBuntingRecipe::new));
        PENNANT_BUNTING_AUGMENTATION = register(registry, "crafting_special_pennant_bunting_augmentation", new CustomRecipe.Serializer<>(PennantBuntingAugmentationRecipe::new));
        LIGHT_TWINKLE = register(registry, "crafting_special_light_twinkle", new CustomRecipe.Serializer<>(LightTwinkleRecipe::new));
        COLOR_CHANGING_LIGHT = register(registry, "crafting_special_color_changing_light", new CustomRecipe.Serializer<>(ColorChangingLightRecipe::new));
        EDIT_COLOR = register(registry, "crafting_special_edit_color", new CustomRecipe.Serializer<>(EditColorRecipe::new));
        COPY_COLOR = register(registry, "crafting_special_copy_color", new CustomRecipe.Serializer<>(CopyColorRecipe::new));
        DYED_SHAPED = register(registry, "crafting_dyed_shaped", new DyedShapedRecipe.Serializer());
    }

    private static <T extends RecipeSerializer<?>> T register(final BiConsumer<ResourceLocation, RecipeSerializer<?>> registry, final String name, final T serializer) {
        registry.accept(Constants.id(name), serializer);
        return serializer;
    }

    private FLRCraftingRecipes() {}
}
