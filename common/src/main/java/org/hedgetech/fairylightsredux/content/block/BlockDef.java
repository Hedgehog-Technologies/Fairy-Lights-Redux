package org.hedgetech.fairylightsredux.content.block;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.hedgetech.fairylightsredux.content.block.data.BlockData;
import org.hedgetech.fairylightsredux.content.loot.LootDef;
import org.hedgetech.fairylightsredux.content.recipe.RecipeDef;
import org.hedgetech.fairylightsredux.registry.FLRBlockItems;
import org.hedgetech.fairylightsredux.registry.FLRBlocks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record BlockDef(
        String id,
        RecipeCategory category,
        Supplier<BlockBehaviour.Properties> blockProps,
        BlockFactory blockFactory,
        BlockItemFactory itemFactory,
        @Nullable RecipeDef recipe,
        @Nullable LootDef loot,
        @Nullable BlockData data
) implements ItemLike {
    public BlockDef(String id, RecipeCategory category, Supplier<BlockBehaviour.Properties> blockProps, BlockFactory blockFactory, BlockItemFactory itemFactory) {
        this(id, category, blockProps, blockFactory, itemFactory, null, null, null);
    }

    public BlockDef(String id, RecipeCategory category, Supplier<BlockBehaviour.Properties> blockProps, BlockFactory blockFactory, BlockItemFactory itemFactory, @Nullable RecipeDef recipe) {
        this(id, category, blockProps, blockFactory, itemFactory, recipe, null, null);
    }

    public BlockDef(String id, RecipeCategory category, Supplier<BlockBehaviour.Properties> blockProps, BlockFactory blockFactory, BlockItemFactory itemFactory, @Nullable LootDef loot) {
        this(id, category, blockProps, blockFactory, itemFactory, null, loot, null);
    }

    public BlockDef(String id, RecipeCategory category, Supplier<BlockBehaviour.Properties> blockProps, BlockFactory blockFactory, BlockItemFactory itemFactory, @Nullable BlockData data) {
        this(id, category, blockProps, blockFactory, itemFactory, null, null, data);
    }

    public BlockDef(String id, RecipeCategory category, Supplier<BlockBehaviour.Properties> blockProps, BlockFactory blockFactory, BlockItemFactory itemFactory, @Nullable LootDef loot, @Nullable BlockData data) {
        this(id, category, blockProps, blockFactory, itemFactory, null, loot, data);
    }

    public BlockDef(String id, RecipeCategory category, Supplier<BlockBehaviour.Properties> blockProps, BlockFactory blockFactory, BlockItemFactory itemFactory, @Nullable RecipeDef recipe, @Nullable BlockData data) {
        this(id, category, blockProps, blockFactory, itemFactory, recipe, null, data);
    }

    public BlockDef(String id, RecipeCategory category, Supplier<BlockBehaviour.Properties> blockProps, BlockFactory blockFactory, BlockItemFactory itemFactory, @Nullable RecipeDef recipe, @Nullable LootDef loot) {
        this(id, category, blockProps, blockFactory, itemFactory, recipe, loot, null);
    }

    public Block createBlock() {
        return this.blockFactory.create(this.blockProps.get(), this);
    }

    public BlockItem createBlockItem() {
        Block block = FLRBlocks.get(this.id);
        return this.itemFactory.create(block, new Item.Properties(), this);
    }

    public @NotNull Item asItem() {
        return FLRBlockItems.get(this.id);
    }
}
