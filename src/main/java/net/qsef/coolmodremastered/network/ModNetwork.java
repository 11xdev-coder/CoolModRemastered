package net.qsef.coolmodremastered.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.*;
import net.qsef.coolmodremastered.CoolModRemastered;

public class ModNetwork {
    public static final SimpleChannel CHANNEL = ChannelBuilder.named(
            new ResourceLocation(CoolModRemastered.MOD_ID, "main")
    ).serverAcceptedVersions((status, version) -> true)
    .clientAcceptedVersions(((status, version) -> true))
    .networkProtocolVersion(1).simpleChannel();

    public static void registerPackets() {
        CHANNEL.messageBuilder(S2C_ExplosionParticlesPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2C_ExplosionParticlesPacket::encode)
                .decoder(S2C_ExplosionParticlesPacket::new)
                .consumerMainThread(S2C_ExplosionParticlesPacket::handle)
                .add();

        CHANNEL.messageBuilder(S2C_PlayerPushPacket.class, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(S2C_PlayerPushPacket::encode)
                .decoder(S2C_PlayerPushPacket::new)
                .consumerMainThread(S2C_PlayerPushPacket::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        CHANNEL.send(msg, PacketDistributor.SERVER.noArg());
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        CHANNEL.send(msg, PacketDistributor.PLAYER.with((player)));
    }

    public static void sendToClients(Object msg) {
        CHANNEL.send(msg, PacketDistributor.ALL.noArg());
    }
}
