package org.hedgetech.fairylightsredux.server.collision;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.server.feature.IFeature;

import java.util.function.Function;
import java.util.function.IntFunction;

public final class FeatureCollisionTree implements ICollidable {
    private final FeatureType type;
    private final AABB[] tree;
    private final IFeature[] nodeToFeature;

    private FeatureCollisionTree(final FeatureType type, final AABB[] tree, final IFeature[] nodeToFeature) {
        this.type = type;
        this.tree = tree;
        this.nodeToFeature = nodeToFeature;
    }

    @Override
    public Intersection intersect(final Vec3 origin, final Vec3 end) {
        return this.intersect(origin, end, 0);
    }

    private Intersection intersect(final Vec3 origin, final Vec3 end, final int node) {
        final Vec3 result;

        if (this.tree[node].contains(origin)) {
            result = origin;
        } else {
            result = this.tree[node].clip(origin, end).orElse(null);
        }

        if (result == null) return null;

        final int nL = node * 2 + 1;
        if (nL >= this.tree.length || this.tree[nL] == null) {
            return new Intersection(result, this.tree[node], this.type, this.nodeToFeature[node]);
        }

        final Intersection intersection = this.intersect(origin, end, nL);
        if (intersection != null) return intersection;

        return this.intersect(origin, end, node * 2 + 2);
    }

    public static <T extends IFeature> FeatureCollisionTree build(final FeatureType type, final T[] features, final Function<T, AABB> mapper) {
        return build(type, i -> features[i], i -> mapper.apply(features[i]), 0, features.length - 1);
    }

    public static <T extends IFeature> FeatureCollisionTree build(final FeatureType type, final IntFunction<T> features, final IntFunction<AABB> mapper, final int start, final int end) {
        final AABB[] tree = new AABB[end == start ? 1 : (1 << (Mth.log2(end - start) + 2)) - 1];
        final IFeature[] treeFeatures = new IFeature[tree.length];
        tree[0] = build(features, mapper, tree, treeFeatures, start, end, 0);
        return new FeatureCollisionTree(type, tree, treeFeatures);
    }

    private static <T extends IFeature> AABB build(final IntFunction<T> features, final IntFunction<AABB> mapper, final AABB[] tree, final IFeature[] treeFeatures, final int min, final int max, final int node) {
        if (min == max) {
            treeFeatures[node] = features.apply(min);
            return mapper.apply(min);
        }

        final int mid = min + (max - min) / 2;
        final int nL = node * 2 + 1;
        final int nR = node * 2 + 2;

        return (tree[nL] = build(features, mapper, tree, treeFeatures, min, mid, nL)).minmax(tree[nR] = build(features, mapper, tree, treeFeatures, mid + 1, max, nR));
    }
}
