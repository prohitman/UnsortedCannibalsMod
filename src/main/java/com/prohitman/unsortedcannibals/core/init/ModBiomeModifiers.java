package com.prohitman.unsortedcannibals.core.init;

import com.mojang.serialization.Codec;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.worldgen.mobs.AddMobSpawnsBiomeModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBiomeModifiers {
    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, UnsortedCannibalsMod.MODID);

    public static RegistryObject<Codec<AddMobSpawnsBiomeModifier>> ADD_MOB_SPAWNS_CODEC = BIOME_MODIFIER_SERIALIZERS.register("add_mob_spawns", () -> Codec.unit(AddMobSpawnsBiomeModifier::new));
}
