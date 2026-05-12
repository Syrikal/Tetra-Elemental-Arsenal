package com.syric.elementalarsenal.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CArrowTagPacket {
    final int arrowId;
    final int arrowType; //0 for water, 1 for feather

    public S2CArrowTagPacket(int id, int type) {
        this.arrowId = id;
        this.arrowType = type;
    }

    public S2CArrowTagPacket(FriendlyByteBuf buffer) {
        this(buffer.readInt(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.arrowId);
        buffer.writeInt(this.arrowType);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {ClientPacketHandler.handleS2CArrowTagPacket(this, contextSupplier);})
        );
        contextSupplier.get().setPacketHandled(true);

    }

}
