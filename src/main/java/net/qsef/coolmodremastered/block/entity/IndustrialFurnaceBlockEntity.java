package net.qsef.coolmodremastered.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.block.base.AbstractFurnaceBlockEntity;
import net.qsef.coolmodremastered.recipe.IndustrialFurnaceRecipe;
import net.qsef.coolmodremastered.recipe.ModRecipes;
import net.qsef.coolmodremastered.screen.IndustrialFurnaceMenu;
import net.qsef.coolmodremastered.screen.IronFurnaceMenu;

import java.util.List;

public class IndustrialFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static final int CONTAINER_SIZE = 3;
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int FUEL_SLOT = 2;

    public IndustrialFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.IndustrialFurnace_BE.get(), pPos, pBlockState, CONTAINER_SIZE, INPUT_SLOT, OUTPUT_SLOT, FUEL_SLOT,
                List.of(RecipeManager.createCheck(IndustrialFurnaceRecipe.RECIPE_TYPE)));
    }

    @Override
    protected boolean usesFuel() {
        return true;
    }

    @Override
    protected void saveData(CompoundTag pTag) {
        pTag.putInt("industrial_furnace.cookTime", cookTime);
        pTag.putInt("industrial_furnace.burnTime", burnTime);
        pTag.putInt("industrial_furnace.maxBurnTime", maxBurnTime);
        ContainerHelper.saveAllItems(pTag, items);

        if (name != null) {
            pTag.putString("CustomName", Component.Serializer.toJson(name));
        }
    }

    @Override
    protected void loadData(CompoundTag pTag) {
        cookTime = pTag.getInt("industrial_furnace.cookTime");
        burnTime = pTag.getInt("industrial_furnace.burnTime");
        maxBurnTime = pTag.getInt("industrial_furnace.maxBurnTime");
        items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, items);

        if (pTag.contains("CustomName", 8)) {
            name = Component.Serializer.fromJson(pTag.getString("CustomName"));
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.coolmodremastered.industrial_furnace");
    }

    @Override
    protected AbstractContainerMenu getContainerMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new IndustrialFurnaceMenu(pContainerId, pPlayerInventory, this, data);
    }
}
