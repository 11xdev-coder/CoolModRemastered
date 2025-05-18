package net.qsef.coolmodremastered.recipe;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class IronFurnaceRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;
    private final float experience;

    public static final RecipeType<IronFurnaceRecipe> RECIPE_TYPE = new RecipeType<>(){
        @Override
        public String toString() {
            return CoolModRemastered.MOD_ID + ":iron_furnace_smelting";
        }
    };

    public IronFurnaceRecipe(Ingredient input, ItemStack output, float experience) {
        this.id = null;
        this.input = input;
        this.output = output;
        this.experience = experience;
    }

    public IronFurnaceRecipe(ResourceLocation id, Ingredient input, ItemStack output, float experience) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.experience = experience;
    }

    @Override
    public boolean matches(Container simpleContainer, Level level) {
        if (level.isClientSide()) {
            return false;
        }
        return input.test(simpleContainer.getItem(0)); // 0 is our input slot
    }

    @Override
    public ItemStack assemble(Container simpleContainer, RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return output.copy();
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.IronFurnace.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.IronFurnaceSerializer.get();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeType<?> getType() {
        return RECIPE_TYPE;
    }

    public Ingredient getInput() {
        return this.input;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public float getExperience() {
        return this.experience;
    }

    public static class Serializer implements RecipeSerializer<IronFurnaceRecipe> {
        @Override
        public IronFurnaceRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
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

            return new IronFurnaceRecipe(pRecipeId, inputIngredient, outputStack, experience);
        }

        @Override
        public @Nullable IronFurnaceRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf friendlyByteBuf) {
            Ingredient input = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack output = friendlyByteBuf.readItem();
            float experience = friendlyByteBuf.readFloat();

            return new IronFurnaceRecipe(pRecipeId, input, output, experience);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, IronFurnaceRecipe pRecipe) {
            pRecipe.getInput().toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.getOutput(), false);
            pBuffer.writeFloat(pRecipe.getExperience());
        }
    }
}
