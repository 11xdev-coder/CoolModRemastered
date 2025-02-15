package net.qsef.coolmodremastered.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.qsef.coolmodremastered.CoolModRemastered;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public class CoolExplosion extends Explosion {

    private final Level level;
    private final double x;
    private final double y;
    private final double z;
    private final float radius;
    public CoolExplosion(Level pLevel, @Nullable Entity pExploder, double x, double y, double z, float pRadius, BlockInteraction pInteraction) {
        super(pLevel, pExploder, null, null, x, y, z, pRadius, false, pInteraction);
        level = pLevel;
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

        if (pSpawnParticles) {
            if (!(this.radius < 2.0F)) {
                Minecraft.getInstance().level.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 0, 0, 0);
            } else {
                Minecraft.getInstance().level.addParticle(ParticleTypes.EXPLOSION, x, y, z, 0, 0, 0);

            }
        }
        level.playSound(null, new BlockPos((int)x, (int)y, (int)z), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, 1.0F);
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
