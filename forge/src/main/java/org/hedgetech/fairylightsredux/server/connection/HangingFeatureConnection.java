package org.hedgetech.fairylightsredux.server.connection;

import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.feature.FeatureType;
import org.hedgetech.fairylightsredux.server.feature.HangingFeature;

import java.util.UUID;

public class HangingFeatureConnection<F extends HangingFeature> extends Connection {
    protected static final FeatureType FEATURE = FeatureType.register("feature");

    protected F[] features = this.createFeatures(0);

    public HangingFeatureConnection(final ConnectionType<? extends HangingFeatureConnection<F>> type, final Level world, final Fastener<?> fastener, final UUID uuid) {
        super(type, world, fastener, uuid);
    }

    public final F[] getFeatures() {
        return this.features;
    }

    @Override
    protected void onCalculateCatenary(final boolean relocated) {
        return this.updateFeatures(relocated);
    }
}
