package net.qsef.coolmodremastered.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.item.ModItems;
import net.qsef.coolmodremastered.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, CoolModRemastered.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ModTags.Items.IRON_INGOT_TAG)
                .add(Items.IRON_INGOT);

        tag(ItemTags.TRIMMABLE_ARMOR).add(ModItems.SteelHelmet.get())
                .add(ModItems.SteelChestplate.get()).add(ModItems.SteelLeggings.get()).add(ModItems.SteelBoots.get());
    }
}
