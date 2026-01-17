package org.hedgetech.fairylightsredux.content.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

public abstract class ConnectionItem extends Item {
    public ConnectionItem(Properties props) {
        super(props);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext ctx) {
        final Player player = ctx.getPlayer();
        if (player == null) return super.useOn(ctx);

        Level world = ctx.getLevel();
        Direction targetSide = ctx.getClickedFace();
        BlockPos clickPos = ctx.getClickedPos();
        Block fastener = FLRBlocks.FASTENER.get();
        ItemStack heldStack = ctx.getItemInHand();

        if (this.isConnectionInOtherHand(world, player, heldStack)) {
            return InteractionResult.PASS;
        }

        BlockState fastenerState = fastener.defaultBlockState().setValue(FastenerBlock.FACING, side);
        BlockState currentTargetState = world.getBlockState(clickPos);
        BlockPlaceContext blockCtx = new BlockPlaceContext(ctx);
        BlockPos placePos = blockCtx.getClickedPos();

        if (currentTargetState.getBlock() == fastener) {
            if (!world.isClientSide()) {
                this.connect(heldStack, player, world, clickPos);
            }
            return InteractionResult.SUCCESS;
        } else if (isFence(currentTargetState)) {
            HangingEntity entity = FenceFastenerEntity.findHanging(world, clickPos);
            if (entity == null || entity instanceof FenceFastenerEntity ffe) {
                if (!world.isClientSide()) {
                    this.connectFence(heldStack, player, world, clickPos, ffe);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private boolean isConnectionInOtherHand(Level world, Player player, ItemStack heldStack) {
        // TODO figure out what capabilities equate to in vanilla
        throw new NotImplementedException("ConnectionItem.isConnectionInOtherHand");
    }

    private void connect(ItemStack heldStack, Player player, Level world, BlockPos clickPos) {
        // TODO figure out what capabilities equate to in vanilla
        throw new NotImplementedException("ConnectionItem.connect");
    }

    private void connect(ItemStack heldStack, Player player, Level world, BlockPos pos, BlockState state) {
        // TODO figure out what capabilities equate to in vanilla
        throw new NotImplementedException("ConnectionItem.connect with BlockState");
    }

    public void connect(ItemStack heldStack, Player player, final Level world, final Fastener<?> fastener) {
        this.connect(heldStack, player, world, fastener, true);
    }

    public void connect(ItemStack heldStack, Player player, Level world, Fastener<?> fastener, boolean playConnectSound) {
        // TODO figure out what capabilities equate to in vanilla
        throw new NotImplementedException("ConnectionItem.connect with Fastener");
    }

    private void connectFence(ItemStack heldStack, Player player, Level world, BlockPos pos, FenceFastenerEntity entity) {
        // TODO figure out what capabilities equate to in vanilla
        throw new NotImplementedException("ConnectionItem.connectFence");
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
