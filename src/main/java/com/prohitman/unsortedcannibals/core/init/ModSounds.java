package com.prohitman.unsortedcannibals.core.init;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
    public static final RegistryObject<SoundEvent> LIVE_BAIT = registerSoundEvents("live_bait");
    public static final RegistryObject<SoundEvent> BLOWGUN_SHOOT = registerSoundEvents("blowgun_shoot");
    public static final RegistryObject<SoundEvent> BLOW_DART_HIT = registerSoundEvents("blow_dart_hit");
    public static final RegistryObject<SoundEvent> SINISTER_SKULL = registerSoundEvents("sinister_skull");
    public static final RegistryObject<SoundEvent> YEARN_EATING = registerSoundEvents("yearn_eating");
    public static final RegistryObject<SoundEvent> CRAVE_IDLE = registerSoundEvents("crave_idle");
    public static final RegistryObject<SoundEvent> CRAVE_HURT = registerSoundEvents("crave_hurt");
    public static final RegistryObject<SoundEvent> CRAVE_DEATH = registerSoundEvents("crave_death");
    public static final RegistryObject<SoundEvent> FRENZY_IDLE = registerSoundEvents("frenzy_idle");
    public static final RegistryObject<SoundEvent> FRENZY_DEATH = registerSoundEvents("frenzy_death");

    public static final RegistryObject<SoundEvent> BONE_CRACK = registerSoundEvents("bone_crack");
    public static final RegistryObject<SoundEvent> SPEAR_THROW = registerSoundEvents("spear_throw");
    public static final RegistryObject<SoundEvent> SPEAR_HIT = registerSoundEvents("spear_hit");
    public static final RegistryObject<SoundEvent> CANNIBAL_AMBIENT_FOREST = registerSoundEvents("cannibal_ambient_forest");
    public static final RegistryObject<SoundEvent> CANNIBAL_AMBIENT_JUNGLE = registerSoundEvents("cannibal_ambient_jungle");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(UnsortedCannibalsMod.MODID, name)));
    }
}
