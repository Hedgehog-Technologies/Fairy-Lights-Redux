package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.hedgetech.fairylightsredux.server.feature.light.LightBehavior;
import org.hedgetech.fairylightsredux.util.EmptyProvider;
import org.hedgetech.fairylightsredux.util.SimpleProvider;

public interface LightVariant<T extends LightBehavior> {
    final class Holder {
        public static Capability<LightVariant<?>> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    }

    boolean parallelsCord();

    float getSpacing();

    AABB getBounds();

    double getFloorOffset();

    T createBehavior(final ItemStack stack);

    boolean isOrientable();

    static LazyOptional<LightVariant<?>> get(final ICapabilityProvider provider) {
        return provider.getCapability(Holder.CAPABILITY);
    }

    static ICapabilityProvider provider(final LightVariant<?> variant) {
        return Holder.CAPABILITY == null ? new EmptyProvider() : new SimpleProvider<>(Holder.CAPABILITY, variant);
    }
}
