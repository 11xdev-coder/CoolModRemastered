package net.qsef.coolmodremastered.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.qsef.coolmodremastered.CoolModRemastered;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PORKCHOP_BLOCKS = tag("porkchop_blocks");
        public static final TagKey<Block> NEEDS_PORKCHOPYONITE_TOOL = tag("needs_porkchopyonite_tool");

        public static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(CoolModRemastered.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> IRON_INGOT_TAG = tag("iron_ingot");

        public static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(CoolModRemastered.MOD_ID, name));
        }
    }
}
