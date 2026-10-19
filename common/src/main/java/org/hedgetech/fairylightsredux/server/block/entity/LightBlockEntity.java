package org.hedgetech.fairylightsredux.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.block.LightBlock;
import org.hedgetech.fairylightsredux.server.feature.light.Light;
import org.hedgetech.fairylightsredux.server.item.DyeableItem;
import org.hedgetech.fairylightsredux.server.item.LightVariant;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;
import org.hedgetech.fairylightsredux.util.FLRMth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class LightBlockEntity extends BlockEntity {
    private Light<?> light;
    private boolean on = true;

    public LightBlockEntity(BlockPos pos, BlockState state) {
        super(FLRBlockEntities.LIGHT, pos, state);
        this.light = new Light<>(0, Vec3.ZERO, 0.0F, 0.0F, ItemStack.EMPTY, LightVariant.FAIRY_LIGHT, 0.0F);
    }

    public Light<?> getLight() {
        return this.light;
    }

    public void setItemStack(final ItemStack stack) {
        this.light = new Light<>(0, Vec3.ZERO, 0.0F, 0.0F, stack, LightVariant.get(stack).orElse(LightVariant.FAIRY_LIGHT), 0.0F);
        this.setChanged();
    }

    public void setColor(final DyeColor dye) {
        this.setItemStack(DyeableItem.setColor(this.light.getItem().copy(), dye));
        this.light.power(this.on, true);
        this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
    }

    private void setOn(final boolean on, final boolean now) {
        this.on = on;
        this.light.power(on, now);
        this.setChanged();
    }

    public void interact(final Level world, final BlockPos pos, final BlockState state, final Player player, final InteractionHand hand, final BlockHitResult hit) {
        this.setOn(!this.on, true);
        world.setBlockAndUpdate(pos, state.setValue(LightBlock.LIT, this.on));
        final SoundEvent lightSnd;
        final float pitch;

        if (this.on) {
            lightSnd = FLRSounds.FEATURE_LIGHT_TURN_ON;
            pitch = 0.6F;
        } else {
            lightSnd = FLRSounds.FEATURE_LIGHT_TURN_OFF;
            pitch = 0.5F;
        }

        this.level.playSound(null, pos, lightSnd, SoundSource.BLOCKS, 1.0F, pitch);
    }

    public static void tickClient(final Level world, final BlockPos pos, final BlockState state, final LightBlockEntity be) {
        be.light.tick(world, be.getLightOrigin());
    }

    public void animateTick() {
        this.light.getBehavior().animateTick(this.level, this.getLightOrigin(), this.light);
    }

    private Vec3 getLightOrigin() {
        final BlockState state = this.getBlockState();
        final AttachFace face = state.getValue(LightBlock.FACE);
        final float rotation = state.getValue(LightBlock.FACING).toYRot();
        final Matrix4f matrix = new Matrix4f();
        matrix.translate(0.5F, 0.5F, 0.5F);
        matrix.rotateY((float) Math.toRadians(180.0F - rotation));

        if (this.light.getVariant().isOrientable()) {
            if (face == AttachFace.WALL) {
                matrix.rotateX(FLRMth.PI / 2.0F);
            } else if (face == AttachFace.FLOOR) {
                matrix.rotateX(-FLRMth.PI);
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

        final Vector3f origin = matrix.transformPosition(new Vector3f());
        return Vec3.atLowerCornerOf(this.worldPosition).add(origin.x, origin.y, origin.z);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.store("item", ItemStack.OPTIONAL_CODEC, this.light.getItem());
        output.putBoolean("on", this.on);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        final ItemStack stack = input.read("item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);

        if (!ItemStack.matches(stack, this.light.getItem())) this.setItemStack(stack);

        this.setOn(input.getBooleanOr("on", true), this.level == null);
    }
}
