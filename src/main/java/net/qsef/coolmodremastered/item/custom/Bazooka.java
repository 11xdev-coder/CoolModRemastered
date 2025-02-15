package net.qsef.coolmodremastered.item.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.qsef.coolmodremastered.entity.custom.RocketEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class Bazooka extends Item {
    private static final int CooldownTicks = 15;
    private static final int RecoilRecoveryTime = 400;
    private static final float MaxRecoil = -0.5f;
    private long lastFireTime = 0;

    public Bazooka(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
                // arm offset
                if (arm == HumanoidArm.RIGHT) {
                    poseStack.translate(0.56, -0.52, -0.72);
                } else {
                    poseStack.translate(-0.56, -0.52, -0.72);
                }

                // calculate recoil
                long currentTime = System.currentTimeMillis();
                long timeSinceFiring = currentTime - lastFireTime;
                float recoilProgress = Math.max(0f, 1f - (float) timeSinceFiring / RecoilRecoveryTime);

                if (recoilProgress > 0f) {
                    // apply recoil
                    poseStack.translate(0, recoilProgress * 0.4, recoilProgress * 0.6);

                    // rotate
                    Quaternionf recoilQuat = new Quaternionf().rotateX((float) Math.toRadians(recoilProgress * 50f));
                    poseStack.mulPose(recoilQuat);
                }

                return true;
            }
        });
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);

        if (!pLevel.isClientSide) {
            // server -> cooldown
            if (!pPlayer.getCooldowns().isOnCooldown(this)) {
                // spawn projectile
                RocketEntity rocket = new RocketEntity(pLevel, pPlayer);
                rocket.setPos(pPlayer.getX(), pPlayer.getEyeY() - 0.1f, pPlayer.getZ());

                Vec3 lookDir = pPlayer.getLookAngle();
                rocket.shoot(lookDir.x, lookDir.y, lookDir.z, 1.5f, 0f);
                pLevel.addFreshEntity(rocket);

                pPlayer.getCooldowns().addCooldown(this, CooldownTicks);
            }
        } else {
            // client -> recoil
            if (!pPlayer.getCooldowns().isOnCooldown(this)) {
                lastFireTime = System.currentTimeMillis();
            }
        }

        return InteractionResultHolder.sidedSuccess(itemStack, pLevel.isClientSide);
    }
}
