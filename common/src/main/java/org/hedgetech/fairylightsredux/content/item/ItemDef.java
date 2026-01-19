package org.hedgetech.fairylightsredux.content.item;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.hedgetech.fairylightsredux.content.item.data.ItemData;
import org.hedgetech.fairylightsredux.registry.FLRItems;
import org.hedgetech.fairylightsredux.content.loot.LootDef;
import org.hedgetech.fairylightsredux.content.recipe.RecipeDef;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record ItemDef(
        String id,
        RecipeCategory category,
        Supplier<Item.Properties> props,
        ItemFactory factory,
        @Nullable RecipeDef recipe,
        @Nullable LootDef loot,
        @Nullable ItemData data
) implements ItemLike {
    public ItemDef(String id, RecipeCategory category, Supplier<Item.Properties> props, ItemFactory factory) {
        this(id, category, props, factory, null, null, null);
    }

    public ItemDef(String id, RecipeCategory category, Supplier<Item.Properties> props, ItemFactory factory, @Nullable RecipeDef recipe) {
        this(id, category, props, factory, recipe, null, null);
    }

    public ItemDef(String id, RecipeCategory category, Supplier<Item.Properties> props, ItemFactory factory, @Nullable LootDef loot) {
        this(id, category, props, factory, null, loot, null);
    }

    public ItemDef(String id, RecipeCategory category, Supplier<Item.Properties> props, ItemFactory factory, @Nullable ItemData data) {
        this(id, category, props, factory, null, null, data);
    }

    public ItemDef(String id, RecipeCategory category, Supplier<Item.Properties> props, ItemFactory factory, @Nullable RecipeDef recipe, @Nullable LootDef loot) {
        this(id, category, props, factory, recipe, loot, null);
    }

    public ItemDef(String id, RecipeCategory category, Supplier<Item.Properties> props, ItemFactory factory, @Nullable RecipeDef recipe, @Nullable ItemData data) {
        this(id, category, props, factory, recipe, null, data);
    }

    public ItemDef(String id, RecipeCategory category, Supplier<Item.Properties> props, ItemFactory factory, @Nullable LootDef loot, @Nullable ItemData data) {
        this(id, category, props, factory, null, loot, data);
    }

    public Item createItem() {
        return factory.create(this.props.get(), this);
    }

    public @NotNull Item asItem() {
        return FLRItems.get(id);
    }
}
