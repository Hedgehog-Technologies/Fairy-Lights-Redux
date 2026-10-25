package org.hedgetech.fairylightsredux.client.renderer;

import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class UnsortedRenderType extends RenderType {
    private static final Function<ResourceLocation, RenderType> ENTITY_TRANSLUCENT = Util.memoize(texture -> new UnsortedRenderType(RenderType.entityTranslucent(texture)));

    private final RenderType sorted;

    private UnsortedRenderType(final RenderType sorted) {
        super(Constants.MOD_ID + "_unsorted_" + sorted, sorted.bufferSize(), sorted.affectsCrumbling(), false, sorted::setupRenderState, sorted::clearRenderState);
        this.sorted = sorted;
    }

    public static RenderType entityTranslucent(final ResourceLocation texture) {
        return ENTITY_TRANSLUCENT.apply(texture);
    }

    @Override
    public void draw(final @NotNull MeshData meshData) {
        this.sorted.draw(meshData);
    }

    @Override
    public @NotNull VertexFormat format() {
        return this.sorted.format();
    }

    @Override
    public VertexFormat.@NotNull Mode mode() {
        return this.sorted.mode();
    }
}
