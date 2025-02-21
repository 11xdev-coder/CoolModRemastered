package net.qsef.coolmodremastered.item;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.item.custom.*;

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

    public static final RegistryObject<Item> EmeraldStaff = ITEMS.register("emerald_staff",
            () -> new EmeraldStaff(new Item.Properties()));

    public static final RegistryObject<Item> SteelIngot = ITEMS.register("steel_ingot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> Carbon = ITEMS.register("carbon",
            () -> new IndustrialFuelItem(new Item.Properties(), 400));

    public static final RegistryObject<Item> CompressedCharcoal = ITEMS.register("compressed_charcoal",
            () -> new IndustrialFuelItem(new Item.Properties(), 400));

    public static final RegistryObject<Item> Bazooka = ITEMS.register("bazooka",
            () -> new Bazooka(new Item.Properties()));

    // Porkchopyonite tools
    public static final RegistryObject<Item> PorkchopyoniteSword = ITEMS.register("porkchopyonite_sword",
            () -> new SwordItem(ModToolTiers.PORKCHOPYONITE_TIER, 3,-2.4f, new Item.Properties()));

    public static final RegistryObject<Item> PorkchopyonitePickaxe = ITEMS.register("porkchopyonite_pickaxe",
            () -> new PickaxeItem(ModToolTiers.PORKCHOPYONITE_TIER, 1,-2.8f, new Item.Properties()));

    public static final RegistryObject<Item> PorkchopyoniteAxe = ITEMS.register("porkchopyonite_axe",
            () -> new AxeItem(ModToolTiers.PORKCHOPYONITE_TIER, 6,-3.1f, new Item.Properties()));

    public static final RegistryObject<Item> PorkchopyoniteShovel = ITEMS.register("porkchopyonite_shovel",
            () -> new ShovelItem(ModToolTiers.PORKCHOPYONITE_TIER, 1.5f,-3f, new Item.Properties()));

    public static final RegistryObject<Item> PorkchopyoniteHoe = ITEMS.register("porkchopyonite_hoe",
            () -> new HoeItem(ModToolTiers.PORKCHOPYONITE_TIER, -2,-1, new Item.Properties()));
    // -------------

    // Porkchop upgrade
    public static final RegistryObject<Item> PorkchopUpgrade = ITEMS.register("porkchop_upgrade",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
