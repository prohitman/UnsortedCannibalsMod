package com.prohitman.unsortedcannibals.core.datagen;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.datagen.client.ModBlockStateProvider;
import com.prohitman.unsortedcannibals.core.datagen.client.ModItemModelProvider;
import com.prohitman.unsortedcannibals.core.datagen.client.ModLanguageProvider;
import com.prohitman.unsortedcannibals.core.datagen.server.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ModBlockTags blockTags = new ModBlockTags(dataGenerator.getPackOutput(), lookupProvider, event.getExistingFileHelper());
        ModBiomeTags biomeTags = new ModBiomeTags(dataGenerator.getPackOutput(), lookupProvider, event.getExistingFileHelper());

        dataGenerator.addProvider(event.includeClient(), (DataProvider.Factory<ModBlockStateProvider>)
                output -> new ModBlockStateProvider(output, event.getExistingFileHelper()));

        dataGenerator.addProvider(event.includeClient(), (DataProvider.Factory<ModItemModelProvider>)
                output -> new ModItemModelProvider(output, event.getExistingFileHelper()));

        dataGenerator.addProvider(event.includeClient(), (DataProvider.Factory<ModLanguageProvider>)
                output -> new ModLanguageProvider(dataGenerator.getPackOutput(), "en_us"));

        dataGenerator.addProvider(event.includeServer(), (DataProvider.Factory<ModBlockTags>)
                output -> blockTags);

        dataGenerator.addProvider(event.includeServer(), (DataProvider.Factory<ModItemTags>)
                output -> new ModItemTags(output, lookupProvider, blockTags.contentsGetter(), event.getExistingFileHelper()));

        dataGenerator.addProvider(event.includeServer(), new ModRecipes(dataGenerator.getPackOutput()));

        dataGenerator.addProvider(event.includeServer(), new LootTableProvider(dataGenerator.getPackOutput(), Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModLootTables::new, LootContextParamSets.BLOCK))));

        dataGenerator.addProvider(event.includeServer(), (DataProvider.Factory<ModBiomeTags>)
                output -> biomeTags);

        /*dataGenerator.addProvider(event.includeServer(), (DataProvider.Factory<ModWorldGenProvider>)
                output -> new ModWorldGenProvider(output, lookupProvider));

        dataGenerator.addProvider(event.includeServer(), new ModGlobalLootModifiers(dataGenerator.getPackOutput()));*/

        try {
            dataGenerator.run();
        } catch (IOException ignored) {
            throw new RuntimeException(ignored);
        }
    }
}
