package org.hedgetech.fairylightsredux.server.feature;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Pennant extends HangingFeature {
    private final ItemStack item;

    public Pennant(final int index, final Vec3 point, final float yaw, final float pitch, final ItemStack item) {
        super(index, point, yaw, pitch, 0.0F, 0.0F);
        this.item = item;
    }

    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public AABB getBounds() {
        return new AABB(-0.22D, -0.5D, -0.02D, 0.22D, 0.0D, 0.02D);
    }

    @Override
    public boolean parallelsCord() {
        return true;
    }
}
