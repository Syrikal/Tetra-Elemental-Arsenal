package com.syric.elementalarsenal.registry;

import com.syric.elementalarsenal.ElementalArsenal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EASounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS.getRegistryName(), ElementalArsenal.MODID);

    public static final RegistryObject<SoundEvent> LIGHTNING_SHOCKWAVE = registerSoundEvents("lightning_shockwave");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(ElementalArsenal.MODID, name)));
    }

}
