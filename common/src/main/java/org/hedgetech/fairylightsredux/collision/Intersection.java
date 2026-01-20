package org.hedgetech.fairylightsredux.collision;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.feature.Feature;
import org.hedgetech.fairylightsredux.feature.FeatureType;

public record Intersection(Vec3 result, AABB hitBox, FeatureType featureType, Feature feature) {
}
