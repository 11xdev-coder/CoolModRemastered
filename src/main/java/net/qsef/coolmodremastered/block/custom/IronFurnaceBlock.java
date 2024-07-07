package net.qsef.coolmodremastered.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.qsef.coolmodremastered.block.entity.IronFurnaceBlockEntity;
import net.qsef.coolmodremastered.block.entity.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class IronFurnaceBlock extends BaseEntityBlock {
    public IronFurnaceBlock(Properties pProperties) {
        super(pProperties);
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
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

            if(blockEntity instanceof IronFurnaceBlockEntity) {
                pPlayer.openMenu((IronFurnaceBlockEntity) blockEntity);
            } else {
                throw new IllegalStateException("Container provider missing.");
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide) return null;

        return createTickerHelper(pBlockEntityType, ModBlockEntities.IronFurnace_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1)); // custom logic for each tick
    }
}
