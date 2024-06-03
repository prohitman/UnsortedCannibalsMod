package com.prohitman.unsortedcannibals.core.init;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.items.*;
import com.prohitman.unsortedcannibals.common.items.armor.BoneArmorItem;
import com.prohitman.unsortedcannibals.common.items.armor.ModArmorMaterials;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.event.GeoRenderEvent;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UnsortedCannibalsMod.MODID);

    public static final RegistryObject<Item> SEVERED_NOSE = ITEMS.register("severed_nose", () -> new Item(new Item.Properties().food(ModFoods.SEVERED_NOSE)));
    public static final RegistryObject<Item> REEKING_FLESH = ITEMS.register("reeking_flesh", () -> new Item(new Item.Properties().food(ModFoods.REEKING_FLESH)));
    public static final RegistryObject<Item> SERRATED_SPEAR = ITEMS.register("serrated_spear", () -> new SerratedSpearItem(new Item.Properties().durability(175)));
    public static final RegistryObject<Item> SERRATED_SPEAR_INVENTORY = ITEMS.register("serrated_spear_inventory", () -> new InventoryItem(new Item.Properties()));
    public static final RegistryObject<Item> BLOWGUN = ITEMS.register("blowgun", () -> new BlowGunItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> BLOWGUN_INVENTORY = ITEMS.register("blowgun_inventory", () -> new InventoryItem(new Item.Properties()));

    public static final RegistryObject<Item> BLOW_DART = ITEMS.register("blow_dart", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STURDY_BONES = ITEMS.register("sturdy_bones", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAZOR_SWORD = ITEMS.register("razor_sword", () -> new RazorSwordItem(ModItemTiers.BONE, 3, -2.2f, new Item.Properties()));
    public static final RegistryObject<Item> CRUSHER_AXE = ITEMS.register("crusher_axe", () -> new CrusherAxeItem(ModItemTiers.BONE, 4, -3.0f, new Item.Properties()));

    public static final RegistryObject<Item> BONE_HELMET = ITEMS.register("bone_helmet", () -> new
            BoneArmorItem(ModArmorMaterials.BONE, ArmorItem.Type.HELMET, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> BONE_CHESTPLATE = ITEMS.register("bone_chestplate", () -> new
            BoneArmorItem(ModArmorMaterials.BONE, ArmorItem.Type.CHESTPLATE, new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DEATH_WHISTLE = ITEMS.register("death_whistle", () -> new DeathWhistleItem(new Item.Properties().rarity(Rarity.EPIC)));

}