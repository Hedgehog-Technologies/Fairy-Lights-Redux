package org.hedgetech.fairylightsredux.network;

import org.hedgetech.fairylightsredux.network.clientbound.JingleHandler;
import org.hedgetech.fairylightsredux.network.clientbound.JingleMessage;

public final class FLRNetwork {
    public static final NetworkRegistry REGISTRY = new NetworkRegistry("1")
            .clientbound(JingleMessage.class, JingleMessage::new, JingleHandler::new);

    private FLRNetwork() {}
}
