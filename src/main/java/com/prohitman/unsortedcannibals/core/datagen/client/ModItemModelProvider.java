package com.prohitman.unsortedcannibals.core.datagen.client;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.init.ModBlocks;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, UnsortedCannibalsMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        //Items
        createSingle(ModItems.SEVERED_NOSE);
        createSingle(ModItems.REEKING_FLESH);
        createSingle(ModItems.BLOW_DART);
        createSingle(ModItems.STURDY_BONES);
        createSingle(ModItems.BONE_CHESTPLATE);
        createSingle(ModItems.BONE_HELMET);
        createSingleHandHeld(ModItems.RAZOR_SWORD);
        createSingle(ModItems.SERRATED_SPEARHEAD);

        withExistingParent(ModItems.CRAVE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.YEARN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.FRENZY_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        //Blocks
        createSingleTextureCrossBlock(ModBlocks.BONE_BARRICADE);
        createParent(ModBlocks.SHARPENED_BONES);
        createParent(ModBlocks.FALL_TRAP);
        createParent(ModBlocks.SINISTER_SKULL);

    }

    private void createSingleTextureCrossBlock(RegistryObject<Block> block){
        singleTexture((block.getId().getPath()),
                mcLoc("item/generated"),
                "layer0", modLoc("block/" + block.getId().getPath()));
    }

    private void createBlockSingle(RegistryObject<Block> block, String location){
        singleTexture((block.getId().getPath()),
                mcLoc("item/generated"),
                "layer0", modLoc(location));
    }

    private void createBlockSingleHandheld(RegistryObject<Block> block, String location){
        singleTexture((block.getId().getPath()),
                mcLoc("item/handheld"),
                "layer0", modLoc(location));
    }

    private void createParent(RegistryObject<Block> handler) {
        withExistingParent(handler.getId().getPath(), modLoc( "block/" + handler.getId().getPath()));
    }

    private void createParentBlock(RegistryObject<Block> handler) {
        withExistingParent(handler.getId().getPath(), modLoc( "block/" + handler.getId().getPath()));
    }

    private String name(RegistryObject<Block> block) {
        return block.getId().getPath();
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private void createSingle(RegistryObject<Item> item) {
        ModelFile generated = getExistingFile(mcLoc("item/generated"));
        getBuilder(item.getId().getPath()).parent(generated).texture("layer0", modLoc( "item/" + item.getId().getPath()));
    }

    private void createSingleHandHeld(RegistryObject<Item> item) {
        ModelFile generated = getExistingFile(mcLoc("item/handheld"));
        getBuilder(item.getId().getPath()).parent(generated).texture("layer0", modLoc( "item/" + item.getId().getPath()));
    }
}
