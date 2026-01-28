package org.hedgetech.fairylightsredux.network;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;

public final class NetworkClientProxy {
    private static PlatformClientNetwork PLATFORM;

    @SuppressWarnings("deprecation")
    public static final Material SOLID_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entity/connections"));

    @SuppressWarnings("deprecation")
    public static final Material TRANSLUCENT_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "entity/connections"));

    public static void init(PlatformClientNetwork platform) {
        PLATFORM = platform;
    }
}
