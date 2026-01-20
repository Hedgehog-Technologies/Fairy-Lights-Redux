package org.hedgetech.fairylightsredux;

import io.netty.util.AttributeKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.ForgePacketHandler;
import net.minecraftforge.network.SimpleChannel;
import org.hedgetech.fairylightsredux.server.net.NetBuilder;

import java.util.function.BiConsumer;

public final class FLRNetwork {
    private static final ResourceLocation HANDSHAKE_NAME = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "handshake");
    public static final AttributeKey<ForgePacketHandler> CONTEXT = AttributeKey.newInstance(HANDSHAKE_NAME.toString());

    public static final SimpleChannel NETWORK = new NetBuilder(Constants.NETWORK)
            .version(1).optionalServer().requiredClient()


//    public static final SimpleChannel NETWORK = ChannelBuilder
//            .named(Constants.NETWORK)
//            .networkProtocolVersion(1)
//            .optionalServer()
//            .simpleChannel()
//                .play()
//                    .clientbound()
//                        .add(JingleMessage.class, JingleMessage.STREAM_CODEC, JingleMessage.Handler)
//            .build();

    private interface Handler<MSG> {
        void handle(ForgePacketHandler handler, MSG msg, CustomPayloadEvent.Context ctx);
    }

    private static <MSG> BiConsumer<MSG, CustomPayloadEvent.Context> ctx(Handler<MSG> handler) {
        return (msg, ctx) -> {
            var inst = ctx.getConnection().channel().attr(CONTEXT).get();
            handler.handle(inst, msg, ctx);
        };
    }

    private FLRNetwork() {}
}
