package org.hedgetech.fairylightsredux.server.net.interfaces;

import net.minecraft.network.FriendlyByteBuf;

public interface MessageInterface {
    void encode(final FriendlyByteBuf buf);

    void decode(final FriendlyByteBuf buf);
}
