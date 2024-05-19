package net.qsef.coolmodremastered.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.item.ModItems;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public static final List<ItemLike> COOKED_PORKCHOP = List.of(Items.COOKED_PORKCHOP);

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
    }

    protected static void smelting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory,
                                   ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        cooking(pRecipeOutput, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void blasting(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cooking(pRecipeOutput, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void smoking(RecipeOutput pRecipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        cooking(pRecipeOutput, RecipeSerializer.SMOKING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_smoking");
    }

    protected static void cooking(RecipeOutput pRecipeOutput, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult,
                            pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pRecipeOutput,  CoolModRemastered.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
