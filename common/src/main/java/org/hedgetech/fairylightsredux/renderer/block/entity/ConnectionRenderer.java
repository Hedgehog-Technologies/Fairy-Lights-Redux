package org.hedgetech.fairylightsredux.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import org.hedgetech.fairylightsredux.connection.Connection;
import org.hedgetech.fairylightsredux.util.Curve;

import java.util.function.Function;

public abstract class ConnectionRenderer<C extends Connection> {
    private final WireModel model;
    private final float wireInflate;

    protected ConnectionRenderer(final Function<ModelLayerLocation, ModelPart> baker, final ModelLayerLocation, wireModelLocation) {
        this(baker, wireModelLocation, 0.0F);
    }

    protected ConnectionRenderer(final Function<ModelLayerLocation, ModelPart> baker, final ModelLayerLocation wireModelLocation, final float wireInflate) {
        this.model = new WireModel(baker.apply(wireModelLocation));
        this.wireInflate = wireInflate;
    }

    public void render(final C conn, final float delta, final PoseStack matrix, final MultiBufferSource source, final int packedLight, final int packedOverlay) {
        final Curve currCat = conn.getCatenary();
        final Curve prevCat = conn.getPrevCatenary();
    }
}
