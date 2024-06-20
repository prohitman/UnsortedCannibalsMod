package com.prohitman.unsortedcannibals.core.datagen.server;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTags extends ItemTagsProvider {
    public ModItemTags(PackOutput generator, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, pLookupProvider, blockTags, UnsortedCannibalsMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(Tags.Items.ARMORS_CHESTPLATES).add(ModItems.BONE_CHESTPLATE.get());
        tag(Tags.Items.ARMORS_HELMETS).add(ModItems.BONE_HELMET.get());
        tag(ItemTags.SWORDS).add(ModItems.RAZOR_SWORD.get());
        tag(ItemTags.AXES).add(ModItems.CRUSHER_AXE.get());

    }
}
