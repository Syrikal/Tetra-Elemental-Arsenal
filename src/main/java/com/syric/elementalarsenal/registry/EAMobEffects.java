package com.syric.elementalarsenal.registry;

import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.effects.ChargedEffect;
import com.syric.elementalarsenal.effects.DimensionalAnchorEffect;
import com.syric.elementalarsenal.effects.ShockedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.UUID;

public class EAMobEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, ElementalArsenal.MODID);

    public static final RegistryObject<MobEffect> DIMENSIONAL_ANCHOR_EFFECT = MOB_EFFECTS.register("dimensional_anchor", () -> new DimensionalAnchorEffect(MobEffectCategory.HARMFUL, 0x84299a));
    public static final RegistryObject<MobEffect> CHARGED_EFFECT = MOB_EFFECTS.register("charged", () -> new ChargedEffect(MobEffectCategory.BENEFICIAL, 0x40ffff)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, UUID.randomUUID().toString(), 0.2F, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, UUID.randomUUID().toString(), 0.1F, AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<MobEffect> SHOCKED_EFFECT = MOB_EFFECTS.register("shocked", () -> new ShockedEffect(MobEffectCategory.HARMFUL, 0x40ffff)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, UUID.randomUUID().toString(), -0.2F, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, UUID.randomUUID().toString(), -0.2F, AttributeModifier.Operation.MULTIPLY_BASE));

}
