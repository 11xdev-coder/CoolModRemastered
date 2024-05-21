package net.qsef.coolmodremastered.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CoolModRemastered.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Blocks.PORKCHOP_BLOCKS)
                .add(ModBlocks.PorkchopBlock.get()); // add porkchop blocks to the tag

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.PorkingStation.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.PorkingStation.get());

        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.PorkchopBlock.get())
                .add(ModBlocks.PorkchopStairs.get())
                .add(ModBlocks.PorkchopButton.get())
                .add(ModBlocks.PorkchopSlab.get())
                .add(ModBlocks.PorkchopWall.get())
                .add(ModBlocks.PorkchopPressurePlate.get())
                .add(ModBlocks.PorkchopDoor.get())
                .add(ModBlocks.PorkchopTrapdoor.get());

        this.tag(BlockTags.WALLS)
                .add(ModBlocks.PorkchopWall.get());
    }
}
