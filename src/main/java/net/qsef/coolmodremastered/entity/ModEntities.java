package net.qsef.coolmodremastered.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.entity.custom.RocketEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CoolModRemastered.MOD_ID);

    public static final RegistryObject<EntityType<RocketEntity>> Rocket = ENTITIES.register("rocket",
            () -> EntityType.Builder.<RocketEntity>of(RocketEntity::new, MobCategory.MISC)
                    .sized(2.5f, 2.5f).build("rocket"));

    public static void register(IEventBus bus) { ENTITIES.register(bus); }
}
