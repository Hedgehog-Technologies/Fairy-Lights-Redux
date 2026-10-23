package org.hedgetech.fairylightsredux.mixin;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerGamePacketListenerImpl.class)
public interface MixinServerGamePacketListenerImpl {
    @Invoker("tryPickItem")
    void fairylightsredux$tryPickItem(ItemStack stack);
}
