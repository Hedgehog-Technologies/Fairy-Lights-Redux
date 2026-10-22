package org.hedgetech.fairylightsredux.mixin;

import net.minecraft.world.entity.player.Player;
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.fastener.IFastenerHolder;
import org.hedgetech.fairylightsredux.server.fastener.PlayerFastener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class MixinPlayer implements IFastenerHolder {
    @Unique
    private PlayerFastener fairylightsredux$fastener;

    @Override
    public Fastener<?> getFastener() {
        if (this.fairylightsredux$fastener == null) {
            this.fairylightsredux$fastener = new PlayerFastener((Player) (Object) this);
        }

        return this.fairylightsredux$fastener;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void fairylightsredux$tickFastener(final CallbackInfo ci) {
        
    }
}
