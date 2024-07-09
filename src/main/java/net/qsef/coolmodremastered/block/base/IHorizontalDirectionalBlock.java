package net.qsef.coolmodremastered.block.base;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IHorizontalDirectionalBlock {
    DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    default BlockState IGetStateForPlacement(Block block, BlockPlaceContext context) {
        Direction dir = context.getHorizontalDirection();
        return block.defaultBlockState().setValue(FACING, dir);
    }

    default void ICreateBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    default BlockState IRotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    default BlockState IMirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    default void IRegisterDefaultState(Consumer registerFunc, StateDefinition<Block, BlockState> stateDefinition) {
        registerFunc.accept(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
}
