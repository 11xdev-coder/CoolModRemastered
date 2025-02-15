package net.qsef.coolmodremastered.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

public class CoolExplosion extends Explosion {

    private final Level level;
    public CoolExplosion(Level pLevel, @Nullable Entity pExploder, double x, double y, double z, float pRadius, BlockInteraction pInteraction) {
        super(pLevel, pExploder, null, null, x, y, z, pRadius, false, pInteraction);
        level = pLevel;
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
            level.addParticle(net.minecraft.core.particles.ParticleTypes.EXPLOSION_EMITTER,
                    getPosition().x, getPosition().y, getPosition().z,
                    0.0D, 0.0D, 0.0D);
        }

        level.playSound(null, new BlockPos((int)getPosition().x, (int)getPosition().y, (int)getPosition().z),
                SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4f, 1f);

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
