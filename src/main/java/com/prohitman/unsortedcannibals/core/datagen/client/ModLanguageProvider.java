package com.prohitman.unsortedcannibals.core.datagen.client;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.init.*;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
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
        addItem(ModItems.CRAVE_SPAWN_EGG);
        addItem(ModItems.YEARN_SPAWN_EGG);
        addItem(ModItems.FRENZY_SPAWN_EGG);

        addBlock(ModBlocks.BONE_BARRICADE);
        addBlock(ModBlocks.SHARPENED_BONES);
        addBlock(ModBlocks.FALL_TRAP);
        addBlock(ModBlocks.SINISTER_SKULL);
        addBlock(ModBlocks.REEKING_FLESH_BLOCK);

        addEffect(ModEffects.LIVE_BAIT);
        addEffect(ModEffects.VISCERAL_PAIN);
        addEffect(ModEffects.SHATTERED_BONES);

        addSound(ModSounds.BLOWGUN_SHOOT);
        addSound(ModSounds.BLOW_DART_HIT);
        addSound(ModSounds.BONE_CRACK);
        addSound(ModSounds.CANNIBAL_AMBIENT_FOREST);
        addSound(ModSounds.CRAVE_DEATH);
        addSound(ModSounds.CRAVE_HURT);
        addSound(ModSounds.CRAVE_IDLE);
        addSound(ModSounds.DEATH_WHISTLE);
        addSound(ModSounds.FRENZY_DEATH);
        addSound(ModSounds.FRENZY_HURT);
        addSound(ModSounds.FRENZY_IDLE);
        addSound(ModSounds.LIVE_BAIT);
        addSound(ModSounds.SINISTER_SKULL);
        addSound(ModSounds.SPEAR_HIT);
        addSound(ModSounds.SPEAR_THROW);
        addSound(ModSounds.YEARN_DEATH);
        addSound(ModSounds.YEARN_EATING);
        addSound(ModSounds.YEARN_HURT);
        addSound(ModSounds.YEARN_IDLE);

        add( "entity.unsortedcannibals.serrated_spear", "Serrated Spear");
        add( "entity.unsortedcannibals.blow_dart", "Blow Dart");
        add( "entity.unsortedcannibals.crave", "Crave");
        add( "entity.unsortedcannibals.yearn", "Yearn");
        add( "entity.unsortedcannibals.frenzy", "Frenzy");

        add("key.unsortedcannibals.detailskey", "More Tooltip Details");
        add("key.categories.unsortedcannibals", "Unsorted Cannibals");

        add("item.tooltip.bone_armor", "\u00A77Grants immunity against \u00A7cVisceral Pain\u00A77.");
        add("item.tooltip.razor_sword", "\u00A77Deals more damage as the target's health \u00A7cdecreases\u00A77.");
        add("item.tooltip.crusher_axe", "\u00A7cShatters bones \u00A77when dealing a critical hit.");
        add("item.tooltip.serrated_spear", "\u00A77Inflicts \u00A7cVisceral Pain\u00A77.");
        add("item.tooltip.death_whistle", "\u00A77Causes panic to nearby creatures.");
        add("item.tooltip.reeking_flesh", "\u00A77Attracts Crave Cannibals.");
        add("item.tooltip.yearn_food_item", "\u00A77One of Yearn's favourite food.");

        add("item.tooltip.press_shift", "\u00A7b[+SHIFT]");

        add("itemGroup.unsortedcannibals", "Unsorted Cannibals Mod");

        addAdvancementTitle("death_whistle", "Echoes of Death");
        addAdvancementDescription("death_whistle", "Blow the Death Whistle. Unleash the sound of terror.");

        addAdvancementTitle("bone_armor", "Bones of Steel");
        addAdvancementDescription("bone_armor", "Wear the full Bone Armor set and become immune to Visceral Pain.");

        addAdvancementTitle("sinister_skull", "Shattering Evil");
        addAdvancementDescription("sinister_skull", "Destroy the Sinister Skull. Beware of what follows.");

        addAdvancementTitle("live_bait", "They are coming...");
        addAdvancementDescription("live_bait", "Fall victim to hidden dangers.");

        addAdvancementTitle("visceral_pain", "Don't Move!");
        addAdvancementDescription("visceral_pain", "Experience the torment of Visceral Pain. Moving has never been so excruciating.");

        addAdvancementTitle("severed_nose", "Bon appétit?");
        addAdvancementDescription("severed_nose", "Snack on a severed nose.");

        addAdvancementTitle("find_campsite", "Into the Den");
        addAdvancementDescription("find_campsite", "Venture into a cannibal campsite. Tread carefully.");

        addAdvancementTitle("enter_world", "The Cannibal's Domain");
        addAdvancementDescription("enter_world", "Enter a world filled with Cannibals.");

        addAdvancementTitle("reeking_flesh", "Ironic, isn't it?");
        addAdvancementDescription("reeking_flesh", "Consume the flesh of a cannibal. Deliciously morbid.");
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

    public void addSound(RegistryObject<SoundEvent> key){
        add("sounds." + key.getId().toLanguageKey(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addAdvancementTitle(String name, String title){
        add("title." + UnsortedCannibalsMod.MODID + "." + name, title);
    }

    public void addAdvancementDescription(String name, String description){
        add("description." + UnsortedCannibalsMod.MODID + "." + name, description);
    }
}
