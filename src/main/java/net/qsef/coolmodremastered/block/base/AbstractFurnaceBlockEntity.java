package net.qsef.coolmodremastered.block.base;

import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.qsef.coolmodremastered.item.custom.IndustrialFuelItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractFurnaceBlockEntity extends BlockEntity implements MenuProvider, Container, Nameable, RecipeCraftingHolder, StackedContentsCompatible {
    protected int inputSlot, outputSlot, fuelSlot;

    protected NonNullList<ItemStack> items;
    protected LazyOptional<?> itemHandler;
    protected Component name;

    protected ContainerData data;
    protected int cookTime = 0;
    protected int cookTimeTotal = 200;
    protected int burnTime = 0;
    protected int maxBurnTime = 0;
    protected int containerSize;

    private RecipeHolder<?> lastUsedRecipe;
    protected List<RecipeManager.CachedCheck<Container, ?>> quickChecks;

    public AbstractFurnaceBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int containerSize,
                                      int inputSlot, int outputSlot, int fuelSlot, List<RecipeManager.CachedCheck<Container, ?>> quickChecks) {
        super(pType, pPos, pBlockState);

        this.inputSlot = inputSlot;
        this.outputSlot = outputSlot;
        this.fuelSlot = fuelSlot;

        this.containerSize = containerSize;
        this.items = NonNullList.withSize(containerSize, ItemStack.EMPTY);
        this.itemHandler = LazyOptional.of(this::createUnSidedHandler);

        // synchronize with GUI
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> cookTime;
                    case 1 -> cookTimeTotal;
                    case 2 -> burnTime;
                    case 3 -> maxBurnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> cookTime = pValue;
                    case 1 -> cookTimeTotal = pValue;
                    case 2 -> burnTime = pValue;
                    case 3 -> maxBurnTime = pValue;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        this.quickChecks = quickChecks;
    }

    protected abstract boolean usesFuel();
    protected abstract void saveData(CompoundTag pTag);
    protected abstract void loadData(CompoundTag pTag);
    protected abstract Component getDefaultName();
    protected abstract AbstractContainerMenu getContainerMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer);

    // wrap container items to get capability
    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        // if block is not removed, and capability is ITEMS -> return itemHandler
        // otherwise call super
        return !this.remove && cap == ForgeCapabilities.ITEM_HANDLER ? itemHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        itemHandler = LazyOptional.of(this::createUnSidedHandler);
    }

    public void drops() {
        if (this.level != null) {
            Containers.dropContents(this.level, this.worldPosition, items);
        }
    }

    @Override
    public int getContainerSize() {
        return containerSize;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        saveData(pTag);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);

        loadData(pTag);
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        return items.get(pSlot);
    }

    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        // remove specified amount
        ItemStack itemStack = ContainerHelper.removeItem(items, pSlot, pAmount);
        if (!itemStack.isEmpty()) this.setChanged(); // sync with GUI

        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        // remove whole slot
        return ContainerHelper.takeItem(items, pSlot);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        items.set(pSlot, pStack);
        // dont exceed Container max stack size (64)
        if (pStack.getCount() > getMaxStackSize()) pStack.setCount(getMaxStackSize());

        this.setChanged();
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public @NotNull Component getName() {
        return name != null ? name : getDefaultName();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getName();
    }

    public void setCustomName(Component pName) {
        name = pName;
    }

    @Override
    public @Nullable Component getCustomName() {
        return name;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return getContainerMenu(pContainerId, pPlayerInventory, pPlayer);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> pRecipe) {
        lastUsedRecipe = pRecipe;
    }

    @Override
    public @Nullable RecipeHolder<?> getRecipeUsed() {
        return lastUsedRecipe;
    }

    @Override
    public void awardUsedRecipes(@NotNull Player pPlayer, @NotNull List<ItemStack> pItems) {
        if (lastUsedRecipe != null && pPlayer instanceof ServerPlayer serverPlayer) {
            serverPlayer.awardRecipes(Set.of(lastUsedRecipe));
        }
    }

    @Override
    public void fillStackedContents(@NotNull StackedContents pContents) {
        for (ItemStack itemStack : items) {
            pContents.accountStack(itemStack);
        }
    }

    protected boolean hasInputItem() {
        return !items.get(inputSlot).isEmpty();
    }

    protected RecipeHolder getCurrentRecipe(Level pLevel) {
        for (RecipeManager.CachedCheck<Container, ?> quickCheck : quickChecks) {
            Optional<? extends RecipeHolder<?>> optionalRecipe = quickCheck.getRecipeFor(this, pLevel);
            if (optionalRecipe.isPresent()) {
                return optionalRecipe.get();
            }
        }

        return null;
    }

    protected boolean canCraft(Level pLevel) {
        RecipeHolder currentRecipe = getCurrentRecipe(pLevel);
        if (currentRecipe == null) return false;

        ItemStack result = currentRecipe.value().getResultItem(null);
        return canInsertIntoOutput(result);
    }

    protected void craftItem(Level pLevel) {
        RecipeHolder currentRecipe = getCurrentRecipe(pLevel);
        if (currentRecipe == null) return;

        items.get(inputSlot).shrink(1);
        int existingCount = items.get(outputSlot).getCount();

        ItemStack recipeResult = currentRecipe.value().getResultItem(null);
        ItemStack newOutput = new ItemStack(recipeResult.getItem(), existingCount + recipeResult.getCount());
        items.set(outputSlot, newOutput);
    }

    protected boolean canInsertIntoOutput(ItemStack pItemStack) {
        ItemStack output = items.get(outputSlot);
        Item item = pItemStack.getItem();
        int amount = pItemStack.getCount();

        return (output.isEmpty() || output.is(item)) && output.getCount() + amount < output.getMaxStackSize();
    }

    protected void increaseCraftingProgress() {
        cookTime++;
    }

    protected void resetProgress() {
        cookTime = 0;
    }

    protected boolean hasFinished() {
        return cookTime >= cookTimeTotal;
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        boolean changed = false;

        if (usesFuel()) {
            if (burnTime > 0) {
                burnTime -= 1;
            }

            // start new fuel when out of fuel
            if (burnTime <= 0 && hasInputItem() && canCraft(pLevel)) {
                ItemStack fuelItem = items.get(fuelSlot);
                int fuel = getFuelBurnTime(fuelItem);
                maxBurnTime = fuel;

                // if valid fuel
                if (fuel > 0) {
                    burnTime = fuel;
                    fuelItem.shrink(1);
                    changed = true;
                }
            }
        }

        if (hasInputItem() && canCraft(pLevel)) {
            if (!usesFuel() || burnTime > 0) {
                increaseCraftingProgress();
                changed = true;

                if (hasFinished()) {
                    craftItem(pLevel);
                    resetProgress();
                }
            } else {
                resetProgress();
                changed = true;
            }
        } else {
            resetProgress();
        }

        if (changed) {
            setChanged(pLevel, pPos, pState);
        }
    }

    private int getFuelBurnTime(ItemStack fuel) {
        if (fuel.getItem() instanceof IndustrialFuelItem industrialFuel) {
            return industrialFuel.getIndustrialBurnTime();
        }

        return 0;
    }
}
