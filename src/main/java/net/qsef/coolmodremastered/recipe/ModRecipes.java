package net.qsef.coolmodremastered.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;

public class ModRecipes {
    // register serializers
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CoolModRemastered.MOD_ID);

    public static final RegistryObject<RecipeSerializer<IronFurnaceRecipe>> IronFurnaceSerializer =
            RECIPE_SERIALIZERS.register("iron_furnace_smelting", IronFurnaceRecipe.Serializer::new);

    public static final RegistryObject<RecipeSerializer<IndustrialFurnaceRecipe>> IndustrialFurnaceSerializer =
            RECIPE_SERIALIZERS.register("industrial", IndustrialFurnaceRecipe.Serializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
