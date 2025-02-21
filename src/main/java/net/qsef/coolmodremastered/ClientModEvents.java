package net.qsef.coolmodremastered;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.qsef.coolmodremastered.entity.ModEntities;
import net.qsef.coolmodremastered.entity.client.RocketRenderer;
import net.qsef.coolmodremastered.network.ModNetwork;
import net.qsef.coolmodremastered.screen.IndustrialFurnaceScreen;
import net.qsef.coolmodremastered.screen.IronFurnaceScreen;
import net.qsef.coolmodremastered.screen.ModMenuTypes;

import static net.qsef.coolmodremastered.CoolModRemastered.MOD_ID;

public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.IronFurnaceMenu.get(), IronFurnaceScreen::new);
            MenuScreens.register(ModMenuTypes.IndustrialFurnaceMenu.get(), IndustrialFurnaceScreen::new);
            EntityRenderers.register(ModEntities.Rocket.get(), RocketRenderer::new);
        });
    }
}