package net.qsef.coolmodremastered.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.util.ModTags;

import java.util.List;

public class ModToolTiers {
    public static final Tier PORKCHOPYONITE_TIER = TierSortingRegistry.registerTier(
        new ForgeTier(2, 300, 7f, 2.5f, 21,
                ModTags.Blocks.NEEDS_PORKCHOPYONITE_TOOL, () -> Ingredient.of(ModItems.Porkchopyonite.get())),
            new ResourceLocation(CoolModRemastered.MOD_ID, "porkchopyonite"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND)
    );
}
