package org.hedgetech.fairylightsredux.server.net;

import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkContext;

public abstract class MessageContext {
    protected final NetworkContext context;

    public MessageContext(final NetworkContext context) {
        this.context = context;
    }

    public abstract LogicalSide getSide();
}
