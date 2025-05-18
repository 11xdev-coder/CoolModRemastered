package net.qsef.coolmodremastered.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.qsef.coolmodremastered.CoolModRemastered;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(CoolModRemastered.MOD_ID, "main"))
            .serverAcceptedVersions((version) -> true) // Server accepts any client version
            .clientAcceptedVersions((version) -> true) // Client accepts any server version
            .networkProtocolVersion(() -> PROTOCOL_VERSION) // Use a supplier for the protocol version
            .simpleChannel();

    private static int messageId = 0; // Counter for message IDs

    // Helper method to get the next message ID
    private static int nextId() {
        return messageId++;
    }

    public static void registerPackets() {
        // Register the S2C_ExplosionParticlesPacket
        CHANNEL.messageBuilder(S2C_ExplosionParticlesPacket.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2C_ExplosionParticlesPacket::encode)
                .decoder(S2C_ExplosionParticlesPacket::new)
                .consumerMainThread(S2C_ExplosionParticlesPacket::handle)
                .add();

        // Register the S2C_PlayerPushPacket
        CHANNEL.messageBuilder(S2C_PlayerPushPacket.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2C_PlayerPushPacket::encode)
                .decoder(S2C_PlayerPushPacket::new)
                .consumerMainThread(S2C_PlayerPushPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG msg) {
        CHANNEL.send(PacketDistributor.SERVER.noArg(), msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void sendToClients(MSG msg) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), msg);
    }
}
