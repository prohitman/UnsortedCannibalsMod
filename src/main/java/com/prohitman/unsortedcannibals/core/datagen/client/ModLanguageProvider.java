package com.prohitman.unsortedcannibals.core.datagen.client;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.projectile.BlowDart;
import com.prohitman.unsortedcannibals.core.init.ModBlocks;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import com.prohitman.unsortedcannibals.core.init.ModEntities;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
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
        addItem(ModItems.BLOWGUN);
        addItem(ModItems.DEATH_WHISTLE);
        addItem(ModItems.BONE_CHESTPLATE);
        addItem(ModItems.BONE_HELMET);
        addItem(ModItems.RAZOR_SWORD);
        addItem(ModItems.CRUSHER_AXE);
        addItem(ModItems.SERRATED_SPEARHEAD);

        addBlock(ModBlocks.BONE_BARRICADE);
        addBlock(ModBlocks.SHARPENED_BONES);
        addBlock(ModBlocks.FALL_TRAP);
        addBlock(ModBlocks.SINISTER_SKULL);

        addEffect(ModEffects.LIVE_BAIT);
        addEffect(ModEffects.VISCERAL_PAIN);
        addEffect(ModEffects.SHATTERED_BONES);

        add( "entity.unsortedcannibals.serrated_spear", "Serrated Spear");
        add( "entity.unsortedcannibals.blow_dart", "Blow Dart");

        add("sounds.unsortedcannibals.death_whistle", "Death Whistle");
        add("sounds.unsortedcannibals.frenzy_hurt", "Frenzy Hurt");
        add("sounds.unsortedcannibals.yearn_death", "Yearn Death");
        add("sounds.unsortedcannibals.yearn_hurt", "Yearn Hurt");
        add("sounds.unsortedcannibals.yearn_idle", "Yearn Idle");

        add("key.unsortedcannibals.detailskey", "More Tooltip Details");
        add("key.categories.unsortedcannibals", "Unsorted Cannibals");

        add("item.tooltip.bone_armor", "\u00A77Grants immunity against \u00A7cVisceral Pain\u00A77.");
        add("item.tooltip.razor_sword", "\u00A77Deals extra damage when enemy has below \u00A7c50% health\u00A77.");
        add("item.tooltip.crusher_axe", "\u00A7cShatters bones \u00A77when dealing a critical hit.");
        add("item.tooltip.serrated_spear", "\u00A77Inflicts \u00A7cVisceral Pain\u00A77.");
        add("item.tooltip.death_whistle", "\u00A77Causes panic to nearby creatures.");
        add("item.tooltip.press_shift", "\u00A7b[+SHIFT]");


        add("itemGroup.unsortedcannibals", "Unsorted Cannibals Mod");
    }

    public void addEffect(RegistryObject<MobEffect> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addBlock(RegistryObject<Block> key) {
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addItem(RegistryObject<Item> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }
}
