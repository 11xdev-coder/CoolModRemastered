package net.qsef.coolmodremastered.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.profiling.jfr.event.NetworkSummaryEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.network.CustomPayloadEvent;

import java.util.function.Supplier;

public class S2C_PlayerPushPacket {
    public final int entityId;
    public final double pushX, pushY, pushZ;

    public S2C_PlayerPushPacket(int entityId, double pushX, double pushY, double pushZ) {
        this.entityId = entityId;
        this.pushX = pushX;
        this.pushY = pushY;
        this.pushZ = pushZ;
    }

    public S2C_PlayerPushPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    public static void encode(S2C_PlayerPushPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeDouble(packet.pushX);
        buffer.writeDouble(packet.pushY);
        buffer.writeDouble(packet.pushZ);
    }

    public void handle(CustomPayloadEvent.Context context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            assert mc.level != null;

            Entity entity = mc.level.getEntity(entityId);
            assert entity != null;
            entity.push(pushX, pushY, pushZ);
        });
        context.setPacketHandled(true);
    }
}
