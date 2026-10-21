package org.hedgetech.fairylightsredux.server.feature.light;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MultiLightBehavior implements ILightBehavior {
    private final List<IStandardLightBehavior> lights;
    private final IStandardLightBehavior fallback;

    public MultiLightBehavior(final List<IStandardLightBehavior> lights) {
        this.lights = lights;
        this.fallback = new CompositeBehavior(new DefaultBrightnessBehavior(), new FixedColorBehavior(1.0F, 1.0F, 1.0F));
    }

    public IStandardLightBehavior get(final int index) {
        if (index >= 0 && index < this.lights.size()) {
            return this.lights.get(index);
        }

        return this.fallback;
    }

    @Override
    public void power(final boolean powered, final boolean now, final Light<?> light) {
        for (final IStandardLightBehavior behavior : this.lights) {
            behavior.power(powered, now, light);
        }

        this.fallback.power(powered, now, light);
    }

    @Override
    public void tick(final Level world, final Vec3 origin, final Light<?> light) {
        for (final IStandardLightBehavior behavior : this.lights) {
            behavior.tick(world, origin, light);
        }

        this.fallback.tick(world, origin, light);
    }
}
