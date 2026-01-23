package org.hedgetech.fairylightsredux.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.hedgetech.fairylightsredux.registry.FLRLightVariants;
import org.hedgetech.fairylightsredux.registry.datacomponent.LightVariantComponent;

public final class LightVariantUtil {
    public static LightVariant<?> get(ItemStack stack) {
        LightVariantComponent comp = stack.get(LightVariantComponent.TYPE);
        if (comp == null) return null;
        return FLRLightVariants.get(comp.variantId());
    }

    public static void set(ItemStack stack, ResourceLocation id) {
        stack.set(LightVariantComponent.TYPE, new LightVariantComponent(id));
    }
}
