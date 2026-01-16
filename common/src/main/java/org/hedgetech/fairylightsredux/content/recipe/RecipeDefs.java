package org.hedgetech.fairylightsredux.content.recipe;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.hedgetech.fairylightsredux.content.def.RecipeDef;

import java.util.List;
import java.util.Map;

public class RecipeDefs {
    public static RecipeDef simpleItem(RecipeCategory category) {
        return new SingleIngredientRecipe(category, () -> Items.IRON_INGOT);
    }

    public static RecipeDef hammer(ItemLike head, ItemLike handle) {
        return new ShapedRecipeDef(
                RecipeCategory.TOOLS,
                List.of(
                        "XXX",
                        " H ",
                        " H "
                ),
                Map.of(
                        'X', head,
                        'H', handle
                )
        );
    }
}
