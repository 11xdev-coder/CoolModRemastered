package net.qsef.coolmodremastered.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.qsef.coolmodremastered.CoolModRemastered;

@Mod.EventBusSubscriber(modid = CoolModRemastered.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        // register attribute for living entities
    }
}
