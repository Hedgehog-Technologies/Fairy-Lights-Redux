package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.server.feature.light.*;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public final class LightVariant<T extends ILightBehavior> {
    public static final LightVariant<IStandardLightBehavior> FAIRY_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.138D, -0.138D, -0.138D, 0.138D, 0.138D, 0.138D), 0.044D, LightVariant::standardBehavior, true);
    public static final LightVariant<IStandardLightBehavior> PAPER_LANTERN = new LightVariant<>(false, 1.0F, new AABB(-0.250D, -0.906D, -0.250D, 0.250D, 0.091D, 0.250D), 0.000D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> ORB_LANTERN = new LightVariant<>(false, 1.0F, new AABB(-0.262D, -0.512D, -0.262D, 0.262D, 0.091D, 0.262D), 0.044D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> FLOWER_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.483D, -0.227D, -0.483D, 0.436D, 0.185D, 0.436D), 0.069D, LightVariant::standardBehavior, true);
    public static final LightVariant<IStandardLightBehavior> CANDLE_LANTERN_LIGHT = new LightVariant<>(false, 1.5F, new AABB(-0.198D, -0.531D, -0.198D, 0.198D, 0.091D, 0.198D), 0.000D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> OIL_LANTERN_LIGHT = new LightVariant<>(false, 1.5F, new AABB(-0.219D, -0.656D, -0.188D, 0.219D, 0.091D, 0.188D), 0.000D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> JACK_O_LANTERN = new LightVariant<>(true, 1.0F, new AABB(-0.231D, -0.419D, -0.231D, 0.231D, 0.122D, 0.231D), 0.044D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> SKULL_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.200D, -0.404D, -0.200D, 0.200D, 0.122D, 0.200D), 0.044D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> GHOST_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.270D, -0.390D, -0.270D, 0.270D, 0.169D, 0.270D), 0.075D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> SPIDER_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.575D, -0.834D, -0.200D, 0.575D, 0.122D, 0.200D), 0.060D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> WITCH_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.294D, -0.419D, -0.294D, 0.294D, 0.173D, 0.294D), 0.044D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> SNOWFLAKE_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.518D, -1.050D, -0.082D, 0.517D, 0.072D, 0.082D), 0.044D, LightVariant::standardBehavior, false);
    public static final LightVariant<IStandardLightBehavior> HEART_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.280D, -0.408D, -0.106D, 0.274D, 0.063D, 0.106D), 0.062D, LightVariant::standardBehavior, true);
    public static final LightVariant<IStandardLightBehavior> MOON_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.200D, -0.669D, -0.144D, 0.300D, 0.063D, 0.144D), 0.044D, LightVariant::standardBehavior, true);
    public static final LightVariant<IStandardLightBehavior> STAR_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.200D, -0.669D, -0.144D, 0.300D, 0.063D, 0.144D), 0.044D, LightVariant::standardBehavior, true);

    public static final LightVariant<MultiLightBehavior> ICICLE_LIGHTS = new LightVariant<>(false, 0.625F, new AABB(-0.264D, -1.032D, -0.253D, 0.276D, 0.091D, 0.266D), 0.012D, stack -> new MultiLightBehavior(Stream.generate(() -> standardBehavior(stack)).limit(4).toList()), false);

    public static final LightVariant<MeteorLightBehavior> METEOR_LIGHT = new LightVariant<>(false, 1.5F, new AABB(-0.090D, -1.588D, -0.090D, 0.090D, 0.091D, 0.090D), 0.000D, stack -> new MeteorLightBehavior(colorBehavior(stack)), false);

    public static final LightVariant<IBrightnessLightBehavior> OIL_LANTERN = new LightVariant<>(false, 1.5F, new AABB(-0.219D, -0.656D, -0.188D, 0.219D, 0.091D, 0.188D), 0.000D, stack -> new TorchLightBehavior(0.13D), false);
    public static final LightVariant<IBrightnessLightBehavior> CANDLE_LANTERN = new LightVariant<>(false, 1.5F, new AABB(-0.198D, -0.531D, -0.198D, 0.198D, 0.091D, 0.198D), 0.000D, stack -> new TorchLightBehavior(0.2D), false);
    public static final LightVariant<IBrightnessLightBehavior> INCANDESCENT_LIGHT = new LightVariant<>(true, 1.0F, new AABB(-0.166D, -0.291D, -0.166D, 0.166D, 0.062D, 0.166D), 0.103D, stack -> new IncandescentBehavior(), true);

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
