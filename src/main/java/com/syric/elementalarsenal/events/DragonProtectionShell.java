package com.syric.elementalarsenal.events;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.compat.IFCompat;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;

//This class is safe to load even if Ice & Fire is not present.
@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class DragonProtectionShell {

    @SubscribeEvent
    public static void dragonscaleShieldCheck(LivingHurtEvent event) {
        if (!event.getEntity().level().isClientSide()
                && event.getEntity() instanceof Player
                && (event.getEntity().getMainHandItem().getItem() instanceof ModularShieldItem
                || event.getEntity().getOffhandItem().getItem() instanceof  ModularShieldItem)
                && ModList.get().isLoaded("iceandfire")
                && !ModList.get().isLoaded("amm")) {
            IFCompat.dragonscaleShieldCheck(event);
        }
    }

}
