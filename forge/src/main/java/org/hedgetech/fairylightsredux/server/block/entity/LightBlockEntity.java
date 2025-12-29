package org.hedgetech.fairylightsredux.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.hedgetech.fairylightsredux.server.item.LightVariant;
import org.hedgetech.fairylightsredux.server.item.SimpleLightVariant;

public class LightBlockEntity extends BlockEntity {
    private Light<?> light;
    private boolean on = true;

    public LightBlockEntity(BlockPos pos, BlockState state) {
        super(FLRBlockEntities.LIGHT.get(), pos, state);
        this.light = new Light<>(0, Vec3.ZERO, 0.0F, 0.0F, ItemStack.EMPTY, SimpleLightVariant.FAIRY_LIGHT, 0.0F);
    }

    public Light<?> getLight() {
        return this.light;
    }

    public void setItemStack(final ItemStack stack) {
        this.light = new Light<>(0, Vec3.ZERO, 0.0F, 0.0F, stack, LightVariant.get(stack).orElse(SimpleLightVariant.FAIRY_LIGHT), 0.0F);
        this.setChanged();
    }

    private void setOn(final boolean on) {
        this.setOn(!this.on);
        this.light.power(on, true);
        this.setChanged();
    }
}
