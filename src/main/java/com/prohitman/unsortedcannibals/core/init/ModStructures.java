package com.prohitman.unsortedcannibals.core.init;

import com.mojang.serialization.Codec;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.worldgen.structures.FallTrapStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, UnsortedCannibalsMod.MODID);

    public static final RegistryObject<StructureType<FallTrapStructure>> FALL_TRAP_STRUCTURE = STRUCTURE_TYPES.register("fall_traps", () -> explicitStructureTypeTyping(FallTrapStructure.CODEC));


    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }
}
