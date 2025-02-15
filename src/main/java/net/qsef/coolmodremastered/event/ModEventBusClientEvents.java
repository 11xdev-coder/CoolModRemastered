package net.qsef.coolmodremastered.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.entity.client.ModModelLayers;
import net.qsef.coolmodremastered.entity.client.RocketModel;

@Mod.EventBusSubscriber(modid = CoolModRemastered.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.ROCKET_LAYER, RocketModel::createBodyLayer);
    }
}
