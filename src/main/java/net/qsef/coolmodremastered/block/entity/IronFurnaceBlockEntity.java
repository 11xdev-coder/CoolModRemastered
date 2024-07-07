package net.qsef.coolmodremastered.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.qsef.coolmodremastered.recipe.IronFurnaceRecipe;
import net.qsef.coolmodremastered.screen.IronFurnaceMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class IronFurnaceBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2); // inventory of our block entity

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    public IronFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.IronFurnace_BE.get() ,pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    // sync progress
                    case 0 -> IronFurnaceBlockEntity.this.progress;
                    case 1 -> IronFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                // sync these 2 integers
                switch (i) {
                    case 0 -> IronFurnaceBlockEntity.this.progress = i1;
                    case 1 -> IronFurnaceBlockEntity.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                // count of syncs
                return 2;
            }
        };
    }

    // lazy optionals are for checking capabilities
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots()); // create a container with same amount of slots
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, inventory); // drop content of inventory at block position
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.coolmodremastered.iron_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new IronFurnaceMenu(containerId, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        // save data when game is saved
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("iron_furnace.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("iron_furnace.progress");
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(hasRecipe()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if(hasFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem() {
        Optional<RecipeHolder<IronFurnaceRecipe>> recipe = getCurrentRecipe();
        ItemStack result = recipe.get().value().getResultItem(null);

        // remove 1 item from INPUT_SLOT
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        // add result count into existing count in OUTPUT_SLOT
        this.itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()), false);
    }

    private boolean hasRecipe() {
        Optional<RecipeHolder<IronFurnaceRecipe>> recipe = getCurrentRecipe();

        if(recipe.isEmpty()) return false;
        ItemStack result = recipe.get().value().getResultItem(null);

        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private Optional<RecipeHolder<IronFurnaceRecipe>> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i)); // copy over all itemHandler slots into the SimpleContainer
        }

        return this.level.getRecipeManager().getRecipeFor(IronFurnaceRecipe.RECIPE_TYPE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        ItemStack stack = this.itemHandler.getStackInSlot(OUTPUT_SLOT);
        return stack.isEmpty() || stack.is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int amount) {
        ItemStack stack = this.itemHandler.getStackInSlot(OUTPUT_SLOT);
        return stack.getCount() + amount <= stack.getMaxStackSize();
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean hasFinished() {
        return progress >= maxProgress;
    }
}
