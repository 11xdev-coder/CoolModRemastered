package net.qsef.coolmodremastered.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CoolModRemastered.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // down, up, all sides
        ModelFile porkingStationModel = models().cube("porking_station",
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/porking_station_bottom"),
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/porking_station_top"),
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/porking_station"),
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/porking_station"),
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/porking_station"),
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/porking_station"))
                .texture("particle", new ResourceLocation(CoolModRemastered.MOD_ID, "block/porking_station_bottom"));

        blockWithItemCustomModel(ModBlocks.PorkingStation, porkingStationModel);
        blockWithItem(ModBlocks.PorkchopBlock);
    }

    // creates a block with an item
    private void blockWithItem(RegistryObject<Block> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void blockWithItemCustomModel(RegistryObject<Block> block, ModelFile model) {
        simpleBlock(block.get(), model);
        simpleBlockItem(block.get(), model);
    }
}
