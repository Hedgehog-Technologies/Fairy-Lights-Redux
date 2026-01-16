package org.hedgetech.fairylightsredux.content;

import net.minecraft.world.item.Item;
import org.hedgetech.fairylightsredux.content.def.ItemDef;
import org.hedgetech.fairylightsredux.content.loot.LootDefs;
import org.hedgetech.fairylightsredux.content.recipe.RecipeDefs;

import java.util.List;

public class ItemDefs {
    public static final List<ItemDef> ITEMS = List.of(

    );

    // EXAMPLE SIMPLE ITEM DEF
    private static ItemDef simple(String name) {
        return new ItemDef(
                name,
                Item.Properties::new,
                RecipeDefs.simpleItem(name),
                LootDefs.self(name)
        );
    }
}
