package org.hedgetech.fairylightsredux.server.collision;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.server.feature.IFeature;

public record Intersection(Vec3 result, AABB hitBox, FeatureType featureType, IFeature feature) {}
