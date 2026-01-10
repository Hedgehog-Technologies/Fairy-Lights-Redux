package org.hedgetech.fairylightsredux.util;

import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;

public final class OreDictUtils {
    public static boolean isDye(final ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof DyeItem) {
                return true;
            }
            return stack.is(Tags.Items.DYES);
        }
        return false;
    }

    private OreDictUtils() {}
}
