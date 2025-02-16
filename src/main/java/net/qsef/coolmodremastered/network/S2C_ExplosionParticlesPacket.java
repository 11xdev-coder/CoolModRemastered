package net.qsef.coolmodremastered.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.profiling.jfr.event.NetworkSummaryEvent;
import net.minecraftforge.event.network.CustomPayloadEvent;

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

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            assert mc.level != null;
            mc.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 0, 0, 0);
        });
        context.setPacketHandled(true);
    }
}
