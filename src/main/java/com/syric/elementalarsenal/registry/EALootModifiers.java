package com.syric.elementalarsenal.registry;

import com.mojang.serialization.Codec;
import com.syric.elementalarsenal.ElementalArsenal;
import com.syric.elementalarsenal.loot.AddMuffledTagModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EALootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ElementalArsenal.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_MUFFLED_TAG =
            LOOT_MODIFIER_SERIALIZERS.register("add_muffled_tag_modifier", AddMuffledTagModifier.CODEC);

}
