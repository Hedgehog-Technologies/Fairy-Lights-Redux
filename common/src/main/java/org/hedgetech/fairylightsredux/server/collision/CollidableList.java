package org.hedgetech.fairylightsredux.server.collision;

import net.minecraft.world.phys.Vec3;

import java.util.List;

public record CollidableList(List<ICollidable> collision) implements ICollidable {
    @Override
    public Intersection intersect(final Vec3 origin, final Vec3 end) {
        Intersection result = null;
        double distance = Double.MAX_VALUE;

        for (final ICollidable collidable : this.collision) {
            final Intersection r = collidable.intersect(origin, end);

            if (r != null) {
                final double d = r.result().distanceTo(origin);

                if (d < distance) {
                    result = r;
                    distance = d;
                }
            }
        }

        return result;
    }
}
