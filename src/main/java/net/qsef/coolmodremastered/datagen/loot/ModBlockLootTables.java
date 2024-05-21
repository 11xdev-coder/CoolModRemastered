package net.qsef.coolmodremastered.datagen.loot;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.item.ModItems;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.PorkingStation.get());

        // porkchop
        this.add(ModBlocks.PorkchopBlock.get(), createPorkchopBlockTable(6, 8, ModBlocks.PorkchopBlock.get()));
        this.add(ModBlocks.PorkchopStairs.get(), createPorkchopBlockTable(3,5, ModBlocks.PorkchopStairs.get()));
        this.add(ModBlocks.PorkchopButton.get(), createPorkchopBlockTable(1, 1, ModBlocks.PorkchopButton.get()));
        this.add(ModBlocks.PorkchopPressurePlate.get(), createPorkchopBlockTable(1, 2, ModBlocks.PorkchopPressurePlate.get()));
        this.add(ModBlocks.PorkchopWall.get(), createPorkchopBlockTable(3, 5, ModBlocks.PorkchopWall.get()));
        this.add(ModBlocks.PorkchopTrapdoor.get(), createPorkchopBlockTable(1, 3, ModBlocks.PorkchopTrapdoor.get()));

        // door and slabs
        this.add(ModBlocks.PorkchopDoor.get(), createDoorTable(ModBlocks.PorkchopDoor.get()));
        this.add(ModBlocks.PorkchopSlab.get(), slabTableWithCustomItem(1, 2, ModBlocks.PorkchopSlab.get(), ModBlocks.PorkchopSlab.get()));
    }

    protected LootTable.Builder createPorkchopBlockTable(int pMin, int pMax, ItemLike blockItemToDrop) {
        LootPool.Builder pool1 = LootPool.lootPool()
                .setRolls(UniformGenerator.between(pMin, pMax))
                .add(LootItem.lootTableItem(ModItems.Porkchopyonite.get()))
                .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(
                        ItemPredicate.Builder.item().of(ItemTags.HOES)
                ))); // drop 6-8 porkchopyonite when Tool isnt a HOE

        LootPool.Builder pool2 = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(blockItemToDrop))
                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.HOES))); // 1 porkchop block when Tool is HOE

        return LootTable.lootTable().withPool(pool1).withPool(pool2);
    }

    protected LootTable.Builder doorTableWithCustomItem(int pMin, int pMax, ItemLike blockItemToDrop, Block blockState) {
        LootPool.Builder pool1 = LootPool.lootPool()
                .setRolls(UniformGenerator.between(pMin, pMax))
                .add(LootItem.lootTableItem(ModItems.Porkchopyonite.get()))
                .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(
                        ItemPredicate.Builder.item().of(ItemTags.HOES)
                )))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockState)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)));

        LootPool.Builder pool2 = LootPool.lootPool()
                .setRolls(UniformGenerator.between(pMin, pMax))
                .add(LootItem.lootTableItem(ModItems.Porkchopyonite.get()))
                .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(
                        ItemPredicate.Builder.item().of(ItemTags.HOES)
                )))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockState)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(DoorBlock.HALF, DoubleBlockHalf.UPPER)));

        LootPool.Builder pool3 = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(blockItemToDrop))
                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.HOES)))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockState)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)));

        LootPool.Builder pool4 = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(blockItemToDrop))
                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.HOES)))
                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockState)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(DoorBlock.HALF, DoubleBlockHalf.UPPER)));

        return LootTable.lootTable().withPool(pool4).withPool(pool3).withPool(pool2).withPool(pool1);
    }

    protected LootTable.Builder slabTableWithCustomItem(int pMin, int pMax, ItemLike blockItemToDrop, Block blockState) {
        LootPool.Builder pool1 = LootPool.lootPool()
                .setRolls(UniformGenerator.between(pMin, pMax))
                .add(LootItem.lootTableItem(ModItems.Porkchopyonite.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)) // constant value 2
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockState) // when has properties
                                        .setProperties(StatePropertiesPredicate.Builder.properties() // set properties
                                                .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))) // to double slab
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)) // constant value 1
                                .when(InvertedLootItemCondition.invert(LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockState) // when has properties (inverted)
                                        .setProperties(StatePropertiesPredicate.Builder.properties() // set properties
                                                .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE))))) // to double slab
                .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(
                        ItemPredicate.Builder.item().of(ItemTags.HOES)
                )))
                .apply(ApplyExplosionDecay.explosionDecay()));

        LootPool.Builder pool2 = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1)) // 1 roll
                .add(LootItem.lootTableItem(blockItemToDrop)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockState)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))) // when double slab, constant value 2
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))
                                .when(InvertedLootItemCondition.invert(LootItemBlockStatePropertyCondition.hasBlockStateProperties(blockState)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))))) // invert double slab, constant value 1
                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.HOES))) // when tag is hoe
                .apply(ApplyExplosionDecay.explosionDecay());

        return LootTable.lootTable().withPool(pool1).withPool(pool2);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        // get every block for loot table, accounts only blocks without noLoottable() flag
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
