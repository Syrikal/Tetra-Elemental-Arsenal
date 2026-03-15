package com.syric.teupnepa.network;

import com.syric.teupnepa.TeUpNePa;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            ResourceLocation.fromNamespaceAndPath(TeUpNePa.MODID, "main"))
            .serverAcceptedVersions((version) -> true)
            .clientAcceptedVersions(version -> true)
            .networkProtocolVersion(() -> "1")
            .simpleChannel();

    public static void register() {
        INSTANCE.messageBuilder(C2SFeatherTogglePacket.class, NetworkDirection.PLAY_TO_SERVER.ordinal())
                .encoder(C2SFeatherTogglePacket::encode)
                .decoder(C2SFeatherTogglePacket::new)
                .consumerMainThread(C2SFeatherTogglePacket::handle)
                .add();
    }

    public static void sendToServer(Object packet) {
        INSTANCE.send(PacketDistributor.SERVER.noArg(), packet);
    }

}
