package org.hedgetech.fairylightsredux.util;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class AABBBuilder {
    private double minX, minY, minZ, maxX, maxY, maxZ;

    public AABBBuilder add(final Vec3 point) {
        return this.add(point.x, point.y, point.z);
    }

    public AABBBuilder add(final double x, final double y, final double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public AABBBuilder include(final double x, final double y, final double z) {
        if (x < this.minX) this.minX = x;
        if (y < this.minY) this.minY = y;
        if (z < this.minZ) this.minZ = z;
        if (x > this.maxX) this.maxX = x;
        if (y > this.maxY) this.maxY = y;
        if (z > this.maxZ) this.maxZ = z;
        return this;
    }

    public AABB build() {
        return new AABB(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
}
