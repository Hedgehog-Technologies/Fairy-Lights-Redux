package org.hedgetech.fairylightsredux.content.def;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeBookCategory;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record ItemDef(
        String name,
        Supplier<Item.Properties> props,
        RecipeBookCategory category,
        @Nullable RecipeDef recipe,
        @Nullable LootDef loot
) {
    public Item asItem() {
        return new Item(this.props.get());
    }
}
