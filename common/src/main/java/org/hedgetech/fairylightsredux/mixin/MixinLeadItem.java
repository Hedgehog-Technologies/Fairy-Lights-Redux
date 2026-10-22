package org.hedgetech.fairylightsredux.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.Level;
import org.hedgetech.fairylightsredux.server.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.server.item.ConnectionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeadItem.class)
public abstract class MixinLeadItem {
    @Inject(method = "bindPlayerMobs", at = @At("HEAD"), cancellable = true)
    private static void fairylightsredux$keepFenceForFastener(final Player player, final Level world, final BlockPos pos, final CallbackInfoReturnable<InteractionResult> cir) {
        if (player.getMainHandItem().getItem() instanceof ConnectionItem
            || player.getOffhandItem().getItem() instanceof ConnectionItem
            || FenceFastenerEntity.findHanging(world, pos) instanceof FenceFastenerEntity) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
