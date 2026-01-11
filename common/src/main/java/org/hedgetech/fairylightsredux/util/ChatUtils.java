package org.hedgetech.fairylightsredux.util;

public final class ChatUtils {
    public static boolean isAllowedChatCharacter(final char c) {
        return c != 167 && c >= ' ' && c != 127;
    }

    private ChatUtils() {}
}
