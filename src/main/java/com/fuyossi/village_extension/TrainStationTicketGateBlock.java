package com.fuyossi.village_extension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrainStationTicketGateBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    // 通行方向が南北(Z軸)のとき
    private static final VoxelShape OUTLINE_POSTS_Z = Shapes.or(
            Block.box(0, 0, 0, 3, 12, 16),
            Block.box(13, 0, 0, 16, 12, 16));
    private static final VoxelShape OUTLINE_CLOSED_Z = Shapes.or(
            OUTLINE_POSTS_Z,
            Block.box(0, 0, 7, 16, 8, 9));

    // 通行方向が東西(X軸)のとき
    private static final VoxelShape OUTLINE_POSTS_X = Shapes.or(
            Block.box(0, 0, 0, 16, 12, 3),
            Block.box(0, 0, 13, 16, 12, 16));
    private static final VoxelShape OUTLINE_CLOSED_X = Shapes.or(
            OUTLINE_POSTS_X,
            Block.box(7, 0, 0, 9, 8, 16));

    private static final VoxelShape COLLISION_CLOSED_Z = Block.box(0, 0, 7, 16, 24, 9);
    private static final VoxelShape COLLISION_CLOSED_X = Block.box(7, 0, 0, 9, 24, 16);

    public TrainStationTicketGateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(OPEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection())
                .setValue(OPEN, false);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!player.getAbilities().mayBuild) {
            return InteractionResult.PASS;
        }

        boolean open = !state.getValue(OPEN);
        level.setBlockAndUpdate(pos, state.setValue(OPEN, open));
        level.playSound(player, pos,
                open ? SoundEvents.FENCE_GATE_OPEN : SoundEvents.FENCE_GATE_CLOSE,
                SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean alongZ = state.getValue(FACING).getAxis() == Direction.Axis.Z;
        boolean open = state.getValue(OPEN);
        if (alongZ) {
            return open ? OUTLINE_POSTS_Z : OUTLINE_CLOSED_Z;
        }
        return open ? OUTLINE_POSTS_X : OUTLINE_CLOSED_X;
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(OPEN)) {
            return Shapes.empty(); // 開いている時は完全に通れる
        }
        boolean alongZ = state.getValue(FACING).getAxis() == Direction.Axis.Z;
        return alongZ ? COLLISION_CLOSED_Z : COLLISION_CLOSED_X;
    }

    // 村の構造物(structure)に含めた時に正しく回転・反転させるため
    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
