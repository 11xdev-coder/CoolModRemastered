package net.qsef.coolmodremastered.datagen.recipebuilder;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.qsef.coolmodremastered.recipe.ModRecipes;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class IndustrialFurnaceRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Ingredient input;
    private final ItemStack output;
    private final float experience;
    private final Map<String, CriterionTriggerInstance> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;
    @Nullable
    private ICondition condition; // For Forge conditional recipes

    public IndustrialFurnaceRecipeBuilder(RecipeCategory category, Ingredient input, ItemStack output, float experience) {
        this.category = category;
        this.input = input;
        this.output = output;
        this.experience = experience;
    }

    public static IndustrialFurnaceRecipeBuilder industrialFurnaceRecipe(RecipeCategory category, Ingredient input, ItemStack output, float experience) {
        return new IndustrialFurnaceRecipeBuilder(category, input, output, experience);
    }

    @Override
    public RecipeBuilder unlockedBy(String name, CriterionTriggerInstance criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.output.getItem();
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.ensureValid(pRecipeId);
        Advancement.Builder advancementBuilder = Advancement.Builder.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId))
                .requirements(RequirementsStrategy.OR); // Use RequirementsStrategy for OR logic

        this.criteria.forEach(advancementBuilder::addCriterion);

        String effectiveGroup = this.group == null ? "" : this.group;

        // The advancement ID is typically derived from the recipe ID.
        ResourceLocation advancementId = new ResourceLocation(pRecipeId.getNamespace(), "recipes/" + this.category.getFolderName() + "/" + pRecipeId.getPath());

        RecipeSerializer<?> serializer = ModRecipes.IndustrialFurnaceSerializer.get();

        pFinishedRecipeConsumer.accept(new Result(
                pRecipeId,
                effectiveGroup,
                this.input,
                this.output,
                this.experience,
                advancementBuilder, // Pass the builder
                advancementId,    // Pass the advancement ID
                serializer,
                this.condition
        ));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId + ". Add at least one criterion via #unlockedBy.");
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient input;
        private final ItemStack output;
        private final float experience;
        private final Advancement.Builder advancementBuilder; // Store the builder
        private final ResourceLocation advancementId;       // Store the advancement ID
        private final RecipeSerializer<?> serializer;
        @Nullable
        private final ICondition condition;

        public Result(ResourceLocation id, String group, Ingredient input, ItemStack output, float experience,
                      Advancement.Builder advancementBuilder, ResourceLocation advancementId,
                      RecipeSerializer<?> serializer, @Nullable ICondition condition) {
            this.id = id;
            this.group = group;
            this.input = input;
            this.output = output;
            this.experience = experience;
            this.advancementBuilder = advancementBuilder;
            this.advancementId = advancementId;
            this.serializer = serializer;
            this.condition = condition;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            // Forge conditional recipe support
            if (this.condition != null) {
                jsonObject.add("conditions", net.minecraftforge.common.crafting.CraftingHelper.serialize(this.condition));
            }

            if (this.group != null && !this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            jsonObject.add("ingredient", this.input.toJson());

            JsonObject resultJson = new JsonObject();
            // In 1.20.1, item is the key for the item ID string.
            resultJson.addProperty("item", BuiltInRegistries.ITEM.getKey(this.output.getItem()).toString());
            if (this.output.getCount() > 1) {
                resultJson.addProperty("count", this.output.getCount());
            }
            jsonObject.add("result", resultJson);

            jsonObject.addProperty("experience", this.experience);
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            // Build the advancement only when it's requested for serialization
            if (this.advancementBuilder != null) {
                return this.advancementBuilder.serializeToJson();
            }
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
