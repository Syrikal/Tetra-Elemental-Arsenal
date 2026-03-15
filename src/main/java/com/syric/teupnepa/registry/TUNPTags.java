package com.syric.teupnepa.registry;

import com.syric.teupnepa.TeUpNePa;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;

public class TUNPTags {

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> GOLD_DAMAGED = tag("gold_damaged");
        public static final TagKey<EntityType<?>> FROST_DAMAGED = tag("frost_damaged");
        public static final TagKey<EntityType<?>> END_NATIVE = tag("end_native");
        public static final TagKey<EntityType<?>> AETHER_NATIVE = tag("aether_native");
        public static final TagKey<EntityType<?>> SCULK = tag("sculk");
        public static final TagKey<EntityType<?>> WITHER = tag("wither");
        public static final TagKey<EntityType<?>> FUNGAL = tag("fungal");
        public static final TagKey<EntityType<?>> PHANTOM = tag("phantom");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TeUpNePa.MODID, name));
        }
    }

    public static class MobEffects {
        public static final TagKey<MobEffect> RADIANT_REDUCES = tag("radiant_reduces");
        public static final TagKey<MobEffect> RADIANT_REDUCES_STRONG = tag("radiant_reduces_strong");

        private static TagKey<MobEffect> tag(String name) {
            return TagKey.create(Registries.MOB_EFFECT, ResourceLocation.fromNamespaceAndPath(TeUpNePa.MODID, name));
        }
    }

}
