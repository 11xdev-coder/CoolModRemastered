package net.qsef.coolmodremastered.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import net.qsef.coolmodremastered.block.entity.IndustrialFurnaceBlockEntity;
import org.jetbrains.annotations.Nullable;

public class IndustrialFurnaceRecipe implements Recipe<Container> {
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
        this.input = input;
        this.output = output;
        this.experience = experience;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
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
        public Codec<IndustrialFurnaceRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("input").forGetter(IndustrialFurnaceRecipe::getInput),
                    ItemStack.CODEC.fieldOf("output").forGetter(IndustrialFurnaceRecipe::getOutput),
                    Codec.FLOAT.fieldOf("experience").forGetter(IndustrialFurnaceRecipe::getExperience)
            ).apply(instance, IndustrialFurnaceRecipe::new));
        }

        @Override
        public @Nullable IndustrialFurnaceRecipe fromNetwork(FriendlyByteBuf pBuffer) {
            Ingredient input = Ingredient.fromNetwork(pBuffer);
            ItemStack output = pBuffer.readItem();
            float experience = pBuffer.readFloat();

            return new IndustrialFurnaceRecipe(input, output, experience);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, IndustrialFurnaceRecipe pRecipe) {
            pRecipe.getInput().toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.getOutput(), false);
            pBuffer.writeFloat(pRecipe.getExperience());
        }
    }
}
