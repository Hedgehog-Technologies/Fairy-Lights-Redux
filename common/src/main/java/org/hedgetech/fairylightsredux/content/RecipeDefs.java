package org.hedgetech.fairylightsredux.content;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.hedgetech.fairylightsredux.content.def.RecipeDef;

import java.util.Map;

public class RecipeDefs {
    public static RecipeDef simpleItem(String name) {
        return new SingleIngredientRecipe(() -> Items.IRON_INGOT);
    }

    public static RecipeDef hammer(ItemLike head, ItemLike handle) {
        return new ShapedRecipeDef(
                "XXX",
                " H ",
                " H ",
                Map.of(
                        'X', head,
                        'H', handle
                )
        );
    }
}
