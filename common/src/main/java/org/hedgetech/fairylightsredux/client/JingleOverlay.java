package org.hedgetech.fairylightsredux.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.hedgetech.fairylightsredux.Constants;
import org.hedgetech.fairylightsredux.server.connection.HangingLightsConnection;
import org.hedgetech.fairylightsredux.server.jingle.Jingle;

import java.util.List;

public final class JingleOverlay {
    public static final ResourceLocation ID = Constants.id("jingle");

    public static void render(final GuiGraphics graphics) {
        final ConnectionHitHandler.Hit hit = ConnectionHitHandler.getHit();
        if (hit == null || !(hit.connection() instanceof final HangingLightsConnection lights) || lights.getPlayingJingle() == null) {
            return;
        }

        final Jingle jingle = lights.getPlayingJingle();
        final Font font = Minecraft.getInstance().font;
        final List<Component> lines = List.of(
                Component.translatable("gui.fairylightsredux.jingle.song", jingle.getTitle()),
                Component.translatable("gui.fairylightsredux.jingle.artist", jingle.getArtist())
        );

        for (int i = 0; i < lines.size(); i++) {
            final Component line = lines.get(i);
            final int y = 2 + font.lineHeight * i;
            graphics.fill(1, y - 1, 2 + font.width(line) + 1, y + font.lineHeight - 1, 0x90505050);
            graphics.drawString(font, line, 2, y, 0xFFE0E0E0);
        }
    }

    private JingleOverlay() {}
}
