package com.prohitman.unsortedcannibals.core.datagen.client;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, UnsortedCannibalsMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        createCrossBlock(ModBlocks.BONE_BARRICADE);
        simpleBlock(ModBlocks.REEKING_FLESH_BLOCK.get());
    }

    private void createCrossBlock(RegistryObject<Block> block) {
        simpleBlock(block.get(), models().cross(block.getId().getPath(),
                modLoc("block/" + block.getId().getPath())).renderType("cutout_mipped"));
    }
}
