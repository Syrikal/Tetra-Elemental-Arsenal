package com.syric.teupnepa.registry;

import com.syric.teupnepa.TeUpNePa;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class TUNPTags {

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> GOLD_DAMAGED = tag("gold_damaged");
        public static final TagKey<EntityType<?>> END_NATIVE = tag("end_native");
        public static final TagKey<EntityType<?>> SCULK = tag("sculk");
        public static final TagKey<EntityType<?>> WITHER = tag("wither");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(TeUpNePa.MODID, name));
        }
    }

}
