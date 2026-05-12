package com.syric.elementalarsenal.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handleS2CArrowTagPacket(S2CArrowTagPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Entity arrow = level.getEntity(packet.arrowId);
            if (arrow != null) {
                if (packet.arrowType == 0) {
                    arrow.addTag("WaterImbuedArrow");
                } else if (packet.arrowType == 1) {
                    arrow.addTag("FeatherImbuedArrow");
                } else if (packet.arrowType == 2) {
                    arrow.addTag("RadiantImbuedArrow");
                }
            }
        }
    }

}
