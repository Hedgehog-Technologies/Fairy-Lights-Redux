package org.hedgetech.fairylightsredux.server.block;

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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.block.entity.FastenerBlockEntity;
import org.hedgetech.fairylightsredux.server.capability.CapabilityHandler;
import org.hedgetech.fairylightsredux.server.connection.HangingLightConnection;
import org.hedgetech.fairylightsredux.server.fastener.accessor.BlockFastenerAccessor;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public final class FastenerBlock extends DirectionalBlock implements EntityBlock {
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private static final VoxelShape NORTH_AABB = Block.box(6.0D, 6.0D, 12.0D, 10.0D, 10.0D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 4.0D);
    private static final VoxelShape WEST_AABB = Block.box(12.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape EAST_AABB = Block.box(0.0D, 6.0D, 6.0D, 4.0D, 10.0D, 10.0D);
    private static final VoxelShape DOWN_AABB = Block.box(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape UP_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D);

    public FastenerBlock(final BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TRIGGERED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @Override
    public @NonNull BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NonNull BlockState mirror(final BlockState state, final Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    public @NonNull VoxelShape getShape(final BlockState state, final @NonNull BlockGetter worldIn, final @NonNull BlockPos pos, final @NonNull CollisionContext context) {
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
    public BlockEntity newBlockEntity(final @NonNull BlockPos pos, final @NonNull BlockState state) {
        return new FastenerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity>BlockEntityTicker<T> getTicker(final Level level, final @NonNull BlockState state, final @NonNull BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return createTickerHelper(type, FLRBlockEntities.FASTENER.get(), FastenerBlockEntity::tickClient);
        }
        return createTickerHelper(type, FLRBlockEntities.FASTENER.get(), FastenerBlockEntity::tick);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expect, BlockEntityTicker<? super E> ticker) {
        return expect == actual ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public void affectNeighborsAfterRemoval(final @NonNull BlockState state, final ServerLevel world, final @NonNull BlockPos pos, final boolean isMoving) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof FastenerBlockEntity) {
            entity.getCapability(CapabilityHandler.FASTENER_CAP).ifPresent(f -> f.dropItems(world, pos));
        }
        // @TODO - verify if this is needed
        super.affectNeighborsAfterRemoval(state, world, pos, isMoving);
    }

    @Override
    public boolean canSurvive(final BlockState state, final LevelReader world, final BlockPos pos) {
        final Direction facing = state.getValue(FACING);
        final BlockPos attachedPos = pos.relative(facing.getOpposite());
        final BlockState attachedState = world.getBlockState(attachedPos);
        return attachedState.is(BlockTags.LEAVES) || attachedState.isFaceSturdy(world, attachedPos, facing) || facing == Direction.UP && attachedState.is(BlockTags.WALLS);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext context) {
        BlockState result = this.defaultBlockState();
        final Level world = context.getLevel();
        final BlockPos pos = context.getClickedPos();
        for (final Direction dir : context.getNearestLookingDirections()) {
            result = result.setValue(FACING, dir.getOpposite());
            if (result.canSurvive(world, pos)) {
                return result.setValue(TRIGGERED, world.hasNeighborSignal(pos.relative(dir)));
            }
        }
        return null;
    }

    @Override
    public void neighborChanged(final BlockState state, final @NonNull Level world, final @NonNull BlockPos pos, final @NonNull Block blockIn, final Orientation orientation, final boolean isMoving) {
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
    public boolean hasAnalogOutputSignal(final @NonNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(final @NonNull BlockState state, final Level world, final @NonNull BlockPos pos) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity == null) return super.getAnalogOutputSignal(state, world, pos);
        return entity.getCapability(CapabilityHandler.FASTENER_CAP).map(f -> f.getAllConnections().stream()).orElse(Stream.empty())
                .filter(HangingLightConnection.class::isInstance)
                .map(HangingLightConnection.class::cast)
                .mapToInt(c -> (int) Math.ceil(c.getJingleProgress() * 15))
                .max().orElse(0);
    }

    @Override
    public void tick(final @NonNull BlockState state, final @NonNull ServerLevel world, final @NonNull BlockPos pos, final @NonNull RandomSource random) {
        this.jingle(world, pos);
    }

    public Vec3 getOffset(final Direction facing, final float offset) {
        return getFastenerOffset(facing, offset);
    }

    public static Vec3 getFastenerOffset(final Direction facing, final float offset) {
        double x = offset, y = offset, z = offset;
        switch (facing) {
            case DOWN: y += 0.75F;
            case UP: x += 0.375F; z += 0.375F; break;
            case WEST: x += 0.75F;
            case EAST: z += 0.375F; y += 0.375F; break;
            case NORTH: z += 0.75F;
            case SOUTH: x += 0.375F; y += 0.375F; break;
        }
        return new Vec3(x, y, z);
    }

    private void jingle(final Level world, final BlockPos pos) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (!(entity instanceof FastenerBlockEntity)) return;

        entity.getCapability(CapabilityHandler.FASTENER_CAP).ifPresent(fastener -> fastener.getAllConnections().stream()
                .filter(HangingLightConnection.class::isInstance)
                .map(HangingLightConnection.class::cast)
                .filter(conn -> conn.canCurrentlyPlayAJingle() && conn.isDestination(new BlockFastenerAccessor(fastener.getPos())) && world.getBlockState(fastener.getPos()).getValue(TRIGGERED))
                .findFirst().ifPresent(conn -> ServerEventHandler.tryJingle(world, conn))
        );
    }
}
