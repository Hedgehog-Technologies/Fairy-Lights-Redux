package org.hedgetech.fairylightsredux.server.net;

import net.minecraft.network.FriendlyByteBuf;

public interface IMessage {
    void encode(final FriendlyByteBuf buf);

    void decode(final FriendlyByteBuf buf);
}
