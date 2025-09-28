package net.qsef.coolmodremastered.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CopperRail extends RailBlock {
    protected static final VoxelShape FLAT_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    protected static final VoxelShape HALF_BLOCK_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    // Acceleration strength (higher = faster acceleration)
    private static final double ACCELERATION_FORCE = 0.06D;
    private static final double MAX_SPEED = 1D; // Maximum speed on this rail

    public CopperRail(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(SHAPE, RailShape.NORTH_SOUTH)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public void onMinecartPass(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        if (level.isClientSide) {
            return;
        }

        Vec3 motion = cart.getDeltaMovement();
        RailShape railShape = state.getValue(SHAPE);

        // Check if cart is moving or stopped
        double horizontalSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

        if (railShape.isAscending()) {
            // For ascending rails, we need much stronger boost
            Vec3 direction = getDirectionFromShape(railShape);

            if (horizontalSpeed < 0.03D) {
                // Strong initial push for stopped carts on slopes
                cart.setDeltaMovement(
                        direction.x * 0.3D,
                        0.2D,  // Strong upward push
                        direction.z * 0.3D
                );
            } else {
                // For moving carts on slopes, maintain strong upward momentum
                double boostMultiplier = 1.15D; // 15% boost per tick

                // Calculate boosted horizontal motion
                double newX = motion.x * boostMultiplier;
                double newZ = motion.z * boostMultiplier;

                // Cap horizontal speed
                double newHorizontalSpeed = Math.sqrt(newX * newX + newZ * newZ);
                if (newHorizontalSpeed > MAX_SPEED) {
                    double scale = MAX_SPEED / newHorizontalSpeed;
                    newX *= scale;
                    newZ *= scale;
                }

                // Strong consistent upward velocity to fight gravity
                // This is the key - we need a strong enough Y velocity
                double upwardVelocity = 0.25D; // Much stronger than before!

                cart.setDeltaMovement(newX, upwardVelocity, newZ);
            }
        } else {
            // Flat rail logic (unchanged)
            if (horizontalSpeed < 0.03D) {
                Vec3 direction = getDirectionFromShape(railShape);
                cart.setDeltaMovement(direction.scale(0.2D));
            } else {
                double accelerationMultiplier = 1.0D + ACCELERATION_FORCE;
                Vec3 newMotion = motion.scale(accelerationMultiplier);

                double newSpeed = Math.sqrt(newMotion.x * newMotion.x + newMotion.z * newMotion.z);
                if (newSpeed > MAX_SPEED) {
                    newMotion = newMotion.scale(MAX_SPEED / newSpeed);
                }

                cart.setDeltaMovement(newMotion.x, motion.y, newMotion.z);
            }
        }
    }

    private Vec3 getDirectionFromShape(RailShape shape) {
        return switch (shape) {
            case NORTH_SOUTH, ASCENDING_SOUTH -> new Vec3(0, 0, 1);
            case ASCENDING_NORTH -> new Vec3(0, 0, -1);
            case EAST_WEST, ASCENDING_EAST -> new Vec3(1, 0, 0);
            case ASCENDING_WEST -> new Vec3(-1, 0, 0);
            default -> new Vec3(0, 0, 1);
        };
    }

    @Override
    public float getRailMaxSpeed(BlockState state, Level level, BlockPos pos, AbstractMinecart cart) {
        return 1f; // Maximum allowed speed on this rail
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        RailShape railshape = state.getValue(SHAPE);
        return railshape.isAscending() ? HALF_BLOCK_AABB : FLAT_AABB;
    }

    @Override
    public boolean canMakeSlopes(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    public boolean isFlexibleRail(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }
}