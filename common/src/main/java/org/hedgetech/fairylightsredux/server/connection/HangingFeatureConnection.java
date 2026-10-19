package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.Fastener.Fastener;
import org.hedgetech.fairylightsredux.server.collision.FeatureCollisionTree;
import org.hedgetech.fairylightsredux.server.collision.ICollidable;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.server.feature.HangingFeature;
import org.hedgetech.fairylightsredux.util.AABBBuilder;
import org.hedgetech.fairylightsredux.util.ICurve;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class HangingFeatureConnection<F extends HangingFeature> extends Connection {
    protected F[] features = this.createFeatures(0);

    public HangingFeatureConnection(final ConnectionType<? extends HangingFeatureConnection<F>> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
        super(type, world, fastener, uuid);
    }

    public final F[] getFeatures() {
        return this.features;
    }

    @Override
    protected void onCalculateCatenary(final boolean relocated) {
        this.updateFeatures(relocated);
    }

    protected void updateFeatures(final boolean relocated) {
        final ICurve catenary = this.getCatenary();
        final float spacing = this.getFeatureSpacing();
        final float totalLength = catenary.getLength();

        if (totalLength > 2.0F * Connection.MAX_LENGTH) {
            this.onBeforeUpdateFeatures();
            this.features = this.createFeatures(0);
            this.onAfterUpdateFeatures();
            return;
        }

        final F[] prev = this.features;
        final List<F> features = new ArrayList<>();
        this.onBeforeUpdateFeatures();

        catenary.visitPoints(spacing, true, (index, x, y, z, yaw, pitch) -> {
            final F feature;

            if (!relocated && prev != null && index < prev.length && this.canReuse(prev[index], index)) {
                feature = prev[index];
                feature.set(new Vec3(x, y, z), yaw, pitch);
            } else {
                feature = this.createFeature(index, new Vec3(x, y, z), yaw, pitch);
            }

            this.updateFeature(feature);
            features.add(feature);
        });

        this.features = features.toArray(this.createFeatures(features.size()));
        this.onAfterUpdateFeatures();
    }

    protected boolean canReuse(final F feature, final int index) {
        return true;
    }

    protected abstract F[] createFeatures(int length);

    protected abstract F createFeature(int index, Vec3 point, float yaw, final float pitch);

    protected abstract float getFeatureSpacing();

    protected void onBeforeUpdateFeatures() {}

    protected void updateFeature(final F feature) {}

    protected void onAfterUpdateFeatures() {}

    @Override
    protected void addCollision(final List<ICollidable> collision, final Vec3 origin) {
        super.addCollision(collision, origin);

        if (this.features.length > 0) {
            collision.add(FeatureCollisionTree.build(FeatureType.FEATURE, this.features, f -> {
                final Matrix4f matrix = new Matrix4f();

                if (f.parallelsCord()) {
                    matrix.rotateY(-f.getYaw());
                    matrix.rotateZ(f.getPitch());
                }

                matrix.translate(0.0F, -f.getDescent(), 0.0F);
                final AABB bb = f.getBounds().inflate(0.01D);
                final AABBBuilder bounds = new AABBBuilder();

                for (int corner = 0; corner < 8; corner++) {
                    final Vector3f vert = matrix.transformPosition(
                            (float) ((corner & 1) == 0 ? bb.minX : bb.maxX),
                            (float) ((corner & 2) == 0 ? bb.minY : bb.maxY),
                            (float) ((corner & 4) == 0 ? bb.minZ : bb.maxZ),
                            new Vector3f()
                    );

                    bounds.include(vert.x(), vert.y(), vert.z());
                }

                return bounds.add(origin.add(f.getPoint())).build();
            }));
        }
    }
}
