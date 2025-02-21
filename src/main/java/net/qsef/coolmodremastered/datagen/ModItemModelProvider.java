package net.qsef.coolmodremastered.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CoolModRemastered.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.SteelIngot);
        simpleItem(ModItems.Carbon);
        simpleItem(ModItems.CompressedCharcoal);
        simpleItem(ModItems.PigRelic);
        simpleItem(ModItems.PigSoul);
        simpleItem(ModItems.PorkBurger);
        simpleItem(ModItems.Porkchopyonite);
        simpleItem(ModItems.RoastedPorkchop);
        simpleItem(ModItems.PorkchopUpgrade);

        // Porkchop furniture
        simpleBlockItem(ModBlocks.PorkchopDoor);
        buttonItem(ModBlocks.PorkchopButton, ModBlocks.PorkchopBlock);
        wallItem(ModBlocks.PorkchopWall, ModBlocks.PorkchopBlock);
        evenSimplerBlockItem(ModBlocks.PorkchopStairs);
        evenSimplerBlockItem(ModBlocks.PorkchopSlab);
        evenSimplerBlockItem(ModBlocks.PorkchopPressurePlate);
        trapdoorItem(ModBlocks.PorkchopTrapdoor);

        // porkchop tools
        handheldItem(ModItems.PorkchopyoniteSword);
        handheldItem(ModItems.PorkchopyonitePickaxe);
        handheldItem(ModItems.PorkchopyoniteAxe);
        handheldItem(ModItems.PorkchopyoniteShovel);
        handheldItem(ModItems.PorkchopyoniteHoe);
    }

    // item model with only parent (used for 3d block items)
    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(CoolModRemastered.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    // handheld for tools, for more 3d look
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(CoolModRemastered.MOD_ID, "item/" + item.getId().getPath()));
    }

    // creates a basic 2d item
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0", // parent: item/generated
                new ResourceLocation(CoolModRemastered.MOD_ID, "item/" + item.getId().getPath())); // layer0: coolmodremastered:item/ItemName
    }

    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(CoolModRemastered.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  new ResourceLocation(CoolModRemastered.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(CoolModRemastered.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    // 2d item for a block (doors)
    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(CoolModRemastered.MOD_ID,"item/" + item.getId().getPath()));
    }
}
