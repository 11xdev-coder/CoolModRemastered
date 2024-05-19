package net.qsef.coolmodremastered.datagen.loot;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.item.ModItems;

import java.util.List;
import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.PorkingStation.get());
        this.add(ModBlocks.PorkchopBlock.get(), createPorkchopBlockTable());
    }

    protected LootTable.Builder createPorkchopBlockTable() {
        LootPool.Builder pool1 = LootPool.lootPool()
                .setRolls(UniformGenerator.between(6, 8))
                .add(LootItem.lootTableItem(ModItems.Porkchopyonite.get()))
                .when(InvertedLootItemCondition.invert(MatchTool.toolMatches(
                        ItemPredicate.Builder.item().of(ItemTags.HOES)
                ))); // drop 6-8 porkchopyonite when Tool isnt a HOE

        LootPool.Builder pool2 = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(ModBlocks.PorkchopBlock.get()))
                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.HOES))); // 1 porkchop block when Tool is HOE

        return LootTable.lootTable().withPool(pool1).withPool(pool2);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        // get every block for loot table, accounts only blocks without noLoottable() flag
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
