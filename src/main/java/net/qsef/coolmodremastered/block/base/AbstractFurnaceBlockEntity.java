package net.qsef.coolmodremastered.block.base;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
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
import net.qsef.coolmodremastered.block.entity.IndustrialFurnaceBlockEntity;
import net.qsef.coolmodremastered.item.custom.IndustrialFuelItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractFurnaceBlockEntity extends BlockEntity implements MenuProvider, Container, Nameable, RecipeHolder, StackedContentsCompatible {
    protected int inputSlot, outputSlot, fuelSlot;

    protected NonNullList<ItemStack> items;
    protected LazyOptional<?> itemHandler;
    protected Component name;

    protected ContainerData data;
    protected int cookTime = 0;
    protected int cookTimeTotal = 200; // Default vanilla cook time
    protected int burnTime = 0;
    protected int maxBurnTime = 0;
    protected int containerSize;

    private Recipe<?> lastUsedRecipe;
    protected List<RecipeManager.CachedCheck<Container, ? extends Recipe<Container>>> quickChecks;


    public AbstractFurnaceBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int containerSize,
                                      int inputSlot, int outputSlot, int fuelSlot,
                                      List<RecipeManager.CachedCheck<Container, ? extends Recipe<Container>>> quickChecks) {
        super(pType, pPos, pBlockState);

        this.inputSlot = inputSlot;
        this.outputSlot = outputSlot;
        this.fuelSlot = fuelSlot;

        this.containerSize = containerSize;
        this.items = NonNullList.withSize(containerSize, ItemStack.EMPTY);
        this.itemHandler = LazyOptional.of(this::createUnSidedHandler);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> AbstractFurnaceBlockEntity.this.cookTime;
                    case 1 -> AbstractFurnaceBlockEntity.this.cookTimeTotal;
                    case 2 -> AbstractFurnaceBlockEntity.this.burnTime;
                    case 3 -> AbstractFurnaceBlockEntity.this.maxBurnTime;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> AbstractFurnaceBlockEntity.this.cookTime = pValue;
                    case 1 -> AbstractFurnaceBlockEntity.this.cookTimeTotal = pValue;
                    case 2 -> AbstractFurnaceBlockEntity.this.burnTime = pValue;
                    case 3 -> AbstractFurnaceBlockEntity.this.maxBurnTime = pValue;
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

    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
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
        ItemStack itemStack = ContainerHelper.removeItem(items, pSlot, pAmount);
        if (!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        return ContainerHelper.takeItem(items, pSlot);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        items.set(pSlot, pStack);
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
    public void setRecipeUsed(@Nullable Recipe<?> pRecipe) {
        lastUsedRecipe = pRecipe;
    }

    @Override
    @Nullable
    public Recipe<?> getRecipeUsed() {
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

    protected Optional<? extends Recipe<Container>> getCurrentRecipe(Level pLevel) {
        if (this.level == null) return Optional.empty(); // Should not happen if called from tick
        for (RecipeManager.CachedCheck<Container, ? extends Recipe<Container>> quickCheck : quickChecks) {
            // getRecipeFor should return Optional<T> where T is ? extends Recipe<Container>
            Optional<? extends Recipe<Container>> optionalRecipe = quickCheck.getRecipeFor(this, this.level);
            if (optionalRecipe.isPresent()) {
                return optionalRecipe;
            }
        }
        return Optional.empty();
    }

    protected boolean canCraft(Level pLevel) {
        Optional<? extends Recipe<Container>> currentRecipeOpt = getCurrentRecipe(pLevel);
        if (currentRecipeOpt.isEmpty()) {
            return false;
        }
        Recipe<Container> recipe = currentRecipeOpt.get();
        ItemStack result = recipe.getResultItem(pLevel.registryAccess());
        if (result.isEmpty()) {
            return false;
        }
        return canInsertIntoOutput(result);
    }

    protected void craftItem(Level pLevel) {
        Optional<? extends Recipe<Container>> currentRecipeOpt = getCurrentRecipe(pLevel);
        if (currentRecipeOpt.isEmpty() || this.level == null) {
            return;
        }
        Recipe<Container> recipe = currentRecipeOpt.get();

        items.get(inputSlot).shrink(1);
        int existingCount = items.get(outputSlot).getCount();

        ItemStack recipeResult = recipe.assemble(this, this.level.registryAccess());

        ItemStack newOutput = new ItemStack(recipeResult.getItem(), existingCount + recipeResult.getCount(), recipeResult.getTag());
        items.set(outputSlot, newOutput);
    }

    protected boolean canInsertIntoOutput(ItemStack pItemStack) {
        ItemStack output = items.get(outputSlot);
        int amount = pItemStack.getCount();

        if (output.isEmpty()) {
            return true;
        }
        // Check if items are the same (including NBT for stackable items) and if there's space.
        if (!ItemStack.isSameItemSameTags(output, pItemStack)) { // More robust check
            return false;
        }
        // Ensure we don't exceed the item's own max stack size or the slot's max stack size (usually 64 from Container)
        return output.getCount() + amount <= output.getMaxStackSize() && output.getCount() + amount <= getMaxStackSize();
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
                changed = true; // Burn time changing means state changed
            }

            if (burnTime <= 0 && hasInputItem() && canCraft(pLevel)) {
                ItemStack fuelItem = items.get(fuelSlot);
                int calculatedBurnTime = getFuelBurnTime(fuelItem);

                if (calculatedBurnTime > 0) {
                    burnTime = calculatedBurnTime;
                    maxBurnTime = calculatedBurnTime; // Set maxBurnTime when new fuel is consumed
                    if (!fuelItem.getItem().hasCraftingRemainingItem()) { // Check for items like Lava Bucket
                        fuelItem.shrink(1);
                    } else {
                        items.set(fuelSlot, fuelItem.getItem().getCraftingRemainingItem(fuelItem.copy()));
                    }
                    changed = true;
                }
            }
        }

        if (hasInputItem() && canCraft(pLevel)) {
            if (!usesFuel() || burnTime > 0) {
                if (!usesFuel() && burnTime <= 0) { // If not using fuel, but somehow burnTime was positive, reset it for logic
                    // This case might not be strictly necessary if usesFuel() is false, burnTime should remain 0
                }
                increaseCraftingProgress();
                if(!usesFuel()){ // if not using fuel, also decrement burnTime so it doesn't get stuck if it was > 0
                    // This ensures that if it's a non-fuel furnace, progress happens without fuel consumption logic interference
                }
                changed = true;

                if (hasFinished()) {
                    craftItem(pLevel);
                    resetProgress();
                    changed = true; // Crafting also changes state
                }
            } else { // Not enough burnTime (and usesFuel() is true)
                resetProgress();
                // No change to 'changed' here unless progress was ongoing
            }
        } else { // No input or cannot craft
            if(cookTime > 0) { // If it was cooking, but can no longer craft/has no input
                resetProgress();
                changed = true; // Progress resetting is a change
            }
        }

        if (changed) {
            setChanged(pLevel, pPos, pState);
        }
    }

    private int getFuelBurnTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        }
        // for industrial use only industrial fuel
        if (this instanceof IndustrialFurnaceBlockEntity) {
            if (fuel.getItem() instanceof IndustrialFuelItem industrialFuelItem)
                return industrialFuelItem.getIndustrialBurnTime();
            return 0;
        }
        return net.minecraftforge.common.ForgeHooks.getBurnTime(fuel, null); // RecipeType can be null for general fuel
    }
}
