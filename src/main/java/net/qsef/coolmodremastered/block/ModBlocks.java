package net.qsef.coolmodremastered.block;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
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

    public static final BlockSetType PorkchopSetType = new BlockSetType("porkchop", true, SoundType.GRAVEL,
            SoundEvents.GRAVEL_BREAK, SoundEvents.PIG_AMBIENT, SoundEvents.GRAVEL_BREAK,
            SoundEvents.PIG_AMBIENT, SoundEvents.GRAVEL_BREAK, SoundEvents.PIG_AMBIENT,
            SoundEvents.GRAVEL_BREAK, SoundEvents.PIG_AMBIENT);

    // PORKCHOP
    public static final RegistryObject<Block> PorkchopBlock = registerBlock("porkchop_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .sound(SoundType.GRAVEL) // use sounds from gravel
                    .strength(1F))); // and some basic strength

    public static final RegistryObject<Block> PorkchopStairs = registerBlock("porkchop_stairs",
            () -> new StairBlock(() -> ModBlocks.PorkchopBlock.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(ModBlocks.PorkchopBlock.get())));

    public static final RegistryObject<Block> PorkchopSlab = registerBlock("porkchop_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(ModBlocks.PorkchopBlock.get())));

    public static final RegistryObject<Block> PorkchopButton = registerBlock("porkchop_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(ModBlocks.PorkchopBlock.get()),
                    PorkchopSetType, 3, true));

    public static final RegistryObject<Block> PorkchopPressurePlate = registerBlock("porkchop_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(ModBlocks.PorkchopBlock.get()),
                    PorkchopSetType));

    public static final RegistryObject<Block> PorkchopWall = registerBlock("porkchop_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(ModBlocks.PorkchopBlock.get())));

    public static final RegistryObject<Block> PorkchopDoor = registerBlock("porkchop_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(ModBlocks.PorkchopBlock.get()).noOcclusion(), PorkchopSetType));

    public static final RegistryObject<Block> PorkchopTrapdoor = registerBlock("porkchop_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(ModBlocks.PorkchopBlock.get()).noOcclusion(), PorkchopSetType));
    // PORKCHOP

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
