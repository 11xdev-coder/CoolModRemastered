package net.qsef.coolmodremastered.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.block.entity.IndustrialFurnaceBlockEntity;
import org.jetbrains.annotations.Nullable;

public class IndustrialFurnaceRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;
    private final float experience;

    public static final RecipeType<IndustrialFurnaceRecipe> RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return CoolModRemastered.MOD_ID + ":industrial";
        }
    };

    public IndustrialFurnaceRecipe(Ingredient input, ItemStack output, float experience) {
        this.id = null;
        this.input = input;
        this.output = output;
        this.experience = experience;
    }

    public IndustrialFurnaceRecipe(ResourceLocation id, Ingredient input, ItemStack output, float experience) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.experience = experience;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }
        return input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.IndustrialFurnace.get());
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.IndustrialFurnaceSerializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RECIPE_TYPE;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public float getExperience() {
        return experience;
    }

    public static class Serializer implements RecipeSerializer<IndustrialFurnaceRecipe> {
        @Override
        public IndustrialFurnaceRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            String group = GsonHelper.getAsString(pSerializedRecipe, "group", "");

            // Read input ingredient from "input" field
            JsonElement inputJsonElement = GsonHelper.getAsJsonObject(pSerializedRecipe, "input");
            Ingredient inputIngredient = Ingredient.fromJson(inputJsonElement);

            // Read output item stack from "output" field
            JsonObject outputJson = GsonHelper.getAsJsonObject(pSerializedRecipe, "output");
            ResourceLocation itemLocation = new ResourceLocation(GsonHelper.getAsString(outputJson, "id"));
            Item outputItem = BuiltInRegistries.ITEM.getOptional(itemLocation)
                    .orElseThrow(() -> new JsonSyntaxException("Unknown item '" + itemLocation + "'"));
            int count = GsonHelper.getAsInt(outputJson, "Count", 1);
            ItemStack outputStack = new ItemStack(outputItem, count);

            float experience = GsonHelper.getAsFloat(pSerializedRecipe, "experience", 0.0F);

            return new IndustrialFurnaceRecipe(pRecipeId, inputIngredient, outputStack, experience);
        }

        @Override
        public @Nullable IndustrialFurnaceRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf friendlyByteBuf) {
            Ingredient input = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack output = friendlyByteBuf.readItem();
            float experience = friendlyByteBuf.readFloat();

            return new IndustrialFurnaceRecipe(pRecipeId, input, output, experience);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, IndustrialFurnaceRecipe pRecipe) {
            pRecipe.getInput().toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.getOutput(), false);
            pBuffer.writeFloat(pRecipe.getExperience());
        }
    }
}
