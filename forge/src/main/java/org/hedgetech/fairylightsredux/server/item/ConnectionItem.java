package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import org.hedgetech.fairylightsredux.components.FastenerComponent;
import org.hedgetech.fairylightsredux.components.Fasteners;
import org.hedgetech.fairylightsredux.server.block.FLBlocks;
import org.hedgetech.fairylightsredux.server.block.FastenerBlock;
import org.hedgetech.fairylightsredux.server.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.server.sound.FLSounds;

import java.util.Optional;

/**
 * Forge-side port of the legacy ConnectionItem. This version replaces capability lookups
 * with Fasteners.getOrCreate(...) to retrieve the component. Full fastener behaviour
 * (connections, reconnection, etc.) must be implemented against the new component API
 * in subsequent steps. For now these operations are best-effort placeholders that avoid
 * direct capability usage.
 */
public abstract class ConnectionItem extends Item {
    private final RegistryObject<?> type;

    public ConnectionItem(final Properties properties, final RegistryObject<?> type) {
        super(properties);
        this.type = type;
    }

    public Object getConnectionType() {
        return this.type.get();
    }

    @Override
    public InteractionResult useOn(final UseOnContext context) {
        final Player user = context.getPlayer();
        if (user == null) {
            return super.useOn(context);
        }
        final Level world = context.getLevel();
        final Direction side = context.getClickedFace();
        final Block fastener = FLBlocks.FASTENER.get();
        final ItemStack stack = context.getItemInHand();
        if (this.isConnectionInOtherHand(world, user, stack)) {
            return InteractionResult.PASS;
        }
        final BlockState fastenerState = fastener.defaultBlockState().setValue(FastenerBlock.FACING, side);
        final BlockState currentBlockState = world.getBlockState(context.getClickedPos());
        final BlockPlaceContext blockContext = new BlockPlaceContext(context);
        final BlockPos placePos = blockContext.getClickedPos();
        if (currentBlockState.getBlock() == fastener) {
            if (!world.isClientSide()) {
                this.connect(stack, user, world, context.getClickedPos());
            }
            return InteractionResult.SUCCESS;
        } else if (blockContext.canPlace() && fastenerState.canSurvive(world, placePos)) {
            if (!world.isClientSide()) {
                this.connect(stack, user, world, placePos, fastenerState);
            }
            return InteractionResult.SUCCESS;
        } else {
            final HangingEntity entity = FenceFastenerEntity.findHanging(world, context.getClickedPos());
            if (entity == null || entity instanceof FenceFastenerEntity) {
                if (!world.isClientSide()) {
                    this.connectFence(stack, user, world, context.getClickedPos(), (FenceFastenerEntity) entity);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    private boolean isConnectionInOtherHand(final Level world, final Player user, final ItemStack stack) {
        // Replaced capability lookup with component access; full logic not yet ported.
        final FastenerComponent comp = Fasteners.getOrCreate(user);
        if (comp == null) return false;
        // TODO: port Fastener.getFirstConnection() logic to the component API.
        return false;
    }

    private void connect(final ItemStack stack, final Player user, final Level world, final BlockPos pos) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity != null) {
            final FastenerComponent comp = Fasteners.getOrCreate(entity);
            if (comp != null) {
                // TODO: implement connect using component-based fastener representation
            }
        }
    }

    private void connect(final ItemStack stack, final Player user, final Level world, final BlockPos pos, final BlockState state) {
        if (world.setBlock(pos, state, 3)) {
            state.getBlock().setPlacedBy(world, pos, state, user, stack);
            final SoundType sound = state.getBlock().getSoundType(state, world, pos, user);
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                sound.getPlaceSound(),
                SoundSource.BLOCKS,
                (sound.getVolume() + 1) / 2,
                sound.getPitch() * 0.8F
            );
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity != null) {
                final FastenerComponent comp = Fasteners.getOrCreate(entity);
                if (comp != null) {
                    // TODO: implement connect(destination) using component-based fastener
                }
            }
        }
    }

    // Placeholder overloads using the component representation
    public void connect(final ItemStack stack, final Player user, final Level world, final FastenerComponent fastener) {
        this.connect(stack, user, world, fastener, true);
    }

    public void connect(final ItemStack stack, final Player user, final Level world, final FastenerComponent fastener, final boolean playConnectSound) {
        final FastenerComponent attacher = Fasteners.getOrCreate(user);
        if (attacher == null) return;
        // TODO: Perform connection logic using attacher & fastener component data.
        if (playConnectSound) {
            final Vec3 pos = new Vec3(0, 0, 0);
            world.playSound(null, pos.x, pos.y, pos.z, FLSounds.CORD_CONNECT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    private void connectFence(final ItemStack stack, final Player user, final Level world, final BlockPos pos, FenceFastenerEntity fastener) {
        final boolean playConnectSound;
        if (fastener == null) {
            fastener = FenceFastenerEntity.create(world, pos);
            playConnectSound = false;
        } else {
            playConnectSound = true;
        }
        final FastenerComponent dest = Fasteners.getOrCreate(fastener);
        if (dest != null) {
            this.connect(stack, user, world, dest, playConnectSound);
        }
    }

    public static boolean isFence(final BlockState state) {
        return state.isSolid() && state.is(net.minecraft.tags.BlockTags.FENCES);
    }
}

