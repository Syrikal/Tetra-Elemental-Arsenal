package com.syric.teupnepa.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CArrowTagPacket {
    private final int arrowId;
    private final int arrowType; //0 for water, 1 for feather

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
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Entity arrow = level.getEntity(arrowId);
            if (arrow != null) {
                if (arrowType == 0) {
                    arrow.addTag("WaterUpgradedNetheriteBow");
                } else if (arrowType == 1) {
                    arrow.addTag("FeatherUpgradedNetheriteBow");
                } else if (arrowType == 2) {
                    arrow.addTag("RadiantUpgradedNetheriteBow");
                }
            }
        }
    }

}
