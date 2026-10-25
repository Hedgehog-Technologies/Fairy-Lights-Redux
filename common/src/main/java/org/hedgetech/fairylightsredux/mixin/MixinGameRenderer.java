package org.hedgetech.fairylightsredux.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.hedgetech.fairylightsredux.client.ConnectionHitHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {
    @Inject(method = "pick(F)V", at = @At("TAIL"))
    private void fairylightsredux$pickConnection(final float partialTick, final CallbackInfo ci) {
        ConnectionHitHandler.update(partialTick);
    }
}
