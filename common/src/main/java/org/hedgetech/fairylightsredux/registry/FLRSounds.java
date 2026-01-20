package org.hedgetech.fairylightsredux.registry;

import net.minecraft.sounds.SoundEvent;
import org.hedgetech.fairylightsredux.content.sound.SoundDef;
import org.hedgetech.fairylightsredux.content.sound.SoundDefs;

import java.util.HashMap;
import java.util.Map;

public final class FLRSounds {
    private static final Map<String, SoundEvent> SOUNDS = new HashMap<>();

    public static void registerAll(RegistryBridge bridge) {
        for (SoundDef def : SoundDefs.SOUNDS) {
            SoundEvent se = bridge.registerSound(
                    def.id(),
                    def
            );
            SOUNDS.put(def.id(), se);
        }
    }

    public static SoundEvent get(String id) {
        return SOUNDS.get(id);
    }

    private FLRSounds() {}
}
