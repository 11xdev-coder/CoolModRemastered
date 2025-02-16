package net.qsef.coolmodremastered.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.qsef.coolmodremastered.CoolModRemastered;
import net.qsef.coolmodremastered.network.ModNetwork;
import net.qsef.coolmodremastered.network.S2C_ExplosionParticlesPacket;
import net.qsef.coolmodremastered.network.S2C_PlayerPushPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public class CoolExplosion extends Explosion {

    private final Level level;
    private final Entity shooter;
    private final double x;
    private final double y;
    private final double z;
    private final float radius;
    public CoolExplosion(Level pLevel, @Nullable Entity pExploder, double x, double y, double z, float pRadius, BlockInteraction pInteraction) {
        super(pLevel, pExploder, null, null, x, y, z, pRadius, false, pInteraction);
        level = pLevel;
        shooter = pExploder;
        this.x = x;
        this.y = y;
        this.z = z;
        radius = pRadius;
    }

    @Override
    public void finalizeExplosion(boolean pSpawnParticles) {
        // iterate through blocks
        List<BlockPos> toBlow = getToBlow();

        for (BlockPos pos : toBlow) {
            BlockState state = level.getBlockState(pos);

            // all blocks except air
            if (!state.isAir()) {
                // remove it
                level.removeBlock(pos, false);

                FallingBlockEntity fallingBlock = EntityType.FALLING_BLOCK.create(level);
                if (fallingBlock != null) {
                    // position
                    fallingBlock.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                    // block state
                    Field blockStateField = getBlockStateField();
                    try {
                        blockStateField.set(fallingBlock, state);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                    // velocity upwards
                    float randomX = (float)(Math.random() - 0.5);
                    float randomZ = (float)(Math.random() - 0.5);
                    fallingBlock.setDeltaMovement(randomX, 1f, randomZ);
                    level.addFreshEntity(fallingBlock);
                }
            }
        }

        blowEntities();

        if (pSpawnParticles) {
            // from server to client
            ModNetwork.sendToClients(new S2C_ExplosionParticlesPacket(x, y, z));
        }
        level.playSound(null, new BlockPos((int)x, (int)y, (int)z), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, 1.0F);
    }

    private void blowEntities() {
        double effectiveRadius = radius * 2f;
        AABB explosionArea = new AABB(
                x - effectiveRadius - 1, y - effectiveRadius - 1, z - effectiveRadius - 1,
                x + effectiveRadius + 1, y + effectiveRadius + 1, z + effectiveRadius + 1);
        Vec3 explosionCenter = new Vec3(x, y, z);

        List<Entity> entitiesToBlow = level.getEntities(null, explosionArea);

        for (Entity entity : entitiesToBlow) {
            if (entity == null) continue;
            if (entity.ignoreExplosion() && !entity.equals(shooter)) continue;

            double distance = Math.sqrt(entity.distanceToSqr(explosionCenter));
            if (distance > effectiveRadius) continue;

            Vec3 direction = entity.position().subtract(explosionCenter).normalize();

            float seenPercent = Explosion.getSeenPercent(explosionCenter, entity);
            double impact = (1 - (distance / effectiveRadius)) * seenPercent;
            double pushStrength = impact;

            if (entity instanceof LivingEntity living) {
                pushStrength = ProtectionEnchantment.getExplosionKnockbackAfterDampener(living, impact);
            }

            Vec3 push = new Vec3(direction.x * pushStrength, direction.y * pushStrength, direction.z * pushStrength);
            if(entity instanceof ServerPlayer player) {
                ModNetwork.sendToPlayer(new S2C_PlayerPushPacket(player.getId(), push.x, push.y * 2f, push.z), player);
            }
            else entity.push(push.x, push.y, push.z);
        }
    }

    private static @NotNull Field getBlockStateField() {
        Field stateField;
        try {
            stateField = FallingBlockEntity.class.getDeclaredField("blockState");
        } catch (NoSuchFieldException e) {
            try {
                stateField = FallingBlockEntity.class.getDeclaredField("f_31946_");
            } catch (NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
        }

        stateField.setAccessible(true);
        return stateField;
    }
}
