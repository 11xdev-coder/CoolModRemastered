package net.qsef.coolmodremastered.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PigRelic extends Item {

    public static final double FOLLOW_RADIUS = 10.0f;
    public static final List<Pig> followers = new ArrayList<>();
    public static final List<Pig> breeders = new ArrayList<>();

    public PigRelic(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(pInteractionTarget instanceof Pig) {
            Pig pig = (Pig) pInteractionTarget;
            // check if it is adult and not in love
            if (pig.getAge() != 0 || !pig.isInLove()) { // age == 0 - if adult
                pig.setInLove(pPlayer); // breed

                removeFollowGoal(pig);
                followers.remove(pig);
                breeders.add(pig);

                return InteractionResult.SUCCESS;
            }
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }

    // each tick check for item in hand
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(!pLevel.isClientSide && pEntity instanceof Player) { // if client side and holding entity is player
            Player player = (Player)pEntity; // get the player
            BlockPos pos = player.blockPosition(); // get the player block position

            boolean isSelected = player.getMainHandItem().getItem() == this || player.getOffhandItem().getItem() == this;

            if(isSelected) {
                // check in radius
                AABB aabb = new AABB(pos).inflate(FOLLOW_RADIUS); // create aabb
                List<Pig> pigs = pLevel.getEntitiesOfClass(Pig.class, aabb); // find all pigs in aabb

                for (Pig pig : pigs) {
                    // add goal if not in the list
                    if(!followers.contains(pig) && !breeders.contains(pig)) {
                        pig.goalSelector.addGoal(0, new FollowPlayerGoal(pig, player, 1.5D, 5.0F, 2.0F));
                        followers.add(pig);
                    }
                }

                // add resistance
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1, 0, false, false, false));
            } else {
                // remove all pigs if item is not held
                for (Pig pig : followers) {
                    removeFollowGoal(pig);
                }
                followers.clear();
            }

            // remove pigs if out of range
            followers.removeIf(pig -> {
                boolean isOutOfRange = !pig.getBoundingBox().inflate(FOLLOW_RADIUS).intersects(player.getBoundingBox());
                if (isOutOfRange) {
                    removeFollowGoal(pig);
                }
                return isOutOfRange;
            });
            breeders.removeIf(pig -> !pig.isInLove());
        }
    }

    private void removeFollowGoal(Pig pig) {
        pig.goalSelector.getAvailableGoals().stream()
                .filter(prioritizedGoal -> prioritizedGoal.getGoal() instanceof FollowPlayerGoal)
                .findFirst()
                .ifPresent(prioritizedGoal -> pig.goalSelector.removeGoal(prioritizedGoal.getGoal()));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.coolmodremastered.pig_relic.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private static class FollowPlayerGoal extends Goal {
        private final Pig pig;
        private final Player player;
        private final double speedModifier;
        private final float stopDistance;
        private final float startDistance;

        public FollowPlayerGoal(Pig pig, Player player, double speedModifier, float stopDistance, float startDistance) {
            this.pig = pig;
            this.player = player;
            this.speedModifier = speedModifier;
            this.stopDistance = stopDistance;
            this.startDistance = startDistance;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            this.pig.getLookControl().setLookAt(this.player, 10.0F, (float) this.pig.getMaxHeadXRot());
            if (this.pig.distanceToSqr(this.player) >= (double) (this.startDistance * this.startDistance)) {
                this.pig.getNavigation().moveTo(this.player, this.speedModifier);
            }
        }
    }
}
