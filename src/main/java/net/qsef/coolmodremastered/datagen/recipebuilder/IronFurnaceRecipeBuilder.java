package net.qsef.coolmodremastered.datagen.recipebuilder;

import com.google.gson.JsonObject;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.blockstates.Condition;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.qsef.coolmodremastered.item.ModItems;
import net.qsef.coolmodremastered.recipe.IronFurnaceRecipe;
import net.qsef.coolmodremastered.recipe.ModRecipes;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class IronFurnaceRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Ingredient input;
    private final ItemStack output;
    private final float experience;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap();
    @Nullable
    private String group;
    @Nullable
    private ICondition condition;

    public IronFurnaceRecipeBuilder(RecipeCategory category, Ingredient input, ItemStack output, float experience) {
        this.category = category;
        this.input = input;
        this.output = output;
        this.experience = experience;
    }

    public static IronFurnaceRecipeBuilder ironFurnaceRecipe(RecipeCategory category, Ingredient input, ItemStack output, float experience) {
        return new IronFurnaceRecipeBuilder(category, input, output, experience);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String s) {
        this.group = s;
        return this;
    }

    @Override
    public Item getResult() {
        return this.output.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation resourceLocation) {
        this.ensureValid(resourceLocation);
        Advancement.Builder advBuilder = recipeOutput.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceLocation))
                .rewards(AdvancementRewards.Builder.recipe(resourceLocation))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advBuilder::addCriterion);

        String group = this.group == null ? "" : this.group;
        AdvancementHolder adv = advBuilder.build(resourceLocation.withPrefix("recipes/" + this.category.getFolderName() + "/"));
        RecipeSerializer<?> serializer = ModRecipes.IronFurnaceSerializer.get();
        recipeOutput.accept(new Result(resourceLocation, group, this.input, this.output, this.experience, adv, serializer, this.condition));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient input;
        private final ItemStack output;
        private final float experience;
        private final AdvancementHolder advancementHolder;
        private final RecipeSerializer<?> serializer;
        @Nullable
        private final ICondition condition;

        public Result(ResourceLocation id, String group, Ingredient input, ItemStack output, float experience,
                      AdvancementHolder advancementHolder, RecipeSerializer<?> serializer, @Nullable ICondition condition) {
            this.id = id;
            this.group = group;
            this.input = input;
            this.output = output;
            this.experience = experience;
            this.advancementHolder = advancementHolder;
            this.serializer = serializer;
            this.condition = condition;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            if(this.condition != null) {
                jsonObject.addProperty("condition", this.condition.toString());
            }
            if(this.group != null) {
                jsonObject.addProperty("group", this.group);
            }

            jsonObject.add("input", this.input.toJson(false));

            JsonObject resultJson = new JsonObject();
            resultJson.addProperty("id", BuiltInRegistries.ITEM.getKey(this.output.getItem()).toString());
            resultJson.addProperty("Count", this.output.getCount());
            jsonObject.add("output", resultJson);

            jsonObject.addProperty("experience", this.experience);
        }

        @Override
        public ResourceLocation id() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> type() {
            return this.serializer;
        }

        @Nullable
        @Override
        public AdvancementHolder advancement() {
            return this.advancementHolder;
        }
    }
}
