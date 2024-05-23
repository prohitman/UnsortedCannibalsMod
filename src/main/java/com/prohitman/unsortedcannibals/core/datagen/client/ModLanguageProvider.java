package com.prohitman.unsortedcannibals.core.datagen.client;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.init.ModBlocks;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.codehaus.plexus.util.StringUtils;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, UnsortedCannibalsMod.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.SEVERED_NOSE);
        addItem(ModItems.SERRATED_SPEAR);
        addItem(ModItems.REEKING_FLESH);
        addItem(ModItems.BLOW_DART);
        addItem(ModItems.STURDY_BONES);

        addBlock(ModBlocks.BONE_BARRICADE);
        addBlock(ModBlocks.SHARPENED_BONES);
        addBlock(ModBlocks.FALL_TRAP);

        add( "effect.unsortedcannibals.live_bait", "Live Bait");
        add( "effect.unsortedcannibals.visceral_pain", "Visceral Pain");
        add( "effect.unsortedcannibals.shattered_bones", "Shattered Bones");

        add( "entity.unsortedcannibals.serrated_spear", "Serrated Spear");

        add("itemGroup.unsortedcannibals", "Unsorted Cannibals Mod");
    }

    public void addBlock(RegistryObject<Block> key) {
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addItem(RegistryObject<Item> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }
}
