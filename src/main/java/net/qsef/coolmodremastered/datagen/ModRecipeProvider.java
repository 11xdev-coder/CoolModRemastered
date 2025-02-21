package net.qsef.coolmodremastered.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.datagen.recipebuilder.IndustrialFurnaceRecipeBuilder;
import net.qsef.coolmodremastered.datagen.recipebuilder.IronFurnaceRecipeBuilder;
import net.qsef.coolmodremastered.item.ModItems;
import net.qsef.coolmodremastered.recipe.IronFurnaceRecipe;
import net.qsef.coolmodremastered.util.ModTags;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public static final List<ItemLike> COOKED_PORKCHOP = List.of(Items.COOKED_PORKCHOP);
    public static final List<ItemLike> IRON_INGOT = List.of(Items.IRON_INGOT);

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // roasted porkchop smelting, smoking
        smelting(recipeOutput, COOKED_PORKCHOP, RecipeCategory.FOOD,
                ModItems.RoastedPorkchop.get(), 0F, 100, "roasted_porkchop");
        smoking(recipeOutput, COOKED_PORKCHOP, RecipeCategory.FOOD,
                ModItems.RoastedPorkchop.get(), 0F, 50, "roasted_porkchop");

        // blasting: steel ingot
        blasting(recipeOutput, IRON_INGOT, RecipeCategory.MISC, ModItems.SteelIngot.get(), 1f, 300, "steel_ingot");
        industrialFurnace(recipeOutput, RecipeCategory.MISC, Items.IRON_INGOT, new ItemStack(ModItems.SteelIngot.get(), 1), 1f, "steel_ingot");

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
                    .save(recipeOutput);

        // porkchop block
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PorkchopBlock.get())
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ModItems.Porkchopyonite.get())
                .unlockedBy(getHasName(ModItems.Porkchopyonite.get()), has(ModItems.Porkchopyonite.get())) // unlock the recipe when player has porkchopyonite
                .save(recipeOutput);

        // pork burger
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.PorkBurger.get())
                .pattern(" x ")
                .pattern(" y ")
                .pattern(" x ")
                .define('x', Items.BREAD)
                .define('y', Items.COOKED_PORKCHOP)
                .unlockedBy(getHasName(Items.BREAD), has(Items.BREAD))
                .unlockedBy(getHasName(Items.COOKED_PORKCHOP), has(Items.COOKED_PORKCHOP))
                .save(recipeOutput);

        // porking station
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PorkingStation.get())
                .pattern("xyx")
                .pattern("y y")
                .pattern("xyx")
                .define('x', ModItems.Porkchopyonite.get())
                .define('y', ModItems.PigSoul.get())
                .unlockedBy(getHasName(ModItems.Porkchopyonite.get()), has(ModItems.Porkchopyonite.get()))
                .unlockedBy(getHasName(ModItems.PigSoul.get()), has(ModItems.PigSoul.get()))
                .save(recipeOutput);

        // porkchopyonite from porkchop block
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.Porkchopyonite.get(), 9)
                .requires(ModBlocks.PorkchopBlock.get())
                .unlockedBy(getHasName(ModBlocks.PorkchopBlock.get()), has(ModBlocks.PorkchopBlock.get()))
                .save(recipeOutput);

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
                .save(recipeOutput);

        // porkchopyonite from cooked porkchop
        ironFurnaceSmelting(recipeOutput, RecipeCategory.FOOD, Items.COOKED_PORKCHOP,
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
                .save(recipeOutput);

        // porkchop tools
        smithingRecipe(recipeOutput, ModItems.PorkchopUpgrade.get(), Items.IRON_SWORD, ModItems.Porkchopyonite.get(), RecipeCategory.COMBAT,
                ModItems.PorkchopyoniteSword.get());

        smithingRecipe(recipeOutput, ModItems.PorkchopUpgrade.get(), Items.IRON_PICKAXE, ModItems.Porkchopyonite.get(), RecipeCategory.TOOLS,
                ModItems.PorkchopyonitePickaxe.get());

        smithingRecipe(recipeOutput, ModItems.PorkchopUpgrade.get(), Items.IRON_AXE, ModItems.Porkchopyonite.get(), RecipeCategory.TOOLS,
                ModItems.PorkchopyoniteAxe.get());

        smithingRecipe(recipeOutput, ModItems.PorkchopUpgrade.get(), Items.IRON_SHOVEL, ModItems.Porkchopyonite.get(), RecipeCategory.TOOLS,
                ModItems.PorkchopyoniteShovel.get());

        smithingRecipe(recipeOutput, ModItems.PorkchopUpgrade.get(), Items.IRON_HOE, ModItems.Porkchopyonite.get(), RecipeCategory.TOOLS,
                ModItems.PorkchopyoniteHoe.get());
    }

    protected static void smelting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                   ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        cooking(pRecipeOutput, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void blasting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                   ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cooking(pRecipeOutput, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void smoking(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                  ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cooking(pRecipeOutput, RecipeSerializer.SMOKING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smoking");
    }

    protected static void cooking(RecipeOutput pRecipeOutput, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer,
                                  List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime,
                                  String pGroup, String pRecipeName) {
        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder builder = SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike));
            builder.save(pRecipeOutput, CoolModRemastered.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    protected static void ironFurnaceSmelting(RecipeOutput pRecipeOutput, RecipeCategory pCategory, Item pInput,
                                              ItemStack pOutput, float pExperience, String pGroup) {
        IronFurnaceRecipeBuilder.ironFurnaceRecipe(pCategory, Ingredient.of(pInput), pOutput, pExperience)
                .group(pGroup)
                .unlockedBy(getHasName(pInput), has(pInput))
                .save(pRecipeOutput, CoolModRemastered.MOD_ID + ":" + getItemName(pOutput.getItem()) + "_from_iron_furnace");
    }

    protected static void industrialFurnace(RecipeOutput pRecipeOutput, RecipeCategory pCategory, Item pInput,
                                              ItemStack pOutput, float pExperience, String pGroup) {
        IndustrialFurnaceRecipeBuilder.industrialFurnaceRecipe(pCategory, Ingredient.of(pInput), pOutput, pExperience)
                .group(pGroup)
                .unlockedBy(getHasName(pInput), has(pInput))
                .save(pRecipeOutput, CoolModRemastered.MOD_ID + ":" + getItemName(pOutput.getItem()) + "_from_industrial");
    }

    protected static void smithingRecipe(RecipeOutput pRecipeOutput, Item pUpgrade, Item pBase, Item pAddition, RecipeCategory pCategory,
                                         Item pResult) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(pUpgrade), Ingredient.of(pBase), Ingredient.of(pAddition),
                pCategory, pResult).unlocks(getHasName(pBase), has(pBase))
                .save(pRecipeOutput, CoolModRemastered.MOD_ID + ":" + getItemName(pResult) + "_from_smithing");
    }
}