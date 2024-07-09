package net.qsef.coolmodremastered.block.entity;

import com.mojang.logging.LogUtils;
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
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.recipe.IronFurnaceRecipe;
import net.qsef.coolmodremastered.recipe.ModRecipes;
import net.qsef.coolmodremastered.screen.IronFurnaceMenu;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class IronFurnaceBlockEntity extends BlockEntity implements MenuProvider, Container, Nameable, RecipeCraftingHolder, StackedContentsCompatible {
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private LazyOptional<?> itemHandler;

    // container progress data
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 100;

    // container items
    private NonNullList<ItemStack> items;
    private Component name;
    private final int CONTAINER_SIZE = 2;
    private final RecipeManager.CachedCheck<Container, ? extends IronFurnaceRecipe> quickCheckIronFurnace;
    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheckSmelting;

    public IronFurnaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.IronFurnace_BE.get(), pPos, pBlockState);

        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        this.itemHandler = LazyOptional.of(this::createUnSidedHandler);
        this.quickCheckIronFurnace = RecipeManager.createCheck(IronFurnaceRecipe.RECIPE_TYPE);
        this.quickCheckSmelting = RecipeManager.createCheck(RecipeType.SMELTING);

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
        return !this.remove && cap == ForgeCapabilities.ITEM_HANDLER ? itemHandler.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandler.invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.itemHandler = LazyOptional.of(this::createUnSidedHandler);
    }

    public void drops() {
//        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots()); // create a container with same amount of slots
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            inventory.setItem(i, itemHandler.getStackInSlot(i));
//        }
//
//        if (this.level != null) {
//            Containers.dropContents(this.level, this.worldPosition, inventory); // drop content of inventory at block position
//        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        // save data when game is saved
        pTag.putInt("iron_furnace.progress", progress);
        ContainerHelper.saveAllItems(pTag, this.items);

        if(this.name != null) {
            pTag.putString("CustomName", Component.Serializer.toJson(this.name));
        }

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        progress = pTag.getInt("iron_furnace.progress");

        this.items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.items);

        if(pTag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(pTag.getString("CustomName"));
        }
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(hasInputItem() && hasRecipe(pLevel)) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if(hasFinished()) {
                craftItem(pLevel);
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private boolean hasInputItem() {
        return this.items.get(INPUT_SLOT) != ItemStack.EMPTY;
    }

    private boolean hasRecipe(Level pLevel) {
        RecipeHolder recipeHolder = getCurrentRecipe(pLevel);
        if(recipeHolder == null) return false;

        ItemStack result = recipeHolder.value().getResultItem(null);
        return canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private void craftItem(Level pLevel) {
        RecipeHolder recipeHolder = getCurrentRecipe(pLevel);

        // remove 1 item from INPUT_SLOT
        this.items.get(INPUT_SLOT).shrink(1);

        // get existing count in OUTPUT_SLOT
        int existingCount = this.items.get(OUTPUT_SLOT).getCount();

        // get result item
        ItemStack result = recipeHolder.value().getResultItem(null);

        ItemStack newResultItemStack = new ItemStack(result.getItem(), existingCount + result.getCount());

        this.items.set(OUTPUT_SLOT, newResultItemStack);
    }

    private RecipeHolder getCurrentRecipe(Level pLevel) {
        // if no Iron Furnace recipes, return normal smelting recipe
        RecipeHolder ironFurnaceRecipe = this.quickCheckIronFurnace.getRecipeFor(this, pLevel).orElse(null);
        if(ironFurnaceRecipe == null) return this.quickCheckSmelting.getRecipeFor(this, pLevel).orElse(null);
        return ironFurnaceRecipe;
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        ItemStack stack = this.items.get(OUTPUT_SLOT);
        return stack.isEmpty() || stack.is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int amount) {
        ItemStack stack = this.items.get(OUTPUT_SLOT);
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

    public void setCustomName(Component pName) {
        this.name = pName;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new IronFurnaceMenu(i, inventory, this, this.data);
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : this.getDefaultName();
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }

    protected Component getDefaultName() {
        return Component.translatable("block.coolmodremastered.iron_furnace");
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int i) {
        return this.getItems().get(i);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        ItemStack stack = ContainerHelper.removeItem(this.getItems(), pIndex, pCount);
        if(!stack.isEmpty()) this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack stack = ContainerHelper.takeItem(this.getItems(), i);
        return stack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.getItems().set(i, itemStack);
        if(itemStack.getCount() > this.getMaxStackSize()) {
            itemStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
    }

    protected IItemHandler createUnSidedHandler() {
        return new InvWrapper(this);
    }

    @Override
    public void setRecipeUsed(@Nullable RecipeHolder<?> recipeHolder) {

    }

    @Nullable
    @Override
    public RecipeHolder<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void awardUsedRecipes(Player pPlayer, List<ItemStack> pItems) {
        RecipeCraftingHolder.super.awardUsedRecipes(pPlayer, pItems);
    }

    @Override
    public boolean setRecipeUsed(Level pLevel, ServerPlayer pPlayers, RecipeHolder<?> pRecipe) {
        return RecipeCraftingHolder.super.setRecipeUsed(pLevel, pPlayers, pRecipe);
    }

    @Override
    public void fillStackedContents(StackedContents stackedContents) {
        for (ItemStack itemstack : this.items) {
            stackedContents.accountStack(itemstack);
        }
    }
}
