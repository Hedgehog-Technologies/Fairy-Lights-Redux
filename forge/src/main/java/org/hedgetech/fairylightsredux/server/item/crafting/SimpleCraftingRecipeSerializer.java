package org.hedgetech.fairylightsredux.server.item.crafting;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jspecify.annotations.NonNull;

public class SimpleCraftingRecipeSerializer<T extends CraftingRecipe> implements RecipeSerializer<T> {
    private final MapCodec<T> codec;
    private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public SimpleCraftingRecipeSerializer(Factory<T> factory) {
        this.codec = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    CraftingBookCategory.CODEC
                            .optionalFieldOf("category", CraftingBookCategory.MISC)
                            .forGetter(CraftingRecipe::category)
            ).apply(instance, factory::create)
        );

        this.streamCodec = StreamCodec.composite(
                CraftingBookCategory.STREAM_CODEC,
                CraftingRecipe::category,
                factory::create
        );
    }

    @Override
    public @NonNull MapCodec<T> codec() {
        return codec;
    }

    @Deprecated
    @Override
    public @NonNull StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return this.streamCodec;
    }

    @FunctionalInterface
    public interface Factory<T extends CraftingRecipe> {
        T create(CraftingBookCategory category);
    }
}
