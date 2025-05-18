package net.qsef.coolmodremastered.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
// Removed: import net.minecraft.tags.TagKey; // Not directly used, but could be if you use custom tags in recipes
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
// Removed: import net.minecraft.world.level.block.Blocks; // Not directly used
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.datagen.recipebuilder.IndustrialFurnaceRecipeBuilder;
import net.qsef.coolmodremastered.datagen.recipebuilder.IronFurnaceRecipeBuilder;
import net.qsef.coolmodremastered.item.ModItems;
// Removed: import net.qsef.coolmodremastered.recipe.IronFurnaceRecipe; // Not directly used in this provider
// Removed: import net.qsef.coolmodremastered.util.ModTags; // Not directly used, but could be

// Removed: import javax.annotation.Nullable; // Not used
// Removed: import java.util.Iterator; // Not used
import java.util.List;
import java.util.function.Consumer; // Changed from RecipeOutput

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public static final List<ItemLike> COOKED_PORKCHOP_LIST = List.of(Items.COOKED_PORKCHOP);
    public static final List<ItemLike> IRON_INGOT_LIST = List.of(Items.IRON_INGOT);
    public static final List<ItemLike> COMPRESSED_CHARCOAL_LIST = List.of(ModItems.CompressedCharcoal.get());
    public static final List<ItemLike> COAL_BLOCK_LIST = List.of(Items.COAL_BLOCK);

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        // roasted porkchop smelting, smoking
        smeltingRecipes(pWriter, COOKED_PORKCHOP_LIST, RecipeCategory.FOOD,
                ModItems.RoastedPorkchop.get(), 0F, 100, "roasted_porkchop");
        smokingRecipes(pWriter, COOKED_PORKCHOP_LIST, RecipeCategory.FOOD,
                ModItems.RoastedPorkchop.get(), 0F, 50, "roasted_porkchop");

        // blasting: steel ingot
        blastingRecipes(pWriter, IRON_INGOT_LIST, RecipeCategory.MISC, ModItems.SteelIngot.get(), 1f, 300, "steel_ingot");
        // Corrected industrialFurnace call to use the pWriter
        industrialFurnace(pWriter, RecipeCategory.MISC, Items.IRON_INGOT, new ItemStack(ModItems.SteelIngot.get(), 1), 1f, "steel_ingot");
        blastingRecipes(pWriter, COMPRESSED_CHARCOAL_LIST, RecipeCategory.MISC, ModItems.Carbon.get(), 1f, 100, "carbon");
        blastingRecipes(pWriter, COAL_BLOCK_LIST, RecipeCategory.MISC, ModItems.Carbon.get(), 1f, 150, "carbon");

        // compressed charcoal
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CompressedCharcoal.get(), 1)
                .unlockedBy(getHasName(ModItems.CompressedCharcoal.get()), has(ModItems.CompressedCharcoal.get()))
                .requires(Items.CHARCOAL, 4).save(pWriter);

        // industrial furnace
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModBlocks.IndustrialFurnace.get())
                .pattern("iii")
                .pattern("blb")
                .pattern("ggg")
                .define('b', Items.BLAST_FURNACE)
                .define('l', Items.LAVA_BUCKET)
                .define('i', Items.IRON_BLOCK)
                .define('g', Items.IRON_INGOT)
                .unlockedBy(getHasName(ModBlocks.IndustrialFurnace.get()), has(ModBlocks.IndustrialFurnace.get()))
                .save(pWriter);

        // bazooka
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.Bazooka.get())
                .pattern("xx ")
                .pattern("yir")
                .pattern("xx ")
                .define('x', Items.IRON_INGOT)
                .define('y', Items.GUNPOWDER)
                .define('i', Items.IRON_BLOCK)
                .define('r', Items.REDSTONE)
                .unlockedBy(getHasName(ModItems.Bazooka.get()), has(ModItems.Bazooka.get()))
                .save(pWriter);

        // steel armor
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SteelHelmet.get())
                .pattern("xxx")
                .pattern("x x")
                // Removed empty pattern line: .pattern("   ")
                .define('x', ModItems.SteelIngot.get())
                .unlockedBy(getHasName(ModItems.SteelHelmet.get()), has(ModItems.SteelHelmet.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SteelChestplate.get())
                .pattern("x x")
                .pattern("xxx")
                .pattern("xxx")
                .define('x', ModItems.SteelIngot.get())
                .unlockedBy(getHasName(ModItems.SteelChestplate.get()), has(ModItems.SteelChestplate.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SteelLeggings.get())
                .pattern("xxx")
                .pattern("x x")
                .pattern("x x")
                .define('x', ModItems.SteelIngot.get())
                .unlockedBy(getHasName(ModItems.SteelLeggings.get()), has(ModItems.SteelLeggings.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.SteelBoots.get())
                .pattern("x x")
                .pattern("x x")
                // Removed empty pattern line: .pattern("   ")
                .define('x', ModItems.SteelIngot.get())
                .unlockedBy(getHasName(ModItems.SteelBoots.get()), has(ModItems.SteelBoots.get()))
                .save(pWriter);



        // porkchop block
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PorkchopBlock.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.Porkchopyonite.get())
                .unlockedBy(getHasName(ModItems.Porkchopyonite.get()), has(ModItems.Porkchopyonite.get())) // unlock the recipe when player has porkchopyonite
                .save(pWriter);

        // pork burger
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.PorkBurger.get())
                .pattern(" x ")
                .pattern(" y ")
                .pattern(" x ")
                .define('x', Items.BREAD)
                .define('y', Items.COOKED_PORKCHOP)
                .unlockedBy(getHasName(Items.BREAD), has(Items.BREAD))
                .unlockedBy(getHasName(Items.COOKED_PORKCHOP), has(Items.COOKED_PORKCHOP))
                .save(pWriter);

        // porking station
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PorkingStation.get())
                .pattern("xyx")
                .pattern("y y")
                .pattern("xyx")
                .define('x', ModItems.Porkchopyonite.get())
                .define('y', ModItems.PigSoul.get())
                .unlockedBy(getHasName(ModItems.Porkchopyonite.get()), has(ModItems.Porkchopyonite.get()))
                .unlockedBy(getHasName(ModItems.PigSoul.get()), has(ModItems.PigSoul.get()))
                .save(pWriter);

        // porkchopyonite from porkchop block
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.Porkchopyonite.get(), 9)
                .requires(ModBlocks.PorkchopBlock.get())
                .unlockedBy(getHasName(ModBlocks.PorkchopBlock.get()), has(ModBlocks.PorkchopBlock.get()))
                .save(pWriter);

        // iron furnace block
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.IronFurnace.get())
                .pattern("ipi")
                .pattern("ifi")
                .pattern("ili")
                .define('i', Items.IRON_BLOCK)
                .define('p', Items.COOKED_PORKCHOP)
                .define('f', Items.FURNACE)
                .define('l', Items.LAVA_BUCKET)
                .unlockedBy(getHasName(Items.LAVA_BUCKET), has(Items.LAVA_BUCKET))
                .unlockedBy(getHasName(Items.IRON_BLOCK), has(Items.IRON_BLOCK))
                .save(pWriter);

        // porkchopyonite from cooked porkchop
        ironFurnaceSmelting(pWriter, RecipeCategory.FOOD, Items.COOKED_PORKCHOP,
                new ItemStack(ModItems.Porkchopyonite.get(), 1), 6f, "porkchopyonite");

        // Porkchop upgrade craft
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.PorkchopUpgrade.get())
                .pattern("grb")
                .pattern("cpb")
                .pattern("cpb")
                .define('g', Items.GUNPOWDER)
                .define('r', Items.ROTTEN_FLESH)
                .define('b', Items.MUTTON)
                .define('p', ModItems.Porkchopyonite.get())
                .define('c', Items.COOKED_PORKCHOP)
                .unlockedBy(getHasName(ModItems.Porkchopyonite.get()), has(ModItems.Porkchopyonite.get()))
                .unlockedBy(getHasName(Items.COOKED_PORKCHOP), has(Items.COOKED_PORKCHOP))
                .save(pWriter);

        // porkchop tools
        smithingRecipe(pWriter, ModItems.PorkchopUpgrade.get(), Items.IRON_SWORD, ModItems.Porkchopyonite.get(), RecipeCategory.COMBAT,
                ModItems.PorkchopyoniteSword.get());

        smithingRecipe(pWriter, ModItems.PorkchopUpgrade.get(), Items.IRON_PICKAXE, ModItems.Porkchopyonite.get(), RecipeCategory.TOOLS,
                ModItems.PorkchopyonitePickaxe.get());

        smithingRecipe(pWriter, ModItems.PorkchopUpgrade.get(), Items.IRON_AXE, ModItems.Porkchopyonite.get(), RecipeCategory.TOOLS,
                ModItems.PorkchopyoniteAxe.get());

        smithingRecipe(pWriter, ModItems.PorkchopUpgrade.get(), Items.IRON_SHOVEL, ModItems.Porkchopyonite.get(), RecipeCategory.TOOLS,
                ModItems.PorkchopyoniteShovel.get());

        smithingRecipe(pWriter, ModItems.PorkchopUpgrade.get(), Items.IRON_HOE, ModItems.Porkchopyonite.get(), RecipeCategory.TOOLS,
                ModItems.PorkchopyoniteHoe.get());
    }

    protected static void smeltingRecipes(Consumer<FinishedRecipe> pWriter, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                          ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        cookingRecipes(pWriter, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void blastingRecipes(Consumer<FinishedRecipe> pWriter, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                          ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cookingRecipes(pWriter, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void smokingRecipes(Consumer<FinishedRecipe> pWriter, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                         ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cookingRecipes(pWriter, RecipeSerializer.SMOKING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smoking");
    }

    protected static void cookingRecipes(Consumer<FinishedRecipe> pWriter, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer,
                                         List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime,
                                         String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder builder = SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike));
            builder.save(pWriter, CoolModRemastered.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    protected static void ironFurnaceSmelting(Consumer<FinishedRecipe> pWriter, RecipeCategory pCategory, Item pInput,
                                              ItemStack pOutput, float pExperience, String pGroup) {
        IronFurnaceRecipeBuilder.ironFurnaceRecipe(pCategory, Ingredient.of(pInput), pOutput, pExperience)
                .group(pGroup)
                .unlockedBy(getHasName(pInput), has(pInput))
                .save(pWriter, CoolModRemastered.MOD_ID + ":" + getItemName(pOutput.getItem()) + "_from_iron_furnace");
    }

    protected static void industrialFurnace(Consumer<FinishedRecipe> pWriter, RecipeCategory pCategory, Item pInput,
                                            ItemStack pOutput, float pExperience, String pGroup) {
        IndustrialFurnaceRecipeBuilder.industrialFurnaceRecipe(pCategory, Ingredient.of(pInput), pOutput, pExperience)
                .group(pGroup)
                .unlockedBy(getHasName(pInput), has(pInput))
                .save(pWriter, CoolModRemastered.MOD_ID + ":" + getItemName(pOutput.getItem()) + "_from_" + getItemName(pInput)  + "_industrial");
    }

    protected static void smithingRecipe(Consumer<FinishedRecipe> pWriter, Item pUpgrade, Item pBase, Item pAddition, RecipeCategory pCategory,
                                         Item pResult) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(pUpgrade), Ingredient.of(pBase), Ingredient.of(pAddition),
                        pCategory, pResult).unlocks(getHasName(pBase), has(pBase))
                .save(pWriter, CoolModRemastered.MOD_ID + ":" + getItemName(pResult) + "_from_smithing");
    }
}
