package com.prohitman.unsortedcannibals.core.init;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.items.BlowGunItem;
import com.prohitman.unsortedcannibals.common.items.DeathWhistleItem;
import com.prohitman.unsortedcannibals.common.items.SerratedSpearItem;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
    public static final RegistryObject<Item> SERRATED_SPEAR_INVENTORY = ITEMS.register("serrated_spear_inventory", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLOWGUN = ITEMS.register("blowgun", () -> new BlowGunItem(new Item.Properties()));
    public static final RegistryObject<Item> BLOW_DART = ITEMS.register("blow_dart", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> STURDY_BONES = ITEMS.register("sturdy_bones", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DEATH_WHISTLE = ITEMS.register("death_whistle", () -> new DeathWhistleItem(new Item.Properties()));

}