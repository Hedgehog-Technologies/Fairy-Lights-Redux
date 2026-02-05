package org.hedgetech.fairylightsredux.content.recipe;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.hedgetech.fairylightsredux.Constants;

import java.util.List;
import java.util.Map;

public class RecipeDefs {
    public static final TagKey<Item> LIGHTS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "lights"));

//    public static RecipeDef simpleItem(RecipeCategory category) {
//        return new SingleIngredientRecipe(() -> Items.IRON_INGOT);
//    }
//
//    public static RecipeDef hammer(ItemLike head, ItemLike handle) {
//        return new ShapedRecipeDef(
//                List.of(
//                        "XXX",
//                        " H ",
//                        " H "
//                ),
//                Map.of(
//                        'X', () -> head,
//                        'H', () -> handle
//                ),
//                Map.of(
//                        "has_head_ing", InventoryChangeTrigger.TriggerInstance.hasItems(head),
//                        "has_handle_ing", InventoryChangeTrigger.TriggerInstance.hasItems(handle)
//                )
//        );
//    }

    public static RecipeDef hangingLights() {
        return new ShapedRecipeDef(
                List.of(
                        "I-I"
                ),
                Map.of(
                        'I', () -> Items.IRON_INGOT,
                        '-', () -> Items.STRING
                ),
                Map.of(
                        "has_iron", InventoryChangeTrigger.TriggerInstance.hasItems(Items.IRON_INGOT)
                )
        );
    }

}
