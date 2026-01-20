package org.hedgetech.fairylightsredux.content.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.hedgetech.fairylightsredux.Constants;

public record SoundDef(
        String id
) {
    public SoundEvent createSoundEvent() {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, this.id()));
    }
}
