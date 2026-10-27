package org.hedgetech.fairylightsredux.server.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.hedgetech.fairylightsredux.Constants;

import java.util.function.BiConsumer;

public final class FLRSounds {

    public static SoundEvent JINGLE_BELL;
    public static SoundEvent CORD_CONNECT;
    public static SoundEvent CORD_DISCONNECT;
    public static SoundEvent CORD_STRETCH;
    public static SoundEvent CORD_SNAP;
    public static SoundEvent FEATURE_COLOR_CHANGE;
    public static SoundEvent FEATURE_LIGHT_TURN_ON;
    public static SoundEvent FEATURE_LIGHT_TURN_OFF;

    public static void register(final BiConsumer<ResourceLocation, SoundEvent> registry) {
        JINGLE_BELL = create(registry, "jingle_bell");
        CORD_CONNECT = create(registry, "cord.connect");
        CORD_DISCONNECT = create(registry, "cord.disconnect");
        CORD_STRETCH = create(registry, "cord.stretch");
        CORD_SNAP = create(registry, "cord.snap");
        FEATURE_COLOR_CHANGE = create(registry, "feature.color_change");
        FEATURE_LIGHT_TURN_ON = create(registry, "feature.light_turn_on");
        FEATURE_LIGHT_TURN_OFF = create(registry, "feature.light_turn_off");
    }

    private static SoundEvent create(final BiConsumer<ResourceLocation, SoundEvent> registry, final String name) {
        final ResourceLocation id = Constants.id(name);
        final SoundEvent sound = SoundEvent.createVariableRangeEvent(id);
        registry.accept(id, sound);
        return sound;
    }

    private FLRSounds() {}
}
