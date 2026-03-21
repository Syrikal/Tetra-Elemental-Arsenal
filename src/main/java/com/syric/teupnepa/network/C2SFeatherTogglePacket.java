package com.syric.teupnepa.network;

import com.syric.teupnepa.upgrade_types.FeatherUpgrade;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class C2SFeatherTogglePacket {

    public C2SFeatherTogglePacket() {}

    public C2SFeatherTogglePacket(FriendlyByteBuf buffer) {
        new C2SFeatherTogglePacket();
    }

    public void encode(FriendlyByteBuf buffer) {}

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
//        TeUpNePa.LOGGER.debug("Handling packet");
        ServerPlayer player = contextSupplier.get().getSender();
        if (player == null) return;

        FeatherUpgrade.toggleActive(player.getMainHandItem(), player);
    }
}
