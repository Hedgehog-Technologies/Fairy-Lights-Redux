package org.hedgetech.fairylightsredux.server.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.jetbrains.annotations.NotNull;

public class DyedShapedRecipe extends ShapedRecipe {

    private final ShapedRecipePattern pattern;
    private final ItemStack result;

    public DyedShapedRecipe(final String group, final CraftingBookCategory category, final ShapedRecipePattern pattern, final ItemStack result) {
        super(group, category, pattern, result);
        this.pattern = pattern;
        this.result = result;
    }

    @Override
    public @NotNull ItemStack assemble(final @NotNull CraftingInput input, final HolderLookup.@NotNull Provider registries) {
        final ItemStack result = super.assemble(input, registries);

        for (int i = 0; i < input.size(); i++) {
            if (DyeableItem.getDyeColor(input.getItem(i)) instanceof DyeColor dye) {
                return DyeableItem.setColor(result, dye);
            }
        }

        return result;
    }

    @Override
    public @NotNull RecipeSerializer<DyedShapedRecipe> getSerializer() {
        return FLRCraftingRecipes.DYED_SHAPED;
    }

    public static final class Serializer implements RecipeSerializer<DyedShapedRecipe> {
        private static final MapCodec<DyedShapedRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.STRING.optionalFieldOf("group", "").forGetter(ShapedRecipe::group),
                CraftingBookCategory.CODEC.fieldOf("category").orElse(CraftingBookCategory.MISC).forGetter(ShapedRecipe::category),
                ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(instance, DyedShapedRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, DyedShapedRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, ShapedRecipe::group,
                CraftingBookCategory.STREAM_CODEC, ShapedRecipe::category,
                ShapedRecipePattern.STREAM_CODEC, recipe -> recipe.pattern,
                ItemStack.STREAM_CODEC, recipe -> recipe.result,
                DyedShapedRecipe::new
        );

        @Override
        public @NotNull MapCodec<DyedShapedRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DyedShapedRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
