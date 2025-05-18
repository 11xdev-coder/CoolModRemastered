package net.qsef.coolmodremastered.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IndustrialFuelItem extends Item {
    public int burnTime = 0;
    public IndustrialFuelItem(Properties pProperties, int burnTime) {
        super(pProperties);
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return burnTime;
    }

    public int getIndustrialBurnTime() {
        return burnTime;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        MutableComponent info = Component.translatable("tooltip.coolmodremastered.industrial_fuel_item.prefix").withStyle(ChatFormatting.DARK_AQUA)
                .append(Component.literal(String.valueOf(burnTime)).withStyle(ChatFormatting.GOLD));

        pTooltipComponents.add(info);
    }
}
