package net.qsef.coolmodremastered.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.Level;
import net.qsef.coolmodremastered.screen.base.AbstractContainerMenu;

public class IronFurnaceMenu extends AbstractContainerMenu {
    private static final int CONTAINER_SIZE = 2;
    private final ContainerData data;

    private final static int INPUT_SLOT = 0;
    private final static int OUTPUT_SLOT = 1;

    public IronFurnaceMenu(int pContainerId, Inventory inv) {
        this(pContainerId, inv, new SimpleContainer(CONTAINER_SIZE), new SimpleContainerData(CONTAINER_SIZE));
    }

    public IronFurnaceMenu(int pContainerId, Inventory inv, FriendlyByteBuf buf) {
        this(pContainerId, inv, new SimpleContainer(CONTAINER_SIZE), new SimpleContainerData(CONTAINER_SIZE));
    }

    public IronFurnaceMenu(int pContainerId, Inventory inv, Container pContainer, ContainerData data) {
        super(ModMenuTypes.IronFurnaceMenu.get(), pContainerId, CONTAINER_SIZE, pContainer);

        checkContainerSize(inv, CONTAINER_SIZE);
        this.data = data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // add actual slots for our Container
        this.addSlot(new Slot(pContainer, INPUT_SLOT, 80, 11));
        this.addSlot(new Slot(pContainer, OUTPUT_SLOT, 80, 59));

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0; // if progress is more than 0
    }

    public int getScaledProgress() {
        int progress = data.get(0);
        int maxProgress = data.get(1);
        int arrowSize = 26;

        if (progress != 0 && maxProgress != 0) {
            return progress * arrowSize / maxProgress;
        }
        return 0;
    }
}

