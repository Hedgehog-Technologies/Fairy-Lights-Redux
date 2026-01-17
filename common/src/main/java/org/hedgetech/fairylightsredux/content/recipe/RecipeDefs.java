package org.hedgetech.fairylightsredux.content.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Map;

public class RecipeDefs {
    public static RecipeDef simpleItem(RecipeCategory category) {
        return new SingleIngredientRecipe(() -> Items.IRON_INGOT);
    }

    public static RecipeDef hammer(ItemLike head, ItemLike handle) {
        return new ShapedRecipeDef(
                List.of(
                        "XXX",
                        " H ",
                        " H "
                ),
                Map.of(
                        'X', () -> head,
                        'H', () -> handle
                ),
                Map.of(
                        "has_head_ing", InventoryChangeTrigger.TriggerInstance.hasItems(head),
                        "has_handle_ing", InventoryChangeTrigger.TriggerInstance.hasItems(handle)
                )
        );
    }

    public static RecipeDef hangingLights() {
        return new ShapedRecipeDef(
                List.of(
                        "I-I"
                ),
                Map.of(
                        'I', () -> Items.IRON_INGOT,
                        '-', () -> Items.STRING
                )
        );
    }
}
