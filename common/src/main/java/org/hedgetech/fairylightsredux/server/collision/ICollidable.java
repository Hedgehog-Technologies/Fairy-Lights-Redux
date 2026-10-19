package org.hedgetech.fairylightsredux.server.collision;

import net.minecraft.world.phys.Vec3;

public interface ICollidable {
    Intersection intersect(final Vec3 origin, final Vec3 end);

    static ICollidable empty() {
        return (o, e) -> null;
    }
}
