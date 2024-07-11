package com.prohitman.unsortedcannibals.common.worldgen.mobs;

import com.mojang.serialization.Codec;
import com.prohitman.unsortedcannibals.core.datagen.server.ModBiomeTags;
import com.prohitman.unsortedcannibals.core.init.ModBiomeModifiers;
import com.prohitman.unsortedcannibals.core.init.ModConfiguration;
import com.prohitman.unsortedcannibals.core.init.ModEntities;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class AddMobSpawnsBiomeModifier implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase.equals(Phase.ADD)) {
            addMobSpawn(builder, biome, ModBiomeTags.CRAVE_PATROL_SPAWNS, MobCategory.MONSTER, ModEntities.CRAVE.get(), ModConfiguration.PATROL_WEIGHT.get(), 1, 3);
        }
    }

    void addMobSpawn(ModifiableBiomeInfo.BiomeInfo.Builder builder, Holder<Biome> biome, TagKey<Biome> tag, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        if (biome.is(tag)) {
            builder.getMobSpawnSettings().addSpawn(mobCategory, new MobSpawnSettings.SpawnerData(entityType, weight, minGroupSize, maxGroupSize));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ModBiomeModifiers.ADD_MOB_SPAWNS_CODEC.get();
    }
}
