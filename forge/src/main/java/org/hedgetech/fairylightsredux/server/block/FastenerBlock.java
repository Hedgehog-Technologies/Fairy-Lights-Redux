package org.hedgetech.fairylightsredux.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.AABB;

import org.hedgetech.fairylightsredux.server.ServerEventHandler;
import org.hedgetech.fairylightsredux.server.connection.HangingLightsConnection; // may be unresolved; placeholder
import org.hedgetech.fairylightsredux.components.Fasteners;
import org.hedgetech.fairylightsredux.components.FastenerComponent;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Partial port of the original FastenerBlock that uses the FastenerComponent helper.
 * This is a pragmatic, incremental port: it implements the key server-side behaviors
 * that interacted with the capability in the original code and now delegate to the
 * new component API via Fasteners.getOrCreate(...).
 */
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
    @SuppressWarnings("deprecation")
    public BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(final BlockState state, final Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(final BlockState state, final BlockGetter worldIn, final BlockPos pos, final CollisionContext context) {
        switch (state.getValue(FACING)) {
            case NORTH:
                return NORTH_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case WEST:
                return WEST_AABB;
            case EAST:
                return EAST_AABB;
            case DOWN:
                return DOWN_AABB;
            case UP:
            default:
                return UP_AABB;
        }
    }

    @Override
    public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
        // BlockEntity port not implemented yet; return null until the block entity is ported.
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level level, final BlockState state, final BlockEntityType<T> type) {
        // No ticking until block entity is ported
        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(final BlockState state, final Level world, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity != null) {
                final FastenerComponent comp = Fasteners.getOrCreate(entity);
                if (comp != null) comp.dropItems(world, pos);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
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
    @SuppressWarnings("deprecation")
    public void neighborChanged(final BlockState state, final Level world, final BlockPos pos, final Block blockIn, final BlockPos fromPos, final boolean isMoving) {
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
    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(final BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(final BlockState state, final Level world, final BlockPos pos) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity == null) return super.getAnalogOutputSignal(state, world, pos);
        final FastenerComponent comp = Fasteners.getOrCreate(entity);
        if (comp == null) return super.getAnalogOutputSignal(state, world, pos);
        return comp.getAllConnections().stream()
            .filter(HangingLightsConnection.class::isInstance)
            .map(HangingLightsConnection.class::cast)
            .mapToInt(c -> (int) Math.ceil(c.getJingleProgress() * 15))
            .max().orElse(0);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void tick(final BlockState state, final ServerLevel world, final BlockPos pos, final Random random) {
        this.jingle(world, pos);
    }

    private void jingle(final Level world, final BlockPos pos) {
        final BlockEntity entity = world.getBlockEntity(pos);
        if (entity == null) return;
        final FastenerComponent comp = Fasteners.getOrCreate(entity);
        if (comp == null) return;

        comp.getAllConnections().stream()
            .filter(HangingLightsConnection.class::isInstance)
            .map(HangingLightsConnection.class::cast)
            .filter(conn -> {
                // Best-effort predicate; many parts of this depend on legacy accessors — keep simple for now
                return true;
            })
            .findFirst().ifPresent(conn -> ServerEventHandler.tryJingle(world, (HangingLightsConnection) conn));
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
