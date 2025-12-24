package org.hedgetech.fairylightsredux.server.net;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.simple.SimpleConnection;
import org.hedgetech.fairylightsredux.server.net.interfaces.ConsumerFactoryInterface;
import org.hedgetech.fairylightsredux.server.net.interfaces.MessageInterface;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class NetBuilder {
    private final ChannelBuilder builder;
    private int version = -1;
    private SimpleChannel channel;
    private int id;

    public NetBuilder(final ResourceLocation name) {
        this.builder = ChannelBuilder.named(name);
    }

    public NetBuilder version(final int version) {
        if (this.version == -1 && version >= 0) {
            this.version = version;
            this.builder.networkProtocolVersion(version);
            return this;
        }

        throw new IllegalArgumentException("Version already assigned");
    }

    public NetBuilder optionalServer() {
        this.builder.clientAcceptedVersions(this.optionalVersion());
        return this;
    }

    public NetBuilder requiredServer() {
        this.builder.clientAcceptedVersions(this.requiredVersion());
        return this;
    }

    public NetBuilder optionalClient() {
        this.builder.serverAcceptedVersions(this.optionalVersion());
        return this;
    }

    public NetBuilder requiredClient() {
        this.builder.serverAcceptedVersions(this.requiredVersion());
        return this;
    }

    private Channel.VersionTest optionalVersion() {
        final int v = this.version;
        if (v < 0) {
            throw new IllegalStateException("Version not specified");
        }
        return Channel.VersionTest.ACCEPT_VANILLA.or(
                Channel.VersionTest.ACCEPT_MISSING.or(
                        Channel.VersionTest.exact(v)
                )
        );
    }

    private Channel.VersionTest requiredVersion() {
        final int v = this.version;
        if (v < 0) {
            throw new IllegalStateException("Version not specified");
        }
        return Channel.VersionTest.exact(v);
    }

    private SimpleChannel channel() {
        if (this.channel == null) {
            this.channel = this.builder.simpleChannel();
        }
        return this.channel;
    }

    public SimpleChannel build() {
        return this.channel();
    }

    public class MessageBuilder<T extends MessageInterface, S extends MessageContext> {
        private final Supplier<T> factory;
        private final ConsumerFactoryInterface<T, S> consumerFactory;

        protected MessageBuilder(final Supplier<T> factory, final ConsumerFactoryInterface<T, S> consumerFactory) {
            this.factory = factory;
            this.consumerFactory = consumerFactory;
        }

        public NetBuilder consumer(final Supplier<BiConsumer<? super T, S>> consumer) {
            final Supplier<T> factory = this.factory;
            final Class<T> type = (Class<T>) factory.get().getClass();
            NetBuilder.this.channel().
//            NetBuilder.this.channel().messageBuilder(type, NetBuilder.this.id++)
//                    .encoder(MessageInterface::encode)
//                    .decoder(buf -> {
//                        final T msg = factory.get();
//                        msg.decode(buf);
//                        return msg;
//                    })
//                    .consumerMainThread(this.consumerFactory.create(consumer))
//                    .add();

            return NetBuilder.this;
        }
    }
}
