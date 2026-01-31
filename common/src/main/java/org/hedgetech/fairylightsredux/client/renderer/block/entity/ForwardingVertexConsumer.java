package org.hedgetech.fairylightsredux.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;

public abstract class ForwardingVertexConsumer implements VertexConsumer {
    protected abstract VertexConsumer delegate();

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        return this.delegate().addVertex(x, y, z);
    }


}
