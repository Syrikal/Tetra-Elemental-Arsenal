package com.syric.teupnepa.registry;

import com.syric.teupnepa.TeUpNePa;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TUNPSounds {

    public static DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS.getRegistryName(), TeUpNePa.MODID);

    public static final RegistryObject<SoundEvent> LIGHTNING_SHOCKWAVE = registerSoundEvents("lightning_shockwave");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TeUpNePa.MODID, name)));
    }

}
