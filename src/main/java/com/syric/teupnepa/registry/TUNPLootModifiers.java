package com.syric.teupnepa.registry;

import com.mojang.serialization.Codec;
import com.syric.teupnepa.TeUpNePa;
import com.syric.teupnepa.loot.AddMuffledTagModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TUNPLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TeUpNePa.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_MUFFLED_TAG =
            LOOT_MODIFIER_SERIALIZERS.register("add_muffled_tag_modifier", AddMuffledTagModifier.CODEC);

}
