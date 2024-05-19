package net.qsef.coolmodremastered.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.custom.PorkingStation;
import net.qsef.coolmodremastered.item.ModItems;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CoolModRemastered.MOD_ID);

    public static final RegistryObject<Block> PorkchopBlock = registerBlock("porkchop_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .sound(SoundType.GRAVEL) // use sounds from gravel
                    .strength(1F))); // and some basic strength

    public static final RegistryObject<Block> PorkingStation = registerBlock("porking_station",
            () -> new PorkingStation(BlockBehaviour.Properties.of()
                    .sound(SoundType.STONE)
                    .strength(3f)
                    .requiresCorrectToolForDrops()));

    // example ore block
//    private static final RegistryObject<Block> ORE_BLOCK = registerBlock("ore_block",
//            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
//                    .requiresCorrectToolForDrops(), UniformInt.of(3, 6))); // 3-6 xp

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> blockObj = BLOCKS.register(name, block); // register the block in the registry
        registerBlockItem(name, blockObj); // register item
        return blockObj;
    }

    // register item for block
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties())); // new block item with default properties
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
