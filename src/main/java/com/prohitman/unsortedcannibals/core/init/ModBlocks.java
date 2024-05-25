package com.prohitman.unsortedcannibals.core.init;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.blocks.BoneBarricade;
import com.prohitman.unsortedcannibals.common.blocks.FallTrap;
import com.prohitman.unsortedcannibals.common.blocks.SharpenedBones;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UnsortedCannibalsMod.MODID);

    public static final RegistryObject<Block> BONE_BARRICADE = createRegistry("bone_barricade", () -> new BoneBarricade(BlockBehaviour.Properties.of().sound(SoundType.BONE_BLOCK).dynamicShape().noCollission().offsetType(BlockBehaviour.OffsetType.XZ).strength(2.5F)), new Item.Properties());
    public static final RegistryObject<Block> SHARPENED_BONES = createRegistry("sharpened_bones", () -> new SharpenedBones(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO_WOOD).strength(2)), new Item.Properties());
    public static final RegistryObject<Block> FALL_TRAP = createRegistry("fall_trap", () -> new FallTrap(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO_WOOD)), new Item.Properties());

    public static <T extends Block> RegistryObject<Block> createRegistry(String name, Supplier<T> block, Item.Properties properties) {
        RegistryObject<Block> object = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(object.get(), properties));

        return object;
    }
}
