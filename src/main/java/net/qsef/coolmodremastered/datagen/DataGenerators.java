package net.qsef.coolmodremastered.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.qsef.coolmodremastered.CoolModRemastered;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CoolModRemastered.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUpProvider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new ModRecipeProvider(packOutput));
        gen.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput));

        gen.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, existingFileHelper));
        gen.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, existingFileHelper));

        ModBlockTagGenerator blockTagGenerator = gen.addProvider(event.includeServer(),
                new ModBlockTagGenerator(packOutput, lookUpProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new ModItemTagGenerator(packOutput, lookUpProvider, blockTagGenerator.contentsGetter(), existingFileHelper));
    }
}
