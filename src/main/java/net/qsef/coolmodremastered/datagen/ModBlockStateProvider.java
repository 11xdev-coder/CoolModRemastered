package net.qsef.coolmodremastered.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.block.base.IHorizontalDirectionalBlock;
import net.qsef.coolmodremastered.block.custom.IronFurnaceBlock;

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

        horizontalDirectionalBlockWithItem("iron_furnace", "block/iron_furnace_side", "block/iron_furnace_front",
                "block/iron_furnace_top", (IHorizontalDirectionalBlock) ModBlocks.IronFurnace.get());

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

    private void horizontalDirectionalBlockWithItem(String name, String pathSide, String pathFront, String pathTop, IHorizontalDirectionalBlock block) {
        // orientable -> side, front, top
        ModelFile model = models().orientable(name,
                        new ResourceLocation(CoolModRemastered.MOD_ID, pathSide),
                        new ResourceLocation(CoolModRemastered.MOD_ID, pathFront),
                        new ResourceLocation(CoolModRemastered.MOD_ID, pathTop))
                .texture("particle", new ResourceLocation(CoolModRemastered.MOD_ID, pathFront));

        getVariantBuilder((Block) block).forAllStates(state -> {
            Direction direction = state.getValue(block.FACING);
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY((int) direction.toYRot())
                    .build();
        });

        simpleBlockItem((Block) block, model);
    }
}
