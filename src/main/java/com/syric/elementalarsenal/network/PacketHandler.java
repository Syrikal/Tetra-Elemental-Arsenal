package com.syric.elementalarsenal.network;

import com.syric.elementalarsenal.ElementalArsenal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(
            ResourceLocation.fromNamespaceAndPath(ElementalArsenal.MODID, "main"))
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
        INSTANCE.messageBuilder(S2CArrowTagPacket.class, NetworkDirection.PLAY_TO_CLIENT.ordinal())
                .encoder(S2CArrowTagPacket::encode)
                .decoder(S2CArrowTagPacket::new)
                .consumerMainThread(S2CArrowTagPacket::handle)
                .add();
    }

    public static void sendToServer(Object packet) { INSTANCE.send(PacketDistributor.SERVER.noArg(), packet); }

    public static void sendWithEntity(Object packet, Entity entity) { INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), packet); }

}
