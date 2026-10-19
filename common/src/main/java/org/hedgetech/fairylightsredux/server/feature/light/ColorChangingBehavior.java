package org.hedgetech.fairylightsredux.server.feature.light;

import net.minecraft.Util;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.component.FLRDataComponents;
import org.hedgetech.fairylightsredux.util.FLRMth;

import java.util.List;

public class ColorChangingBehavior implements IColorLightBehavior {
    private final List<Integer> colors;

    private boolean powered;

    public ColorChangingBehavior(final List<Integer> colors) {
        this.colors = colors;
    }

    @Override
    public float getRed(final float delta) {
        return ARGB.redFloat(this.getColor());
    }

    @Override
    public float getGreen(final float delta) {
        return ARGB.greenFloat(this.getColor());
    }

    @Override
    public float getBlue(final float delta) {
        return ARGB.blueFloat(this.getColor());
    }

    private int getColor() {
        return this.powered
                ? animate(this.colors)
                : this.colors.getFirst();
    }

    @Override
    public void power(final boolean powered, final boolean now, final Light<?> light) {
        this.powered = powered;
    }

    @Override
    public void tick(final Level world, final Vec3 origin, final Light<?> light) {}

    public static IColorLightBehavior create(final ItemStack stack) {
        return new ColorChangingBehavior(stack.getOrDefault(FLRDataComponents.COLORS, List.of()));
    }

    public static int animate(final List<Integer> colors) {
        final float p = FLRMth.mod(Util.getMillis() * (20.0F / 1000.0F) * (colors.size() / 960.0F), colors.size());
        final int i = (int) p;
        return ARGB.lerp(p - i, colors.get(i % colors.size()), colors.get((i + 1) % colors.size()));
    }

    public static boolean exists(final ItemStack stack) {
        return !stack.getOrDefault(FLRDataComponents.COLORS, List.of()).isEmpty();
    }
}
