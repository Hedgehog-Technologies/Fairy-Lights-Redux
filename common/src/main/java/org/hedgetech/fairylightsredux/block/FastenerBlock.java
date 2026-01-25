package org.hedgetech.fairylightsredux.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.hedgetech.fairylightsredux.connection.HangingLightsConnection;
import org.hedgetech.fairylightsredux.content.block.BlockDefs;
import org.hedgetech.fairylightsredux.block.entity.FastenerBlockEntity;
import org.hedgetech.fairylightsredux.fastener.Fastener;
import org.hedgetech.fairylightsredux.fastener.accessor.BlockFastenerAccessor;
import org.hedgetech.fairylightsredux.handler.ServerEventHandler;
import org.hedgetech.fairylightsredux.registry.FLRBlockEntities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class FastenerBlock extends DirectionalBlock implements EntityBlock {
    public static final MapCodec<FastenerBlock> CODEC = simpleCodec(FastenerBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private static final VoxelShape NORTH_AABB = Block.box(6.0D, 6.0D, 12.0D, 10.0D, 10.0D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 4.0D);
    private static final VoxelShape WEST_AABB = Block.box(12.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape EAST_AABB = Block.box(0.0D, 6.0D, 6.0D, 4.0D, 10.0D, 10.0D);
    private static final VoxelShape DOWN_AABB = Block.box(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape UP_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D);

    public FastenerBlock(final Properties props) {
        super(props);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TRIGGERED, false)
        );
    }

    @Override
    protected @NotNull MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @Override
    public @NotNull BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(final BlockState state, final Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    public @NotNull VoxelShape getShape(final BlockState state, final @NotNull BlockGetter worldIn, final @NotNull BlockPos pos, final @NotNull CollisionContext ctx) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_AABB;
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            case EAST -> EAST_AABB;
            case DOWN -> DOWN_AABB;
            default -> UP_AABB;
        };
    }

    @Override
    public BlockEntity newBlockEntity(final @NotNull BlockPos pos, final @NotNull BlockState state) {
        return new FastenerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level world, final @NotNull BlockState state, final @NotNull BlockEntityType<T> type) {
        if (world.isClientSide()) {
            return createTickerHelper(type, FLRBlockEntities.get(BlockDefs.FASTENER), FastenerBlockEntity::tickClient);
        }
        return createTickerHelper(type, FLRBlockEntities.get(BlockDefs.FASTENER), FastenerBlockEntity::tick);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker) {
        return expected == actual ? (BlockEntityTicker<A>) ticker : null;
    }

    // TODO Figure out if needed and what it changed to
//    @Override
//    public void onRemove(final BlockState state, final Level world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
////        if (!state.is(newState.getBlock())) {
////            final BlockEntity entity = world.getBlockEntity(pos);
////            if (entity instanceof FastenerBlockEntity) {
////                entity.getCapability(CapabilityHandler.FASTENER_CAP).ifPresent(f -> f.dropItems(world, pos));
////            }
////            super.onRemove(state, world, pos, newState, isMoving);
////        }
//    }

    @Override
    public boolean canSurvive(final BlockState state, final LevelReader world, final BlockPos pos) {
        final Direction facing = state.getValue(FACING);
        final BlockPos attachedPos = pos.relative(facing.getOpposite());
        final BlockState attachedState = world.getBlockState(attachedPos);
        return attachedState.is(BlockTags.LEAVES)
                || attachedState.isFaceSturdy(world, attachedPos, facing)
                || facing == Direction.UP && attachedState.is(BlockTags.WALLS);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext ctx) {
        BlockState result = this.defaultBlockState();
        final Level world = ctx.getLevel();
        final BlockPos pos = ctx.getClickedPos();
        for (final Direction dir : ctx.getNearestLookingDirections()) {
            result = result.setValue(FACING, dir.getOpposite());
            if (result.canSurvive(world, pos)) {
                return result.setValue(TRIGGERED, world.hasNeighborSignal(pos.relative(dir)));
            }
        }
        return null;
    }

    @Override
    public void neighborChanged(final BlockState state, final @NotNull Level world, final @NotNull BlockPos pos, final @NotNull Block blockIn, final Orientation orientation, final boolean movedByPiston) {
        if (state.canSurvive(world, pos)) {
            final boolean receivingPower = world.hasNeighborSignal(pos);
            final boolean isPowered = state.getValue(TRIGGERED);
            if (receivingPower && !isPowered) {
                world.scheduleTick(pos, this, 2);
                world.setBlock(pos, state.setValue(TRIGGERED, true), 4);
            } else if (!receivingPower && isPowered) {
                world.setBlock(pos, state.setValue(TRIGGERED, false), 4);
            }
        } else {
            final BlockEntity entity = world.getBlockEntity(pos);
            dropResources(state, world, pos, entity);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(final @NotNull BlockState state) {
        return true;
    }

    @Override
    public void tick(final @NotNull BlockState state, final @NotNull ServerLevel world, final @NotNull BlockPos pos, final @NotNull RandomSource random) {
        this.jingle(world, pos);
    }

    private void jingle(final Level world, final BlockPos pos) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof FastenerBlockEntity fbe) {
            Fastener<?> fastener = fbe.getFastener();
            fastener.getAllConnections().stream()
                    .filter(HangingLightsConnection.class::isInstance)
                    .map(HangingLightsConnection.class::cast)
                    .filter(conn -> conn.canCurrentlyPlayAJingle() && conn.isDestination(new BlockFastenerAccessor(fastener.getPos())) && world.getBlockState(fastener.getPos()).getValue(TRIGGERED))
                    .findFirst().ifPresent(conn -> ServerEventHandler.tryJingle(world, conn));
        }
    }

    public Vec3 getOffset(final Direction facing, final float offset) {
        return getFastenerOffset(facing, offset);
    }

    public static Vec3 getFastenerOffset(final Direction facing, final float offset) {
        double x = offset, y = offset, z = offset;
        switch (facing) {
            case DOWN:
                y += 0.75F;
            case UP:
                x += 0.375F;
                z += 0.375F;
                break;
            case WEST:
                x += 0.75F;
            case EAST:
                z += 0.375F;
                y += 0.375F;
                break;
            case NORTH:
                z += 0.75F;
            case SOUTH:
                x += 0.375F;
                y += 0.375F;
        }
        return new Vec3(x, y, z);
    }
}
