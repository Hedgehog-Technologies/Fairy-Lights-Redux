package org.hedgetech.fairylightsredux.server.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import org.hedgetech.fairylightsredux.server.fastener.Fastener;
import org.hedgetech.fairylightsredux.server.block.entity.FLRBlockEntities;
import org.hedgetech.fairylightsredux.server.block.entity.FastenerBlockEntity;
import org.hedgetech.fairylightsredux.server.connection.HangingLightsConnection;
import org.hedgetech.fairylightsredux.server.sound.FLRSounds;
import org.jetbrains.annotations.NotNull;

public final class FastenerBlock extends DirectionalBlock implements EntityBlock {
    public static final MapCodec<FastenerBlock> CODEC = simpleCodec(FastenerBlock::new);
    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    private static final VoxelShape NORTH_AABB = Block.box(6.0D, 6.0D, 12.0D, 10.0D, 10.0D, 16.0D);
    private static final VoxelShape SOUTH_AABB = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 4.0D);
    private static final VoxelShape WEST_AABB = Block.box(12.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
    private static final VoxelShape EAST_AABB = Block.box(0.0D, 6.0D, 6.0D, 4.0D, 10.0D, 10.0D);
    private static final VoxelShape DOWN_AABB = Block.box(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D);
    private static final VoxelShape UP_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 4.0D, 10.0D);

    public FastenerBlock(final Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(TRIGGERED, false)
        );
    }

    @Override
    protected @NotNull MapCodec<FastenerBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TRIGGERED);
    }

    @Override
    protected @NotNull BlockState rotate(final BlockState state, final Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(final BlockState state, final Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    protected @NotNull VoxelShape getShape(final BlockState state, final @NotNull BlockGetter worldIn, final @NotNull BlockPos pos, final @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_AABB;
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            case EAST -> EAST_AABB;
            case DOWN -> DOWN_AABB;
            case UP -> UP_AABB;
        };
    }

    @Override
    public BlockEntity newBlockEntity(final @NotNull BlockPos pos, final @NotNull BlockState state) {
        return new FastenerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level world, final @NotNull BlockState state, final @NotNull BlockEntityType<T> type) {
        if (world.isClientSide()) {
            return createTickerHelper(type, FLRBlockEntities.FASTENER, FastenerBlockEntity::tickClient);
        }

        return createTickerHelper(type, FLRBlockEntities.FASTENER, FastenerBlockEntity::tick);
    }

    @SuppressWarnings("unchecked")
    private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(final BlockEntityType<A> actual, final BlockEntityType<E> expect, final BlockEntityTicker<? super E> ticker) {
        return expect == actual
                ? (BlockEntityTicker<A>) ticker
                : null;
    }

    @Override
    protected boolean canSurvive(final BlockState state, final LevelReader world, final BlockPos pos) {
        final Direction facing = state.getValue(FACING);
        final BlockPos attachedPos = pos.relative(facing.getOpposite());
        final BlockState attachedState = world.getBlockState(attachedPos);
        return attachedState.is(BlockTags.LEAVES) || attachedState.isFaceSturdy(world, attachedPos, facing)
                || facing == Direction.UP && attachedState.is(BlockTags.WALLS);
    }

    @Override
    protected void neighborChanged(final BlockState state, final @NotNull Level world, final @NotNull BlockPos pos, final @NotNull Block neighborBlock, final Orientation orientation, final boolean movedByPiston) {
        if (state.canSurvive(world, pos)) {
            final boolean receivingPower = world.hasNeighborSignal(pos);
            final boolean isPowered = state.getValue(TRIGGERED);

            if (receivingPower && !isPowered) {
                world.scheduleTick(pos, this, 2);
            }

            if (receivingPower != isPowered) {
                world.setBlock(pos, state.setValue(TRIGGERED, receivingPower), Block.UPDATE_INVISIBLE);
            }
        } else {
            dropResources(state, world, pos, world.getBlockEntity(pos));
            world.removeBlock(pos, false);
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(final @NotNull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(final @NotNull BlockState state, final Level world, final @NotNull BlockPos pos) {
        if (!(world.getBlockEntity(pos) instanceof final FastenerBlockEntity entity)) return 0;

        return entity.getFastener().getAllConnections().stream()
                .filter(HangingLightsConnection.class::isInstance)
                .mapToInt(connection -> Mth.ceil(((HangingLightsConnection) connection).getJingleProgress() * 15))
                .max().orElse(0);
    }

    @Override
    protected void tick(final BlockState state, final @NotNull ServerLevel world, final @NotNull BlockPos pos, final @NotNull RandomSource random) {
        if (!state.getValue(TRIGGERED) || !(world.getBlockEntity(pos) instanceof final FastenerBlockEntity entity)) return;

        final Fastener<?> fastener = entity.getFastener();
        fastener.getAllConnections().stream()
                .filter(HangingLightsConnection.class::isInstance)
                .map(HangingLightsConnection.class::cast)
                .filter(connection -> connection.canCurrentlyPlayAJingle() && connection.isDestination(fastener.createAccessor()))
                .findFirst()
                .ifPresent(HangingLightsConnection::tryJingle);
    }

    public static boolean ringNoteBlockBell(final Level world, final BlockPos pos, final BlockState noteBlock, final int note) {
        final BlockState below = world.getBlockState(pos.below());
        if (!below.is(FLRBlocks.FASTENER) || below.getValue(FACING) != Direction.DOWN) return false;

        if (world instanceof final ServerLevel level) {
            level.playSound(null, pos, FLRSounds.JINGLE_BELL, SoundSource.RECORDS, 3.0F, NoteBlock.getPitchFromNote(note));
            level.getServer().getPlayerList().broadcast(null, pos.getX(), pos.getY(), pos.getZ(), 64, level.dimension(), new ClientboundBlockEventPacket(pos, noteBlock.getBlock(), 0, 0));
        } else {
            world.addParticle(ParticleTypes.NOTE, pos.getX() + 0.5D, pos.getY() + 1.2D, pos.getZ() + 0.5D, note / 24.0D, 0.0D, 0.0D);
        }

        return true;
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
                y += 0.375F;
                z += 0.375F;
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
