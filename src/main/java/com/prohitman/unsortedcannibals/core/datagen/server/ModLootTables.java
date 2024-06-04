package com.prohitman.unsortedcannibals.core.datagen.server;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.init.ModBlocks;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.stream.Collectors;

public class ModLootTables extends VanillaBlockLoot {

    @Override
    protected void generate(){
        this.dropSelf(ModBlocks.SINISTER_SKULL.get());
        this.add(ModBlocks.FALL_TRAP.get(), noDrop());
        this.add(ModBlocks.SHARPENED_BONES.get(), noDrop());
        this.add(ModBlocks.BONE_BARRICADE.get(), (p_251149_) -> {
            return this.createSingleItemTableWithSilkTouch(p_251149_, Items.BONE, UniformGenerator.between(1.0F, 3.0F));
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(UnsortedCannibalsMod.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
