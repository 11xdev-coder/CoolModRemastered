package net.qsef.coolmodremastered.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties PORK_BURGER = new FoodProperties.Builder().nutrition(7)
            .saturationMod(0.8f).meat().fast().build();

    public static final FoodProperties ROASTED_PORKCHOP = new FoodProperties.Builder().nutrition(2)
            .saturationMod(0.1f).effect(new MobEffectInstance(MobEffects.POISON, 200, 3), 1)
            .effect(new MobEffectInstance(MobEffects.CONFUSION, 300, 2), 1)
            .build();
}
