package net.qsef.coolmodremastered.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2C_ExplosionParticlesPacket {
    public final double x,y,z;

    public S2C_ExplosionParticlesPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public S2C_ExplosionParticlesPacket(FriendlyByteBuf buffer) {
        this(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void encode(S2C_ExplosionParticlesPacket packet, FriendlyByteBuf buffer) {
        buffer.writeDouble(packet.x);
        buffer.writeDouble(packet.y);
        buffer.writeDouble(packet.z);
    }

    public static boolean handle(S2C_ExplosionParticlesPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // ensure client-side
            if (context.getDirection().getReceptionSide().isClient()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.level != null) {
                    mc.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, packet.x, packet.y, packet.z, 0, 0, 0);
                }
            }
        });
        return true;
    }
}
