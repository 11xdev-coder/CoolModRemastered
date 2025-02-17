package net.qsef.coolmodremastered;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;

public class PlayerOnItemPickup {
    public static final Map<Item, ResourceLocation> ITEM_TO_ACHIEVEMENT = new HashMap<>();

    static {
        ITEM_TO_ACHIEVEMENT.put(Items.IRON_INGOT, new ResourceLocation("coolmodremastered", "steel_ingot_from_blasting_iron_ingot"));
    }

    private static Optional<ResourceLocation> getAchievementForItem(Item item) {
        return Optional.ofNullable(ITEM_TO_ACHIEVEMENT.get(item));
    }

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            ItemStack stack = event.getStack();

            Optional<ResourceLocation> optionalId = getAchievementForItem(stack.getItem());
            if (optionalId.isPresent()) {
                ResourceLocation id = optionalId.get();

                Optional<? extends RecipeHolder<?>> optionalRecipe = serverPlayer.level().getRecipeManager().byKey(id);
                if (optionalRecipe.isPresent()) {
                    RecipeHolder<?> recipe = optionalRecipe.get();
                    serverPlayer.awardRecipes(Set.of(recipe));
                }
            }
        }
    }
}
