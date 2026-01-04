package org.hedgetech.fairylightsredux.server.net;

import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkContext;

public abstract class MessageContext {
    protected final CustomPayloadEvent.Context context;

    public MessageContext(final CustomPayloadEvent.Context context) {
        this.context = context;
    }

    public abstract LogicalSide getSide();
}
