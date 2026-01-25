package org.hedgetech.fairylightsredux.content.item;

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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.hedgetech.fairylightsredux.block.FastenerBlock;
import org.hedgetech.fairylightsredux.entity.FenceFastenerEntity;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.registry.FLRBlocks;
import org.jetbrains.annotations.NotNull;

public abstract class ConnectionItem extends Item {
    public ConnectionItem(Properties props) {
        super(props);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        final Player player = ctx.getPlayer();
        if (player == null) return super.useOn(ctx);

        final Level world = ctx.getLevel();
        final Direction targetSide = ctx.getClickedFace();
        final BlockPos clickPos = ctx.getClickedPos();
        final Block fastener = FLRBlocks.get("fastener");
        final ItemStack heldStack = ctx.getItemInHand();

        if (this.isConnectionInOtherHand(world, player, heldStack)) {
            return InteractionResult.PASS;
        }

        final BlockState fastenerState = fastener.defaultBlockState().setValue(FastenerBlock.FACING, targetSide);
        final BlockState currentTargetState = world.getBlockState(clickPos);
        final BlockPlaceContext blockCtx = new BlockPlaceContext(ctx);
        final BlockPos placePos = blockCtx.getClickedPos();

        if (currentTargetState.getBlock() == fastener) {
            if (!world.isClientSide()) {
                this.connect(heldStack, player, world, clickPos);
            }
            return InteractionResult.SUCCESS;
        } else if (blockCtx.canPlace() && fastenerState.canSurvive(world, placePos)) {
            if (!world.isClientSide()) {
                this.connect(heldStack, player, world, placePos, fastenerState);
            }
            return InteractionResult.SUCCESS;
        } else if (isFence(currentTargetState)) {
            final BlockAttachedEntity entity = FenceFastenerEntity.findAttached(world, clickPos);
            if (entity == null || entity instanceof FenceFastenerEntity) {
                if (!world.isClientSide()) {
                    this.connectFence(heldStack, player, world, clickPos, (FenceFastenerEntity) entity);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean isConnectionInOtherHand(Level world, Player player, ItemStack heldStack) {
        // TODO figure out what capabilities equate to in vanilla
        throw new NotImplementedException("ConnectionItem.isConnectionInOtherHand");
//        final Fastener<?> attacher = user.getCapability(CapabilityHandler.FASTENER_CAP).orElseThrow(IllegalStateException::new);
//        return attacher.getFirstConnection().filter(connection -> {
//            final CompoundTag nbt = connection.serializeLogic();
//            return nbt.isEmpty() ? stack.hasTag() : !NbtUtils.compareNbt(nbt, stack.getTag(), true);
//        }).isPresent();
    }

    private void connect(ItemStack stack, Player player, Level world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof Fastener<?> fastener) {
            this.connect(stack, player, world, fastener);
        }
//        final BlockEntity entity = world.getBlockEntity(pos);
//        if (entity != null) {
//            entity.getCapability(CapabilityHandler.FASTENER_CAP).ifPresent(fastener -> this.connect(stack, user, world, fastener));
//        }
    }

    private void connect(ItemStack stack, Player player, Level world, BlockPos pos, BlockState state) {
        if (world.setBlock(pos, state, 3)) {
            state.getBlock().setPlacedBy(world, pos, state, player, stack);
            // FIXME Unsure if this will have the same result as `state.getBlock().getSoundType(state, world, pos, user);`
            final SoundType sound = state.getSoundType();
            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    sound.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (sound.getVolume() + 1) / 2,
                    sound.getPitch() * 0.8F
            );
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof Fastener<?> fastener) {
                this.connect(stack, player, world, fastener, false);
            }
        }
//        if (world.setBlock(pos, state, 3)) {
//            state.getBlock().setPlacedBy(world, pos, state, user, stack);
//            final SoundType sound = state.getBlock().getSoundType(state, world, pos, user);
//            world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
//                    sound.getPlaceSound(),
//                    SoundSource.BLOCKS,
//                    (sound.getVolume() + 1) / 2,
//                    sound.getPitch() * 0.8F
//            );
//            final BlockEntity entity = world.getBlockEntity(pos);
//            if (entity != null) {
//                entity.getCapability(CapabilityHandler.FASTENER_CAP).ifPresent(destination -> this.connect(stack, user, world, destination, false));
//            }
//        }
    }

    public void connect(ItemStack stack, Player player, final Level world, final Fastener<?> fastener) {
        this.connect(stack, player, world, fastener, true);
    }

    public void connect(ItemStack stack, Player player, Level world, Fastener<?> fastener, boolean playConnectSound) {

        throw new NotImplementedException("ConnectionItem.connect with Fastener");
//        user.getCapability(CapabilityHandler.FASTENER_CAP).ifPresent(attacher -> {
//            boolean playSound = playConnectSound;
//            final Optional<Connection> placing = attacher.getFirstConnection();
//            if (placing.isPresent()) {
//                final Connection conn = placing.get();
//                if (conn.reconnect(fastener)) {
//                    conn.onConnect(world, user, stack);
//                    stack.shrink(1);
//                } else {
//                    playSound = false;
//                }
//            } else {
//                final CompoundTag data = stack.getTag();
//                fastener.connect(world, attacher, this.getConnectionType(), data == null ? new CompoundTag() : data, false);
//            }
//            if (playSound) {
//                final Vec3 pos = fastener.getConnectionPoint();
//                world.playSound(null, pos.x, pos.y, pos.z, FLSounds.CORD_CONNECT.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
//            }
//        });
    }

    private void connectFence(ItemStack stack, Player player, Level world, BlockPos pos, FenceFastenerEntity entity) {
        boolean playConnectSound = true;
        if (entity == null) {
            entity = FenceFastenerEntity.create(world, pos);
            playConnectSound = false;
        }
        // FIXME Unsure if this check is equivalent to the original capability check
//        if (entity != null && entity instanceof Fastener<?> fastener) {
//            this.connect(stack, player, world, fastener, playConnectSound);
//        } else {
//            throw new IllegalStateException("FenceFastenerEntity is not a Fastener");
//        }
//        final boolean playConnectSound;
//        if (fastener == null) {
//            fastener = FenceFastenerEntity.create(world, pos);
//            playConnectSound = false;
//        } else {
//            playConnectSound = true;
//        }
//        this.connect(stack, user, world, fastener.getCapability(CapabilityHandler.FASTENER_CAP).orElseThrow(IllegalStateException::new), playConnectSound);
    }

    public static boolean isFence(BlockState state) {
        // TODO - figure out what sort of solid we want here
        // - collision -> isCollisionShapeFullBlock
        // - visual -> isSolidRender
        // - light -> getLightBlock == 15
        // - full cube -> isRedstoneConductor / isFaceSturdy
        // - mob spawn -> isValidSpawn
        return /*state.isSolid() &&*/ state.is(BlockTags.FENCES);
    }
}
