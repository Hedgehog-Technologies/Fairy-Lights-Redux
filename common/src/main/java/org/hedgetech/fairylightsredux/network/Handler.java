package org.hedgetech.fairylightsredux.network;

@FunctionalInterface
public interface Handler<T extends Message, C> {
    void handle(T message, C context);
}
