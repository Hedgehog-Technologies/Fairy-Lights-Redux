package org.hedgetech.fairylightsredux.server.capability;

import org.hedgetech.fairylightsredux.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public final class CapabilityHandler {
    public static final ResourceLocation FASTENER_ID = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "fastener");

    public static final Capability<Fastener<?>> FASTENER_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    private CapabilityHandler() {}
}
