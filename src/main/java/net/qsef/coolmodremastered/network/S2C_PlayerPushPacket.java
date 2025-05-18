package net.qsef.coolmodremastered.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.profiling.jfr.event.NetworkSummaryEvent;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

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

    public static boolean handle(S2C_PlayerPushPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // ensure client-side
            if (context.getDirection().getReceptionSide().isClient()) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.level != null) {
                    Entity entity = mc.level.getEntity(packet.entityId);
                    if (entity != null) {
                        entity.push(packet.pushX, packet.pushY, packet.pushZ);
                    }
                }
            }
        });
        return true;
    }
}
