package org.hedgetech.fairylightsredux.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.feature.light.BrightnessLightBehavior;
import org.hedgetech.fairylightsredux.feature.light.ColorChangingBehavior;
import org.hedgetech.fairylightsredux.feature.light.ColorLightBehavior;
import org.hedgetech.fairylightsredux.feature.light.CompositeBehavior;
import org.hedgetech.fairylightsredux.feature.light.DefaultBrightnessBehavior;
import org.hedgetech.fairylightsredux.feature.light.FixedColorBehavior;
import org.hedgetech.fairylightsredux.feature.light.LightBehavior;
import org.hedgetech.fairylightsredux.feature.light.StandardLightBehavior;
import org.hedgetech.fairylightsredux.feature.light.TwinkleBehavior;

import java.util.function.Function;

public class SimpleLightVariant<T extends LightBehavior> implements LightVariant<T> {
    public static final Codec<AABB> AABB_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("minX").forGetter(aabb -> aabb.minX),
            Codec.DOUBLE.fieldOf("minY").forGetter(aabb -> aabb.minY),
            Codec.DOUBLE.fieldOf("minZ").forGetter(aabb -> aabb.minZ),
            Codec.DOUBLE.fieldOf("maxX").forGetter(aabb -> aabb.maxX),
            Codec.DOUBLE.fieldOf("maxY").forGetter(aabb -> aabb.maxY),
            Codec.DOUBLE.fieldOf("maxZ").forGetter(aabb -> aabb.maxZ)
    ).apply(instance, AABB::new));

    public static final MapCodec<SimpleLightVariant<?>> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.BOOL.fieldOf("parallelsCord").forGetter(SimpleLightVariant::parallelsCord),
            Codec.FLOAT.fieldOf("spacing").forGetter(SimpleLightVariant::getSpacing),
            AABB_CODEC.fieldOf("bounds").forGetter(SimpleLightVariant::getBounds),
            Codec.DOUBLE.fieldOf("floorOffset").forGetter(SimpleLightVariant::getFloorOffset),
            Codec.BOOL.fieldOf("orientable").forGetter(SimpleLightVariant::isOrientable)
    ).apply(instance, (parallelsCord, spacing, bounds, floorOffset, orientable) ->
            new SimpleLightVariant<>(parallelsCord, spacing, bounds, floorOffset, SimpleLightVariant::standardBehavior, orientable)
    ));

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "simple_light_varient");
    public static final LightVariantType<SimpleLightVariant<?>> TYPE = LightVariantTypes.register(ID, CODEC);

    public static final LightVariant<StandardLightBehavior> FAIRY_LIGHT = new SimpleLightVariant<>(true, 1.0F, new AABB(-0.138D, -0.138D, -0.138D, 0.138D, 0.138D, 0.138D), 0.044D, SimpleLightVariant::standardBehavior, true);

    private final boolean parallelsCord;
    private final float spacing;
    private final AABB bounds;
    private final double floorOffset;
    private final Function<ItemStack, T> behaviorFactory;
    private final boolean orientable;

    SimpleLightVariant(final boolean parallelsCord, final float spacing, final AABB bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory) {
        this(parallelsCord, spacing, bounds, floorOffset, behaviorFactory, false);
    }

    SimpleLightVariant(final boolean parallelsCord, final float spacing, final AABB bounds, final double floorOffset, final Function<ItemStack, T> behaviorFactory, final boolean orientable) {
        this.parallelsCord = parallelsCord;
        this.spacing = spacing;
        this.bounds = bounds;
        this.floorOffset = floorOffset;
        this.behaviorFactory = behaviorFactory;
        this.orientable = orientable;
    }

    @Override
    public static MapCodec<SimpleLightVariant<?>> codec() {
        return CODEC;
    }

    @Override
    public LightVariantType<T> type() {
        return null;
    }

    @Override
    public boolean parallelsCord() {
        return this.parallelsCord;
    }

    @Override
    public float getSpacing() {
        return this.spacing;
    }

    @Override
    public AABB getBounds() {
        return this.bounds;
    }

    @Override
    public double getFloorOffset() {
        return this.floorOffset;
    }

    @Override
    public T createBehavior(final ItemStack stack) {
        return this.behaviorFactory.apply(stack);
    }

    @Override
    public boolean isOrientable() {
        return this.orientable;
    }

    private static StandardLightBehavior standardBehavior(final ItemStack stack) {
        final BrightnessLightBehavior brightness;
        if (TwinkleBehavior.exists(stack)) {
            brightness = new TwinkleBehavior(0.05F, 40);
        } else {
            brightness = new DefaultBrightnessBehavior();
        }
        final ColorLightBehavior color;
        if (ColorChangingBehavior.exists(stack)) {
            color = ColorChangingBehavior.create(stack);
        } else {
            color = FixedColorBehavior.create(stack);
        }
        return new CompositeBehavior(brightness, color);
    }
}
