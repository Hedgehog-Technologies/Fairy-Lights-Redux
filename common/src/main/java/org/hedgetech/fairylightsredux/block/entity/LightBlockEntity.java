package org.hedgetech.fairylightsredux.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.block.LightBlock;
import org.hedgetech.fairylightsredux.content.block.BlockDefs;
import org.hedgetech.fairylightsredux.content.sound.SoundDefs;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.hedgetech.fairylightsredux.item.LightVariantUtil;
import org.hedgetech.fairylightsredux.item.SimpleLightVariant;
import org.hedgetech.fairylightsredux.registry.FLRBlockEntities;
import org.hedgetech.fairylightsredux.registry.FLRSounds;
import org.hedgetech.fairylightsredux.util.FLRMth;
import org.hedgetech.fairylightsredux.util.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LightBlockEntity extends BlockEntity {
    private Light<?> light;
    private boolean on = true;

    public LightBlockEntity(BlockPos pos, BlockState state) {
        super(FLRBlockEntities.get(BlockDefs.LIGHT), pos, state);
        this.light = new Light<>(0, Vec3.ZERO, 0.0F, 0.0F, ItemStack.EMPTY, SimpleLightVariant.FAIRY_LIGHT, 0.0F);
    }

    public Light<?> getLight() {
        return this.light;
    }

    public void setItemStack(final ItemStack stack) {
        this.light = new Light<>(0, Vec3.ZERO, 0.0F, 0.0F, stack, Objects.requireNonNull(LightVariantUtil.get(stack)), 0.0F);
        this.setChanged();
    }

    private void setOn(final boolean on) {
        this.on = on;
        this.light.power(on, true);
        this.setChanged();
    }

    public void interact(final Level world, final BlockPos pos, final BlockState state, final Player player, final BlockHitResult hit) {
        this.setOn(!this.on);
        world.setBlockAndUpdate(pos, state.setValue(LightBlock.LIT, this.on));
        final SoundEvent lightSnd;
        final float pitch;
        if (this.on) {
            lightSnd = FLRSounds.get(SoundDefs.FEATURE_LIGHT_TURNON);
            pitch = 0.6F;
        } else {
            lightSnd = FLRSounds.get(SoundDefs.FEATURE_LIGHT_TURNOFF);
            pitch = 0.5F;
        }
        this.level.playSound(null, pos, lightSnd, SoundSource.BLOCKS, 1.0F, pitch);
    }

    public void animateTick() {
        final BlockState state = this.getBlockState();
        final AttachFace face = state.getValue(LightBlock.FACE);
        final float rotation = state.getValue(LightBlock.FACING).toYRot();
        final MatrixStack matrix = new MatrixStack();
        matrix.translate(0.5F, 0.5F, 0.5F);
        matrix.rotate((float) Math.toRadians(180.0F - rotation), 0.0F, 1.0F, 0.0F);
        if (this.light.getVariant().isOrientable()) {
            if (face == AttachFace.WALL) {
                matrix.rotate(FLRMth.HALF_PI, 1.0F, 0.0F, 0.0F);
            } else if (face == AttachFace.FLOOR) {
                matrix.rotate(-FLRMth.PI, 1.0F, 0.0F, 0.0F);
            }
            matrix.translate(0.0F, 0.5F, 0.0F);
        } else {
            if (face == AttachFace.CEILING) {
                matrix.translate(0.0F, 0.25F, 0.0F);
            } else if (face == AttachFace.WALL) {
                matrix.translate(0.0F, 3.0F / 16.0F, 0.125F);
            } else {
                matrix.translate(0.0F, -(float) this.light.getVariant().getBounds().minY - 0.5F, 0.0F);
            }
        }
        this.light.getBehavior().animateTick(this.level, Vec3.atLowerCornerOf(this.worldPosition).add(matrix.transform(Vec3.ZERO)), this.light);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.store("light", ItemStack.CODEC, this.light.getItem());
        output.putBoolean("on", this.on);
    }

    @Override
    public void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.setItemStack(input.read("light", ItemStack.CODEC).orElse(ItemStack.EMPTY));
        this.setOn(input.getBooleanOr("on", true));
    }
}
