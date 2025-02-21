package net.qsef.coolmodremastered.block.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.base.AbstractFurnaceBlockEntity;
import net.qsef.coolmodremastered.block.base.IHorizontalDirectionalBlock;
import net.qsef.coolmodremastered.recipe.IronFurnaceRecipe;
import net.qsef.coolmodremastered.recipe.ModRecipes;
import net.qsef.coolmodremastered.screen.IronFurnaceMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class IronFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int CONTAINER_SIZE = 2;

    public IronFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.IronFurnace_BE.get(), pPos, pBlockState, CONTAINER_SIZE, INPUT_SLOT, OUTPUT_SLOT, -1,
                List.of(RecipeManager.createCheck(IronFurnaceRecipe.RECIPE_TYPE), RecipeManager.createCheck(RecipeType.SMELTING)));
    }

    @Override
    protected boolean usesFuel() {
        return false;
    }

    @Override
    protected void saveData(CompoundTag pTag) {
        pTag.putInt("iron_furnace.cookTime", cookTime);
        ContainerHelper.saveAllItems(pTag, this.items);

        if(this.name != null) {
            pTag.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    @Override
    protected void loadData(CompoundTag pTag) {
        cookTime = pTag.getInt("iron_furnace.cookTime");

        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);

        if(pTag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(pTag.getString("CustomName"));
        }
    }

    private void spawnXp(Level pLevel, BlockPos pPos) {
        if(pLevel instanceof ServerLevel pServerLevel) {
            RecipeHolder recipeHolder = getCurrentRecipe(pLevel);
            if(recipeHolder == null) return;
            Recipe recipe = recipeHolder.value();

            // get xp from the recipe
            float xpAmount = 0;
            if(recipe instanceof IronFurnaceRecipe ifr) {
                xpAmount = ifr.getExperience();
            } else if (recipe instanceof AbstractCookingRecipe acr) {
                xpAmount = acr.getExperience();
            }

            int xpAmountInt = Mth.ceil(xpAmount);
            Vec3 dropPos = new Vec3(pPos.getX() + 0.5f, pPos.getY() + 1f, pPos.getZ() + 0.5f);
            ExperienceOrb.award(pServerLevel, dropPos, xpAmountInt);
        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.coolmodremastered.iron_furnace");
    }

    @Override
    protected AbstractContainerMenu getContainerMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new IronFurnaceMenu(pContainerId, pPlayerInventory, this, this.data);
    }
}
