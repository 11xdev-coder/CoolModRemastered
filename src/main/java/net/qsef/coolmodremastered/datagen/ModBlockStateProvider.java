package net.qsef.coolmodremastered.datagen;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.RailShape;
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

        horizontalDirectionalBlockWithItem("industrial_furnace", "block/industrial_furnace_side",
                "block/industrial_furnace_front", "block/industrial_furnace_top",
                (IHorizontalDirectionalBlock) ModBlocks.IndustrialFurnace.get());

        blockWithItem(ModBlocks.PorkchopBlock);
        stairsBlock((StairBlock) ModBlocks.PorkchopStairs.get(), blockTexture(ModBlocks.PorkchopBlock.get()));
        slabBlock((SlabBlock) ModBlocks.PorkchopSlab.get(), blockTexture(ModBlocks.PorkchopBlock.get()), blockTexture(ModBlocks.PorkchopBlock.get()));
        buttonBlock((ButtonBlock) ModBlocks.PorkchopButton.get(), blockTexture(ModBlocks.PorkchopBlock.get()));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.PorkchopPressurePlate.get(), blockTexture(ModBlocks.PorkchopBlock.get()));
        wallBlock((WallBlock) ModBlocks.PorkchopWall.get(), blockTexture(ModBlocks.PorkchopBlock.get()));
        doorBlockWithRenderType((DoorBlock) ModBlocks.PorkchopDoor.get(), modLoc("block/porkchop_door_bottom"), modLoc("block/porkchop_door_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.PorkchopTrapdoor.get(), modLoc("block/porkchop_trapdoor"), true, "cutout");

        // Add data generation for the copper rail block with proper render type
        railBlockWithItem(ModBlocks.CopperRail, "copper_rail", "cutout");
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

    private void railBlockWithItem(RegistryObject<Block> block, String textureName) {
        railBlockWithItem(block, textureName, "cutout");
    }

    private void railBlockWithItem(RegistryObject<Block> block, String textureName, String renderType) {
        // Get the rail block
        RailBlock railBlock = (RailBlock) block.get();

        // Get texture path for the rail
        ResourceLocation texture = modLoc("block/" + textureName);

        // Create the rail model for straight rails - using flat rail parent from minecraft models
        ModelFile railStraight = models().withExistingParent(block.getId().getPath(), mcLoc("block/rail_flat"))
                .texture("rail", texture)
                .renderType(renderType);

        // Create the rail model for curved rails
        ModelFile railCorner = models().withExistingParent(block.getId().getPath() + "_corner", mcLoc("block/rail_curved"))
                .texture("rail", texture)
                .renderType(renderType);

        // Create raised rail model for ascending rails
        ModelFile railRaised = models().withExistingParent(block.getId().getPath() + "_raised", mcLoc("block/rail_raised_ne"))
                .texture("rail", texture)
                .renderType(renderType);

        // Generate all rail blockstates
        getVariantBuilder(railBlock)
                .forAllStatesExcept(state -> {
                    RailShape shape = state.getValue(RailBlock.SHAPE);

                    // Determine model to use based on shape
                    ModelFile model;
                    int rotationY = 0;

                    if (shape == RailShape.NORTH_SOUTH) {
                        model = railStraight;
                    } else if (shape == RailShape.EAST_WEST) {
                        model = railStraight;
                        rotationY = 90;
                    } else if (shape == RailShape.ASCENDING_EAST) {
                        model = railRaised;
                        rotationY = 90;
                    } else if (shape == RailShape.ASCENDING_WEST) {
                        model = railRaised;
                        rotationY = 270;
                    } else if (shape == RailShape.ASCENDING_NORTH) {
                        model = railRaised;
                        rotationY = 0;
                    } else if (shape == RailShape.ASCENDING_SOUTH) {
                        model = railRaised;
                        rotationY = 180;
                    } else {
                        // For corner rails
                        model = railCorner;
                        if (shape == RailShape.SOUTH_EAST) {
                            rotationY = 0;
                        } else if (shape == RailShape.SOUTH_WEST) {
                            rotationY = 90;
                        } else if (shape == RailShape.NORTH_WEST) {
                            rotationY = 180;
                        } else if (shape == RailShape.NORTH_EAST) {
                            rotationY = 270;
                        }
                    }

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(rotationY)
                            .build();
                }, RailBlock.WATERLOGGED); // Exclude waterlogged property

        // Generate the item model for the rail
        itemModels().withExistingParent(block.getId().getPath(), mcLoc("item/generated"))
                .texture("layer0", modLoc("block/" + textureName));
    }
}
