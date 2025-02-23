package net.qsef.coolmodremastered.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.qsef.coolmodremastered.item.ModArmorMaterials;

import javax.annotation.Nullable;
import java.util.List;

public class SteelArmorItem extends net.minecraft.world.item.ArmorItem {
    public SteelArmorItem(ArmorMaterial pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        if (this.getMaterial() instanceof ModArmorMaterials material) {
            tooltip.add(Component.translatable("tooltip.coolmodremastered.fire_resistance", material.getFireResistance())
                    .withStyle(ChatFormatting.BLUE));

            tooltip.add(Component.translatable("tooltip.coolmodremastered.blast_resistance", material.getBlastResistance())
                    .withStyle(ChatFormatting.BLUE));
        }
    }
}
