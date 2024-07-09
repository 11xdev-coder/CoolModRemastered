package net.qsef.coolmodremastered.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.qsef.coolmodremastered.block.base.IHorizontalDirectionalBlock;
import net.qsef.coolmodremastered.block.entity.IronFurnaceBlockEntity;
import net.qsef.coolmodremastered.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class IronFurnaceBlock extends BaseEntityBlock implements IHorizontalDirectionalBlock {
    public IronFurnaceBlock(Properties pProperties) {
        super(pProperties);
        IRegisterDefaultState(pState -> registerDefaultState((BlockState) pState), this.stateDefinition);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new IronFurnaceBlockEntity(blockPos, blockState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(pState != pNewState) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

            if(blockEntity instanceof IronFurnaceBlockEntity) {
                ((IronFurnaceBlockEntity) blockEntity).drops(); // if the state is changed, and BlockEntity is IronFurnace, drop contents
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide) {

            MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            pPlayer.openMenu(menuprovider);
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide) return null;

        return createTickerHelper(pBlockEntityType, ModBlockEntities.IronFurnace_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1)); // custom logic for each tick
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        //  set  custom name when placed
        if (pStack.hasCustomHoverName()) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof IronFurnaceBlockEntity) {
                ((IronFurnaceBlockEntity)blockentity).setCustomName(pStack.getHoverName());
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return IGetStateForPlacement(this, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        ICreateBlockStateDefinition(builder);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return IRotate(state, rotation);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return IMirror(state, mirror);
    }
}
