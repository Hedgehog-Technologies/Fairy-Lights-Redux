package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.server.feature.light.*;

import java.util.Optional;
import java.util.function.Function;

public final class LightVariant<T extends ILightBehavior> {
    public static final LightVariant<IStandardLightBehavior> FAIRY_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.138D, -0.138D, -0.138D, 0.138D, 0.138D, 0.138D), 0.044D, LightVariant::standardBehavior, true);

    private final boolean parallelsCord;
    private final float spacing;
    private final AABB bounds;
    private final double floorOffset;
    private final Function<ItemStack, T> behaviorFactory;
    private final boolean orientable;

    private LightVariant(final boolean parallelsCord, final float spacing, final AABB bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory, final boolean orientable) {
        this.parallelsCord = parallelsCord;
        this.spacing = spacing;
        this.bounds = bounds;
        this.floorOffset = floorOffset;
        this.behaviorFactory = behaviorFactory;
        this.orientable = orientable;
    }

    public boolean parallelsCord() {
        return this.parallelsCord;
    }

    public float getSpacing() {
        return this.spacing;
    }

    public AABB getBounds() {
        return this.bounds;
    }

    public double getFloorOffset() {
        return this.floorOffset;
    }

    public T createBehavior(final ItemStack stack) {
        return this.behaviorFactory.apply(stack);
    }

    public boolean isOrientable() {
        return this.orientable;
    }

    public static Optional<LightVariant<?>> get(final ItemStack stack) {
        return stack.getItem() instanceof LightItem item
                ? Optional.of(item.getBlock().getVariant())
                : Optional.empty();
    }

    private static IStandardLightBehavior standardBehavior(final ItemStack stack) {
        final IBrightnessLightBehavior brightness = TwinkleBehavior.exists(stack)
                ? new TwinkleBehavior(0.05F, 40)
                : new DefaultBrightnessBehavior();
        return new CompositeBehavior(brightness, colorBehavior(stack));
    }

    private static IColorLightBehavior colorBehavior(final ItemStack stack) {
        return ColorChangingBehavior.exists(stack)
                ? ColorChangingBehavior.create(stack)
                : FixedColorBehavior.create(stack);
    }
}
