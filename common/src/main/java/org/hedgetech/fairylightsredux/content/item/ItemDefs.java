package org.hedgetech.fairylightsredux.content.item;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.hedgetech.fairylightsredux.content.item.data.HammerData;
import org.hedgetech.fairylightsredux.content.item.data.ItemData;
import org.hedgetech.fairylightsredux.content.loot.LootDefs;
import org.hedgetech.fairylightsredux.content.recipe.RecipeDefs;

import java.util.List;

public class ItemDefs {
    public static final List<ItemDef> ITEMS = List.of(
//            new ItemDef(
//                    "steel_hammer",
//                    RecipeCategory.TOOLS,
//                    () -> new Item.Properties().durability(250),
//                    (props, def) -> {
//                        HammerData d = ItemData.requireData(def, HammerData.class);
//                        return new HammerItem(props, d.tier());
//                    },
//                    RecipeDefs.hammer(Items.IRON_INGOT, Items.STICK),
//                    LootDefs.self(),
//                    new HammerData(3)
//            )

            new ItemDef(
                    "hanging_lights",
                    RecipeCategory.DECORATIONS,
                    ItemDefs::defaultProperties,
                    (props, def) -> new HangingLightsConnectionItem(props),
                    null,
                    null,
                    null
            )
    );

    private static Item.Properties defaultProperties() {
        return new Item.Properties();
    }
}
