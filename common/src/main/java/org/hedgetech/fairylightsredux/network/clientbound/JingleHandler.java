package org.hedgetech.fairylightsredux.network.clientbound;

import org.hedgetech.fairylightsredux.connection.HangingLightsConnection;
import org.hedgetech.fairylightsredux.jingle.Jingle;
import org.hedgetech.fairylightsredux.network.ClientMessageContext;
import org.hedgetech.fairylightsredux.network.ConnectionMessage;
import org.hedgetech.fairylightsredux.network.Handler;

public final class JingleHandler implements Handler<JingleMessage, ClientMessageContext> {
    @Override
    public void handle(final JingleMessage message, final ClientMessageContext context) {
        final Jingle jingle = message.jingle;
        if (jingle != null) {
            ConnectionMessage
                    .<HangingLightsConnection>getConnection(
                            message,
                            c -> c instanceof HangingLightsConnection,
                            context.getLevel()
                    )
                    .ifPresent(connection ->
                            connection.play(jingle, message.getLightOffset())
                    );
        }
    }
}
