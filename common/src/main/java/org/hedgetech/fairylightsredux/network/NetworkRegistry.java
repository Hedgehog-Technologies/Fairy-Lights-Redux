package org.hedgetech.fairylightsredux.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class NetworkRegistry {
    private final List<MessageRegistration<?>> registrations = new ArrayList<>();
    private final String version;

    public NetworkRegistry(String version) {
        this.version = version;
    }

    public <T extends Message> NetworkRegistry clientbound(Class<T> type, Supplier<T> factory, Supplier<Handler<T, ClientMessageContext>> handler) {
        registrations.add(new MessageRegistration<>(Direction.CLIENTBOUND, type, factory, handler));
        return this;
    }

    public <T extends Message> NetworkRegistry serverbound(Class<T> type, Supplier<T> factory, Supplier<Handler<T, ServerMessageContext>> handler) {
        registrations.add(new MessageRegistration<>(Direction.SERVERBOUND, type, factory, handler));
        return this;
    }

    public List<MessageRegistration<?>> getRegistrations() {
        return this.registrations;
    }

    public String getVersion() {
        return this.version;
    }

    public enum Direction { CLIENTBOUND, SERVERBOUND }

    public record MessageRegistration<T extends Message>(
            Direction direction,
            Class<T> type,
            Supplier<T> factory,
            Supplier<?> handler
    ) {}
}
