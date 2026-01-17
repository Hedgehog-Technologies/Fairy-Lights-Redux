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
    public Item createItem() {
        return factory.create(this.props.get(), this);
    }

    public @NotNull Item asItem() {
        return FLRItems.get(id);
    }
}
