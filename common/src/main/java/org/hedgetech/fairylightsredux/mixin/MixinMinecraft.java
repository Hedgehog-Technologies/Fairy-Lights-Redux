package org.hedgetech.fairylightsredux.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import org.hedgetech.fairylightsredux.client.ConnectionHitHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Shadow
    public LocalPlayer player;

    @Shadow
    public MultiPlayerGameMode gameMode;

    @Shadow
    protected int missTime;

    @Shadow
    private int rightClickDelay;

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void fairylightsredux$attackConnection(final CallbackInfoReturnable<Boolean> cir) {
        if (this.missTime <= 0 && !this.player.isHandsBusy() && ConnectionHitHandler.attack(this.player)) {
            this.player.swing(InteractionHand.MAIN_HAND);
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
    private void fairylightsredux$useConnection(final CallbackInfo ci) {
        if (!this.gameMode.isDestroying() && !this.player.isHandsBusy() && ConnectionHitHandler.use(this.player)) {
            this.rightClickDelay = 4;
            this.player.swing(InteractionHand.MAIN_HAND);
            ci.cancel();
        }
    }

    @Inject(method = "pickBlock", at = @At("HEAD"), cancellable = true)
    private void fairylightsredux$pickConnection(final CallbackInfo ci) {
        if (ConnectionHitHandler.pick()) {
            ci.cancel();
        }
    }
}
