package net.qsef.coolmodremastered.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.item.custom.FuelItem;
import net.qsef.coolmodremastered.item.custom.PigRelic;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CoolModRemastered.MOD_ID);

    public static final RegistryObject<Item> Porkchopyonite = ITEMS.register("porkchopyonite",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PigRelic = ITEMS.register("pig_relic",
            () -> new PigRelic(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> PigSoul = ITEMS.register("pig_soul",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> PorkBurger = ITEMS.register("pork_burger",
            () -> new Item(new Item.Properties().food(ModFoods.PORK_BURGER)));

    public static final RegistryObject<Item> RoastedPorkchop = ITEMS.register("roasted_porkchop",
            () -> new FuelItem(new Item.Properties().food(ModFoods.ROASTED_PORKCHOP), 300));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
