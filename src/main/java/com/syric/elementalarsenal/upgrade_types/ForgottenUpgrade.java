package com.syric.elementalarsenal.upgrade_types;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.enums.UpgradeType;
import com.syric.elementalarsenal.util.FindShield;
import com.syric.elementalarsenal.util.ItemIdentificationUtil;
import com.syric.elementalarsenal.util.SendMessageUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@Mod.EventBusSubscriber(
        modid = ElementalArsenal.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class ForgottenUpgrade {

    //Shield damages undergarden mobs
    @SubscribeEvent
    public static void ShieldBlock(ShieldBlockEvent event) {
//        ElementalArsenal.LOGGER.debug("triggered ShieldBlock in ForgottenUpgrade");
        if (!event.getEntity().level().isClientSide
                && event.getDamageSource().getDirectEntity() != null
                && FindShield.getModularShield(event.getEntity()) != null
                && ItemIdentificationUtil.isUpgradedShield(FindShield.getModularShield(event.getEntity()), UpgradeType.FORGOTTEN)) {
            Entity attacker = event.getDamageSource().getDirectEntity();
            LivingEntity defender = event.getEntity();
//            ElementalArsenal.LOGGER.debug("Attacker Namespace: {}", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType())).getNamespace());
            if (Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(attacker.getType())).getNamespace().contains("undergarden") && defender.getRandom().nextFloat() < 0.5) {
                attacker.hurt(attacker.damageSources().thorns(defender), defender.getRandom().nextFloat() * 4 + 2);
                FindShield.getModularShield(defender).hurtAndBreak(1, defender, (x) -> {});
                SendMessageUtil.triggered(UpgradeType.AETHERIC, defender);
            }
        }
    }


}
