package net.qsef.coolmodremastered.entity.custom;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.qsef.coolmodremastered.entity.ModEntities;
import net.qsef.coolmodremastered.util.CoolExplosion;

public class RocketEntity extends ThrowableProjectile {
    private LivingEntity shooter;

    public RocketEntity(EntityType<RocketEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RocketEntity(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.Rocket.get(), pShooter, pLevel);
        shooter = pShooter;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        Vec3 velocity = this.getDeltaMovement();
        if (velocity.lengthSqr() > 0) {
            // Compute yaw based on x and z; subtract 90 degrees to align with the model’s forward direction.
            float yaw = (float)(Math.atan2(velocity.x, velocity.z) * (180F / Math.PI)) - 90F;
            // Compute horizontal speed for pitch calculation
            float horizontalSpeed = (float)Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);
            float pitch = (float)(Math.atan2(velocity.y, horizontalSpeed) * (180F / Math.PI) - 180f);
            setYRot(yaw);
            setXRot(pitch);
        }
    }

    private void explode() {
        Level level = level();
        // server-side
        if (!level.isClientSide) {
            CoolExplosion explosion = new CoolExplosion(level,  shooter, getX(), getY(), getZ(), 2f, Explosion.BlockInteraction.DESTROY);
            explosion.explode();
            explosion.finalizeExplosion(true);
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        explode();
    }
}
