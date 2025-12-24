package org.hedgetech.fairylightsredux.server.net.interfaces;

import net.minecraftforge.event.network.CustomPayloadEvent;
import org.hedgetech.fairylightsredux.server.net.MessageContext;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface ConsumerFactoryInterface<T extends MessageInterface, S extends MessageContext> {
    BiConsumer<T, Supplier<CustomPayloadEvent.Context>> create(final Supplier<BiConsumer<? super T, S>> handlerFactory);
}
