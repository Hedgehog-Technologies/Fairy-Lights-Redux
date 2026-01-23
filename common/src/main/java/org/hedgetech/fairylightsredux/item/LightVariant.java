package org.hedgetech.fairylightsredux.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.feature.light.LightBehavior;

import java.util.Optional;

public interface LightVariant<T extends LightBehavior> {
    // FIXME - Capabilities rework
//    final class Holder {
//        public static Capability<LightVariant<?>> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
//    }

    boolean parallelsCord();

    float getSpacing();

    AABB getBounds();

    double getFloorOffset();

    T createBehavior(final ItemStack stack);

    boolean isOrientable();

    // FIXME - Capabilities rework
//    static LazyOptional<LightVariant<?>> get(final ICapabilityProvider provider) {
//        return provider.getCapability(Holder.CAPABILITY);
//    }
//
//    static ICapabilityProvider provider(final LightVariant<?> variant) {
//        return Holder.CAPABILITY == null ? new EmptyProvider() : new SimpleProvider<>(Holder.CAPABILITY, variant);
//    }
}
