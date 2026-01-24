package org.hedgetech.fairylightsredux.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.hedgetech.fairylightsredux.connection.HangingLightsConnection;
import org.hedgetech.fairylightsredux.jingle.Jingle;
import org.hedgetech.fairylightsredux.network.ClientMessageContext;
import org.hedgetech.fairylightsredux.network.ConnectionMessage;

import java.util.function.BiConsumer;

public class JingleMessage extends ConnectionMessage {
    private int lightOffset;

    public Jingle jingle;

    public JingleMessage() {}

    public JingleMessage(final HangingLightsConnection connection, final int lightOffset, final Jingle jingle) {
        super(connection);
        this.lightOffset = lightOffset;
        this.jingle = jingle;
    }

    public int getLightOffset() {
        return this.lightOffset;
    }

    @Override
    public void encode(final FriendlyByteBuf buf) {
        super.encode(buf);
        buf.writeVarInt(this.lightOffset);
        this.jingle.write(buf);
    }

    @Override
    public void decode(final FriendlyByteBuf buf) {
        super.decode(buf);
        this.lightOffset = buf.readInt();
        this.jingle = Jingle.read(buf);
    }
}
