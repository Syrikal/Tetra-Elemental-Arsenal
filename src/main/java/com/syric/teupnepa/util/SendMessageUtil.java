package com.syric.teupnepa.util;

import com.syric.teupnepa.enums.UpgradeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class SendMessageUtil {

    public static void sendMessage(Entity entity, String message) {
        if (entity instanceof Player player) {
            player.sendSystemMessage(Component.literal(message));
        }
    }

    public static void triggered(UpgradeType type, Entity entity) {
        if (true && entity.level().isClientSide) { //Togglable
            sendMessage(entity, "Triggered effect of " + type.name + "-upgraded item");
        }
    }

}
