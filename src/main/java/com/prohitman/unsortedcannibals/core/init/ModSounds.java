package com.prohitman.unsortedcannibals.core.init;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UnsortedCannibalsMod.MODID);

    public static final RegistryObject<SoundEvent> DEATH_WHISTLE = registerSoundEvents("death_whistle");
    public static final RegistryObject<SoundEvent> FRENZY_HURT = registerSoundEvents("frenzy_hurt");
    public static final RegistryObject<SoundEvent> YEARN_IDLE = registerSoundEvents("yearn_idle");
    public static final RegistryObject<SoundEvent> YEARN_HURT = registerSoundEvents("yearn_hurt");
    public static final RegistryObject<SoundEvent> YEARN_DEATH = registerSoundEvents("yearn_death");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(UnsortedCannibalsMod.MODID, name)));
    }
}
