package org.hedgetech.fairylightsredux.server.feature.light;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.feature.HangingFeature;
import org.hedgetech.fairylightsredux.server.item.LightVariant;

public final class Light<T extends LightBehavior> extends HangingFeature {
    private static final int SWAY_RATE = 10;
    private static final int SWAY_PEAK_COUNT = 5;
    private static final int SWAY_CYCLE = SWAY_RATE * SWAY_PEAK_COUNT;

    private final ItemStack item;
    private final LightVariant<T> variant;
    private final T behavior;

    private int sway;
    private boolean swaying;
    private boolean swayDirection;
    private int tick;
    private int lastJingledTick = -1;
    private boolean powered;

    public Light(final int index, final Vec3 point, final float yaw, final float pitch, final ItemStack item, final LightVariant<T> variant, final float descent) {
        super(index, point, yaw, pitch, 0.0F, descent);
        this.item = item;
        this.variant = variant;
        this.behavior = variant.createBehavior(item);
    }
}
