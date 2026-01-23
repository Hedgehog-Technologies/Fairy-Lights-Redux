package org.hedgetech.fairylightsredux.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.feature.light.LightBehavior;

import java.util.Optional;

public interface LightVariant<T extends LightBehavior> {
    boolean parallelsCord();

    float getSpacing();

    AABB getBounds();

    double getFloorOffset();

    T createBehavior(final ItemStack stack);

    boolean isOrientable();
}
