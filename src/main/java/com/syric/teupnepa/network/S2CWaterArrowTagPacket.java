package com.syric.teupnepa.network;

import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.events.FeatherUpgrade;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.MalformedInputException;
import java.util.function.Supplier;

public class S2CWaterArrowTagPacket {
    private final int arrowId;

    public S2CWaterArrowTagPacket(int id) {
        this.arrowId = id;
    }

    public S2CWaterArrowTagPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.arrowId);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Entity arrow = level.getEntity(arrowId);
            if (arrow != null) {
                arrow.addTag("WaterUpgradedNetheriteBow");
            }
        }
    }

}
