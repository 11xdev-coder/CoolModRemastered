package net.qsef.coolmodremastered;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.block.entity.ModBlockEntities;
import net.qsef.coolmodremastered.entity.ModEntities;
import net.qsef.coolmodremastered.item.ModCreativeModeTabs;
import net.qsef.coolmodremastered.item.ModItems;
import net.qsef.coolmodremastered.network.ModNetwork;
import net.qsef.coolmodremastered.recipe.ModRecipes;
import net.qsef.coolmodremastered.screen.IronFurnaceScreen;
import net.qsef.coolmodremastered.screen.ModMenuTypes;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CoolModRemastered.MOD_ID)
public class CoolModRemastered {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "coolmodremastered";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public CoolModRemastered() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);
        ModEntities.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(PlayerOnItemPickup.class);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(ClientModEvents::onClientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.registerPackets();
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
