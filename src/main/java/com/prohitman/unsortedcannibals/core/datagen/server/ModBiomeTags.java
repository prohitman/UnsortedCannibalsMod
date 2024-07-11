package com.prohitman.unsortedcannibals.core.datagen.server;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTags extends BiomeTagsProvider {
    public static final TagKey<Biome> CRAVE_PATROL_SPAWNS = createTag("crave_patrol_spawns");

    public ModBiomeTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, UnsortedCannibalsMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(CRAVE_PATROL_SPAWNS).addTags(BiomeTags.IS_SAVANNA).addTags(BiomeTags.IS_FOREST).addTags(BiomeTags.IS_JUNGLE).replace(false);
    }

    private static TagKey<Biome> createTag(String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(UnsortedCannibalsMod.MODID, name));
    }
}
