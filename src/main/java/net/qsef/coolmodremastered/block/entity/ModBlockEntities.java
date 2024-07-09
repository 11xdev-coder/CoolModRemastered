package net.qsef.coolmodremastered.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CoolModRemastered.MOD_ID);

    public static final RegistryObject<BlockEntityType<IronFurnaceBlockEntity>> IronFurnace_BE =
            BLOCK_ENTITIES.register("iron_furnace",
                    () -> BlockEntityType.Builder.of(IronFurnaceBlockEntity::new, ModBlocks.IronFurnace.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
