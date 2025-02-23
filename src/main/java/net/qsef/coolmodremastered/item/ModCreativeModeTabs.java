package net.qsef.coolmodremastered.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB,
            CoolModRemastered.MOD_ID);

    // create a new tab using CreativeModeTab.builder()
    public static final RegistryObject<CreativeModeTab> COOL_TAB = CREATIVE_MODE_TABS.register("cool_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.Bazooka.get())) // new itemstack of our item, get() is used for modded items
                    .title(Component.translatable("creativetab.cool_tab")) // title, later translated in en_us.json
                    .displayItems((itemDisplayParameters, output) -> {
                        // all new items should be listed in here
                        output.accept(ModItems.SteelIngot.get());
                        output.accept(ModItems.Carbon.get());
                        output.accept(ModItems.CompressedCharcoal.get());
                        output.accept(ModItems.Bazooka.get());
                        output.accept(ModBlocks.IndustrialFurnace.get());

                        output.accept(ModItems.SteelHelmet.get());
                        output.accept(ModItems.SteelChestplate.get());
                        output.accept(ModItems.SteelLeggings.get());
                        output.accept(ModItems.SteelBoots.get());

                        output.accept(ModItems.Porkchopyonite.get());
                        output.accept(ModItems.PigRelic.get());
                        output.accept(ModItems.PigSoul.get());
                        output.accept(ModItems.PorkBurger.get());
                        output.accept(ModItems.RoastedPorkchop.get());
                        output.accept(ModItems.EmeraldStaff.get());
                        output.accept(ModItems.PorkchopyoniteSword.get());
                        output.accept(ModItems.PorkchopyonitePickaxe.get());
                        output.accept(ModItems.PorkchopyoniteAxe.get());
                        output.accept(ModItems.PorkchopyoniteShovel.get());
                        output.accept(ModItems.PorkchopyoniteHoe.get());
                        output.accept(ModItems.PorkchopUpgrade.get());

                        output.accept(ModBlocks.PorkchopBlock.get());
                        output.accept(ModBlocks.PorkingStation.get());
                        output.accept(ModBlocks.PorkchopStairs.get());
                        output.accept(ModBlocks.PorkchopSlab.get());
                        output.accept(ModBlocks.PorkchopButton.get());
                        output.accept(ModBlocks.PorkchopPressurePlate.get());
                        output.accept(ModBlocks.PorkchopWall.get());
                        output.accept(ModBlocks.PorkchopDoor.get());
                        output.accept(ModBlocks.PorkchopTrapdoor.get());
                        output.accept(ModBlocks.IronFurnace.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
