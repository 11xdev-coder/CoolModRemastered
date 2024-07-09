package net.qsef.coolmodremastered.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
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

        // orientable -> side, front, top
        ModelFile ironFurnaceModel = models().orientable("iron_furnace",
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/iron_furnace_side"),
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/iron_furnace_front"),
                        new ResourceLocation(CoolModRemastered.MOD_ID, "block/iron_furnace_top"))
                .texture("particle", new ResourceLocation(CoolModRemastered.MOD_ID, "block/iron_furnace_front"));

        blockWithItemCustomModel(ModBlocks.IronFurnace, ironFurnaceModel);

        blockWithItem(ModBlocks.PorkchopBlock);
        stairsBlock((StairBlock) ModBlocks.PorkchopStairs.get(), blockTexture(ModBlocks.PorkchopBlock.get()));
        slabBlock((SlabBlock) ModBlocks.PorkchopSlab.get(), blockTexture(ModBlocks.PorkchopBlock.get()), blockTexture(ModBlocks.PorkchopBlock.get()));
        buttonBlock((ButtonBlock) ModBlocks.PorkchopButton.get(), blockTexture(ModBlocks.PorkchopBlock.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.PorkchopPressurePlate.get(), blockTexture(ModBlocks.PorkchopBlock.get()));
        wallBlock((WallBlock) ModBlocks.PorkchopWall.get(), blockTexture(ModBlocks.PorkchopBlock.get()));
        doorBlockWithRenderType((DoorBlock) ModBlocks.PorkchopDoor.get(), modLoc("block/porkchop_door_bottom"), modLoc("block/porkchop_door_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.PorkchopTrapdoor.get(), modLoc("block/porkchop_trapdoor"), true, "cutout");
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
