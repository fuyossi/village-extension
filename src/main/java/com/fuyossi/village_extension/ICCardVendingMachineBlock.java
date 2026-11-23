package com.fuyossi.village_extension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class ICCardVendingMachineBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final int PHASES = 2;
    public static final IntegerProperty PHASE = IntegerProperty.create("phase", 0, PHASES - 1);

    // 20ティック = 1秒
    private static final int INTERVAL_TICKS = 100;

    public ICCardVendingMachineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
        registerDefaultState(defaultBlockState().setValue(PHASE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        builder.add(PHASE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // 設置した人のほうに正面を向ける
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        // 設置されたら最初のティックを予約する
        if (!level.isClientSide() && !oldState.is(this)) {
            level.scheduleTick(pos, this, INTERVAL_TICKS);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int next = (state.getValue(PHASE) + 1) % PHASES;
        // UPDATE_CLIENTS: クライアントへ送るだけで、隣接ブロックの更新は起こさない
        level.setBlock(pos, state.setValue(PHASE, next), Block.UPDATE_CLIENTS);
        // 次のティックを予約する
        level.scheduleTick(pos, this, INTERVAL_TICKS);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        Direction front = state.getValue(FACING);
        ItemEntity drop = new ItemEntity(
                level,
                pos.getX() + 0.5 + front.getStepX() * 0.7,
                pos.getY() + 0.5,
                pos.getZ() + 0.5 + front.getStepZ() * 0.7,
                new ItemStack(ModItems.IC_CARD));
        drop.setDeltaMovement(front.getStepX() * 0.1, 0.1, front.getStepZ() * 0.1);
        drop.setDefaultPickUpDelay();
        level.addFreshEntity(drop);

        level.playSound(null, pos, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0F, 1.0F);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}
