package org.hedgetech.fairylightsredux.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.hedgetech.fairylightsredux.server.block.FastenerBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoteBlock.class)
public class MixinNoteBlock {
    @Inject(method = "triggerEvent", at = @At("HEAD"), cancellable = true)
    private void fairylightsredux$ringBell(final BlockState state, final Level world, final BlockPos pos, final int id, final int param, final CallbackInfoReturnable<Boolean> cir) {
        if (FastenerBlock.ringNoteBlockBell(world, pos, state, state.getValue(NoteBlock.NOTE))) {
            cir.setReturnValue(false);
        }
    }
}
