package net.qsef.coolmodremastered.event;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.item.ModArmorMaterials;
import net.qsef.coolmodremastered.item.custom.SteelArmorItem;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = CoolModRemastered.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEventBusEvents {
//    @SubscribeEvent
//    public static void registerAttributes(EntityAttributeCreationEvent event) {
//        // register attribute for living entities
//    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().is(DamageTypes.ON_FIRE) || event.getSource().is(DamageTypes.IN_FIRE) || event.getSource().is(DamageTypes.LAVA)) {
            reduceDamage(event, ModArmorMaterials::getFireResistance);
        }

        if (event.getSource().is(DamageTypes.EXPLOSION) || event.getSource().is(DamageTypes.PLAYER_EXPLOSION)) {
            reduceDamage(event, ModArmorMaterials::getBlastResistance);
        }
    }

    private static void reduceDamage(LivingHurtEvent event, Function<ModArmorMaterials, Float> resistanceGetter) {
        float reduction = 0f;
        LivingEntity entity = event.getEntity();

        for (ItemStack armorStack : entity.getArmorSlots()) {
            if (armorStack.getItem() instanceof SteelArmorItem armorItem) {
                ModArmorMaterials material = (ModArmorMaterials) armorItem.getMaterial();

                reduction += resistanceGetter.apply(material);
            }
        }

        // max = 80%
        reduction = Mth.clamp(reduction, 0f, 0.8f);
        float originalDamage = event.getAmount();
        float newDamage = originalDamage * (1 - reduction);
        event.setAmount(newDamage);
        CoolModRemastered.LOGGER.debug("Reduced damage from " + originalDamage + " to " + newDamage);
    }
}
