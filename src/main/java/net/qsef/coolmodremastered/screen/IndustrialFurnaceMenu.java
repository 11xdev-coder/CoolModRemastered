package net.qsef.coolmodremastered.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.qsef.coolmodremastered.screen.base.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class IndustrialFurnaceMenu extends AbstractContainerMenu {
    private static final int CONTAINER_SIZE = 3;
    private final ContainerData data;

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int FUEL_SLOT = 2;

    public IndustrialFurnaceMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, new SimpleContainer(3), new SimpleContainerData(4));
    }

    public IndustrialFurnaceMenu(int pContainerId, Inventory inv, Container pContainer, ContainerData data) {
        super(ModMenuTypes.IndustrialFurnaceMenu.get(), pContainerId, CONTAINER_SIZE, pContainer);

        checkContainerSize(pContainer, CONTAINER_SIZE);
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.addSlot(new Slot(pContainer, INPUT_SLOT, 56, 17));
        this.addSlot(new Slot(pContainer, OUTPUT_SLOT, 116, 35));
        this.addSlot(new Slot(pContainer, FUEL_SLOT, 56, 53));

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0; // if progress is more than 0
    }

    public int getCookProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        return (maxProgress != 0) ? (progress * 24) / maxProgress : 0;
    }

    public int getBurnProgress() {
        int currentBurn = data.get(2);
        int maxBurn = data.get(3);
        return (maxBurn != 0) ? (currentBurn * 13) / maxBurn : 0;
    }

    public boolean isBurning() {
        return data.get(2) > 0;
    }
}
