package com.prohitman.unsortedcannibals.core.datagen.server;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.init.ModBlocks;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.stream.Collectors;

public class ModLootTables extends VanillaBlockLoot {

    @Override
    protected void generate(){
        this.dropSelf(ModBlocks.SINISTER_SKULL.get());
        this.add(ModBlocks.FALL_TRAP.get(), noDrop());
        this.add(ModBlocks.SHARPENED_BONES.get(), noDrop());
        this.dropSelf(ModBlocks.BONE_BARRICADE.get());

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(e -> e.getKey().location().getNamespace().equals(UnsortedCannibalsMod.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
