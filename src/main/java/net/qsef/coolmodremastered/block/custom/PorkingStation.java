package net.qsef.coolmodremastered.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.qsef.coolmodremastered.block.entity.IronFurnaceBlockEntity;
import net.qsef.coolmodremastered.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PorkingStation extends Block {
    public static final boolean haveRegistered = false;

    public PorkingStation(Properties pProperties) {
        super(pProperties);
    }

    public void registerRecipes() {
        if(!haveRegistered) {
            final PorkingRecipe PIG_RELIC_RECIPE = new PorkingRecipe(ModItems.Porkchopyonite.get(),
                    ModItems.PigSoul.get(), 12, 6, ModItems.PigRelic.get());

            recipes.add(PIG_RELIC_RECIPE);
        }
    }

    public final List<PorkingRecipe> recipes = new ArrayList<>();


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        registerRecipes();

        BlockPos checkingPos = pPos.above();

        AABB aabb = new AABB(checkingPos);
        List<ItemEntity> items = pLevel.getEntitiesOfClass(ItemEntity.class, aabb);


        for (PorkingRecipe recipe : recipes) {
            int count1 = 0;
            int count2 = 0;
            int count3 = 0;
            for (ItemEntity item : items) {
                ItemStack itemStack = item.getItem();

                if(itemStack.getItem() == recipe.ingredient1) {
                    count1 += itemStack.getCount();
                } else if (itemStack.getItem() == recipe.ingredient2) {
                    count2 += itemStack.getCount();
                } else if (itemStack.getItem() == recipe.ingredient3) {
                    count3 += itemStack.getCount();
                }
            }

            if (count1 >= recipe.maxCount1 && count2 >= recipe.maxCount2 && (recipe.ingredient3 == null || count3 >= recipe.maxCount3)) {
                for (ItemEntity item : items) {
                    ItemStack itemStack = item.getItem();

                    // check for each ingredient
                    if (itemStack.getItem() == recipe.ingredient1) {
                        // choose  the minimum between maxCount or itemstack count
                        int removeCount = Math.min(recipe.maxCount1, itemStack.getCount());
                        itemStack.shrink(removeCount);
                        count1 -= removeCount;
                    } else if (itemStack.getItem() == recipe.ingredient2) {
                        int removeCount = Math.min(recipe.maxCount2, itemStack.getCount());
                        itemStack.shrink(removeCount);
                        count2 -= removeCount;
                    } else if (recipe.ingredient3 != null && itemStack.getItem() == recipe.ingredient3) {
                        int removeCount = Math.min(recipe.maxCount3, itemStack.getCount());
                        itemStack.shrink(removeCount);
                        count3 -= removeCount;
                    }

                    // remove empty item stacks
                    if (itemStack.isEmpty()) {
                        item.remove(Entity.RemovalReason.DISCARDED);
                    }

                    if (count1 <= 0 && count2 <= 0 && count3 <= 0) {
                        break;
                    }
                }

                if (!pLevel.isClientSide) {
                    // spawn result item
                    ItemEntity resultItem = new ItemEntity(pLevel, checkingPos.getX() + 0.5, checkingPos.getY(), checkingPos.getZ() + 0.5, new ItemStack(recipe.result));
                    pLevel.addFreshEntity(resultItem);
                }
                // spawn particles
                particles(pLevel, checkingPos);
                pLevel.playSound(pPlayer, checkingPos, SoundEvents.UI_BUTTON_CLICK.get(), SoundSource.BLOCKS, 1F, 1.5F);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public void particles(Level level, BlockPos pos) {
        if(level.isClientSide) {
            int num = 15;
            double spread = 2D;
            double upwardSpread = 0.1D;

            double x = pos.getX() + 0.5D;
            double y = pos.getY();
            double z = pos.getZ() + 0.5D;

            for (int i = 0; i < num; i++) {
                double deltaX = (level.random.nextDouble() - 0.5F) * spread;
                double deltaZ = (level.random.nextDouble() - 0.5F) * spread;
                double velocityX = deltaX * 0.4F;
                double velocityZ = deltaZ * 0.4F;

                level.addParticle(ParticleTypes.LARGE_SMOKE, x, y, z, velocityX, upwardSpread, velocityZ);
            }

        }
    }

    public static class PorkingRecipe {
        public final Item ingredient1;
        public final Item ingredient2;
        public final Item ingredient3;
        public final int maxCount1;
        public final int maxCount2;
        public final int maxCount3;
        public final Item result;

        public PorkingRecipe(Item ingredient1, Item ingredient2, int count1, int count2, Item result) {
            this(ingredient1, ingredient2, null, count1, count2, 0, result);
        }

        public PorkingRecipe(Item ingredient1, Item ingredient2, Item ingredient3, int count1, int count2, int count3, Item result) {
            this.ingredient1 = ingredient1;
            this.ingredient2 = ingredient2;
            this.ingredient3 = ingredient3;
            this.maxCount1 = count1;
            this.maxCount2 = count2;
            this.maxCount3 = count3;
            this.result = result;
        }
    }
}


