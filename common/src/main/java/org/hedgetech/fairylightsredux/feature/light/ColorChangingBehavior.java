package org.hedgetech.fairylightsredux.feature.light;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.registry.datacomponent.PrimitiveComponents;
import org.hedgetech.fairylightsredux.util.FLRMth;

public class ColorChangingBehavior implements ColorLightBehavior {
    public static final Codec<ColorChangingBehavior> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(
                    PrimitiveComponents.FLOAT_ARRAY_CODEC.fieldOf("red").forGetter(ColorChangingBehavior::getRedRaw),
                    PrimitiveComponents.FLOAT_ARRAY_CODEC.fieldOf("green").forGetter(ColorChangingBehavior::getGreenRaw),
                    PrimitiveComponents.FLOAT_ARRAY_CODEC.fieldOf("blue").forGetter(ColorChangingBehavior::getBlueRaw)
            ).apply(inst, ColorChangingBehavior::new));

    public static final DataComponentType<ColorChangingBehavior> TYPE = DataComponentType.<ColorChangingBehavior>builder()
            .persistent(CODEC)
            .build();

    private final float[] red;
    private final float[] green;
    private final float[] blue;
    private final float rate;
    private boolean powered;

    public ColorChangingBehavior(final float[] red, final float[] green, final float[] blue) {
        this(red, green, blue, red.length / 960.F);
    }

    public ColorChangingBehavior(final float[] red, final float[] green, final float[] blue, final float rate) {
        assert red.length == green.length && red.length == blue.length;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.rate = rate;
    }

    @Override
    public float getRed(final float delta) {
        return this.get(this.red, delta);
    }

    public float[] getRedRaw() {
        return this.red;
    }

    @Override
    public float getGreen(final float delta) {
        return this.get(this.green, delta);
    }

    public float[] getGreenRaw() {
        return this.green;
    }

    @Override
    public float getBlue(final float delta) {
        return this.get(this.blue, delta);
    }

    public float[] getBlueRaw() {
        return this.blue;
    }

    private float get(final float[] values, final float delta) {
        // TECH_DEBT - Why the hardcoded math?
        final float p = this.powered ? FLRMth.mod(Util.getMillis() * (20.0F / 1000.0F) * this.rate, values.length) : 0.0F;
        final int i = (int) p;
        return Mth.lerp(p - i, values[i % values.length], values[(i + 1) % values.length]);
    }

    @Override
    public void power(final boolean powered, final boolean now, final Light<?> light) {
        this.powered = powered;
    }

    @Override
    public void tick(final Level world, final Vec3 origin, final Light<?> light) {}

    public static ColorLightBehavior create(final ItemStack stack) {
        ColorChangingBehavior comp = stack.get(ColorChangingBehavior.TYPE);
        if (comp == null) {
            return new FixedColorBehavior(1.0F, 1.0F, 1.0F);
        }
        return comp;
    }


    public static int animate(final ItemStack stack) {
        ColorChangingBehavior comp = stack.get(ColorChangingBehavior.TYPE);
        if (comp == null) {
            return 0xFFFFFF;
        }
        if (comp.red.length == 0 || comp.green.length == 0 || comp.blue.length == 0) {
            return 0xFFFFFF;
        }
        if (comp.red.length == 1 && comp.green.length == 1 && comp.blue.length == 1) {
            return ((int) (comp.red[0] * 255.0F) << 16) |
                    ((int) (comp.green[0] * 255.0F) << 8) |
                    ((int) (comp.blue[0] * 255.0F));
        }
        // TECH_DEBT - Why the hardcoded math?
        final float p = FLRMth.mod(Util.getMillis() * (20.0F / 1000.0F) * comp.rate, comp.red.length);
        final int i = (int) p;
        final int idx1 = i % comp.red.length;
        final int idx2 = (i + 1) % comp.red.length;
        return (int) (Mth.lerp(p - i, comp.red[idx1], comp.red[idx2]) * 255.0F) << 16 |
                (int) (Mth.lerp(p - i, comp.green[idx1], comp.green[idx2]) * 255.0F) << 8 |
                (int) (Mth.lerp(p - i, comp.blue[idx1], comp.blue[idx2]) * 255.0F);
    }

    public static boolean exists(final ItemStack stack) {
        return stack.has(ColorChangingBehavior.TYPE);
    }
}
