package org.hedgetech.fairylightsredux.server.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.hedgetech.fairylightsredux.server.Fastener.Fastener;
import org.hedgetech.fairylightsredux.server.Fastener.IFastenerHolder;
import org.hedgetech.fairylightsredux.server.block.FLRBlocks;
import org.hedgetech.fairylightsredux.server.block.FastenerBlock;
import org.hedgetech.fairylightsredux.server.connection.Connection;
import org.hedgetech.fairylightsredux.server.connection.ConnectionType;
import org.hedgetech.fairylightsredux.server.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class ConnectionItem extends Item {
    private final Supplier<? extends ConnectionType<?>> type;

    public ConnectionItem(final Properties properties, final Supplier<? extends ConnectionType<?>> type) {
        super(properties);
        this.type = type;
    }

    public final ConnectionType<?> getConnectionType() {
        return this.type.get();
    }

    @Override
    public @NotNull InteractionResult useOn(final UseOnContext context) {
        final Player user = context.getPlayer();
        if (user == null) return super.useOn(context);

        final Level world = context.getLevel();
        final Direction side = context.getClickedFace();
        final BlockPos clickPos = context.getClickedPos();
        final Block fastener = FLRBlocks.FASTENER;
        final ItemStack stack = context.getItemInHand();
        if (this.isConnectionInOtherHand(user, stack)) return InteractionResult.PASS;

        final BlockState fastenerState = fastener.defaultBlockState().setValue(FastenerBlock.FACING, side);
        final BlockState currentBlockState = world.getBlockState(clickPos);
        final BlockPlaceContext blockContext = new BlockPlaceContext(context);
        final BlockPos placePos = blockContext.getClickedPos();

        if (currentBlockState.getBlock() == fastener) {
            if (!world.isClientSide()) this.connect(stack, user, world, clickPos);
            return InteractionResult.SUCCESS;
        } else if (blockContext.canPlace() && fastenerState.canSurvive(world, placePos)) {
            if (!world.isClientSide()) this.connect(stack, user, world, placePos, fastenerState);
            return InteractionResult.SUCCESS;
        } else if (isFence(currentBlockState)) {
            final BlockAttachedEntity entity = FenceFastenerEntity.findHanging(world, placePos);
            if (entity == null || entity instanceof FenceFastenerEntity) {
                if (!world.isClientSide()) this.connectFence(stack, user, world, clickPos, (FenceFastenerEntity) entity);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean isConnectionInOtherHand(final Player user, final ItemStack stack) {
        return ((IFastenerHolder) user).getFastener().getFirstConnection().filter(connection -> !connection.matches(stack)).isPresent();
    }

    private void connect(final ItemStack stack, final Player user, final Level world, final BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof final IFastenerHolder holder) {
            this.connect(stack, user, world, holder.getFastener());
        }
    }

    private void connect(final ItemStack stack, final Player user, final Level world, final BlockPos pos, final BlockState state) {
        if (world.setBlock(pos, state, Block.UPDATE_ALL)) {
            state.getBlock().setPlacedBy(world, pos, state, user, stack);
            final SoundType sound = state.getSoundType();
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    sound.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (sound.getVolume() + 1) / 2,
                    sound.getPitch() * 0.8F
            );

            if (world.getBlockEntity(pos) instanceof final IFastenerHolder holder) {
                this.connect(stack, user, world, holder.getFastener(), false);
            }
        }
    }

    public void connect(final ItemStack stack, final Player user, final Level world, final Fastener<?> fastener) {
        this.connect(stack, user, world, fastener, true);
    }

    public void connect(final ItemStack stack, final Player user, final Level world, final Fastener<?> fastener, final boolean playConnectSound) {
        final Fastener<?> attacher = ((IFastenerHolder) user).getFastener();
        boolean playSound = playConnectSound;
        final Optional<Connection> placing = attacher.getFirstConnection();

        if (placing.isPresent()) {
            if (placing.get().reconnect(fastener)) {
                placing.get().onConnect(user, stack);
                stack.shrink(1);
            } else {
                playSound = false;
            }
        } else {
            fastener.connect(world, attacher, this.getConnectionType(), stack, false);
        }

        if (playSound) {
            final Vec3 pos = fastener.getConnectionPoint();
            world.playSound(null, pos.x, pos.y, pos.z, FLRSounds.CORD_CONNECT, SoundSource.BLOCKS, 1.0F, 1.0F);
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

        this.connect(stack, user, world, fastener.getFastener(), playConnectSound);
    }

    public static boolean isFence(final BlockState state) {
        return state.is(BlockTags.FENCES);
    }
}
