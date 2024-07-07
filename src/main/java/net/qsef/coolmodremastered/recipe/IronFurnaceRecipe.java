package net.qsef.coolmodremastered.recipe;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

public class IronFurnaceRecipe implements Recipe<SimpleContainer> {
    private final Ingredient input;
    private final ItemStack output;
    private final float experience;

    public static final RecipeType<IronFurnaceRecipe> RECIPE_TYPE = new RecipeType<>(){};

    public IronFurnaceRecipe(Ingredient input, ItemStack output, float experience) {
        this.input = input;
        this.output = output;
        this.experience = experience;
    }

    @Override
    public boolean matches(SimpleContainer simpleContainer, Level level) {
        if(level.isClientSide()) return false;

        return input.test(simpleContainer.getItem(0)); // 0 is our input slot
    }

    @Override
    public ItemStack assemble(SimpleContainer simpleContainer, RegistryAccess registryAccess) {
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
        public Codec<IronFurnaceRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    Ingredient.CODEC.fieldOf("input").forGetter(IronFurnaceRecipe::getInput),
                    ItemStack.CODEC.fieldOf("output").forGetter(IronFurnaceRecipe::getOutput),
                    Codec.FLOAT.fieldOf("experience").forGetter(IronFurnaceRecipe::getExperience)
            ).apply(instance, IronFurnaceRecipe::new));
        }

        @Override
        public @Nullable IronFurnaceRecipe fromNetwork(FriendlyByteBuf friendlyByteBuf) {
            Ingredient input = Ingredient.fromNetwork(friendlyByteBuf);
            float experience = friendlyByteBuf.readFloat();

            ItemStack output = friendlyByteBuf.readItem();

            return new IronFurnaceRecipe(input, output, experience);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, IronFurnaceRecipe ironFurnaceRecipe) {
            ironFurnaceRecipe.getInput().toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeFloat(ironFurnaceRecipe.getExperience());

            friendlyByteBuf.writeItem(ironFurnaceRecipe.getOutput());
        }
    }
}
