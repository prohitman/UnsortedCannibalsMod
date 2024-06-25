package com.prohitman.unsortedcannibals.common;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.core.init.ModEntities;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CRAVE.get(), CraveCannibal.createAttributes().build());
        event.put(ModEntities.YEARN.get(), CraveCannibal.createAttributes().build());
        event.put(ModEntities.FRENZY.get(), CraveCannibal.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawns(@NotNull SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.CRAVE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CraveCannibal::checkCraveSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
