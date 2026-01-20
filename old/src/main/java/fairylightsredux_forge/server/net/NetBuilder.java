package org.hedgetech.fairylightsredux.server.net;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.SimpleChannel;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class NetBuilder {
    private final ChannelBuilder builder;
    private SimpleChannel channel;
    private int id;

    public NetBuilder(ResourceLocation name) {
        this.builder = ChannelBuilder.named(name);
    }

    public NetBuilder version(int version) {
        this.builder.networkProtocolVersion(version);
        this.builder.clientAcceptedVersions(Channel.VersionTest.exact(version));
        this.builder.clientAcceptedVersions(Channel.VersionTest.exact(version));
        return this;
    }

    private SimpleChannel channel() {
        if (this.channel == null) {
            this.channel = this.builder.simpleChannel();
        }
        return this.channel;
    }

    /* -------------------------
     * Registration entry points
     * ------------------------- */

    public <T extends IMessage> NetBuilder serverbound(Class<T> type, Function<T, BiConsumer<T, ServerPlayer>> handler) {
        register(type, NetworkDirection.PLAY_TO_SERVER, handler);
        return this;
    }

    public <T extends IMessage> NetBuilder clientbound(Class<T> type, Function<T, BiConsumer<T, Minecraft>> handler) {
        register(type, NetworkDirection.PLAY_TO_CLIENT, handler);
        return this;
    }

    private <T extends IMessage, C> void register(Class<T> type, NetworkDirection direction, Function<T, BiConsumer<T, C>> handlerFactory) {
        channel()
                .messageBuilder(type, id++, direction)
                .encoder(IMessage::encode)
                .decoder(buf -> {
                    try {
                        T msg = type.getDeclaredConstructor().newInstance();
                        msg.decode(buf);
                        return msg;
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
                .consumerMainThread((msg, ctx) -> {
                    C target = (C) ctx.getSender();
                    handlerFactory.apply(msg).accept(msg, target);
                })
                .add();
    }
}
