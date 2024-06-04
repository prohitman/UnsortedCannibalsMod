package com.prohitman.unsortedcannibals.common;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.DummyCannibal;
import com.prohitman.unsortedcannibals.core.init.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.DUMMY_CANNIBAL.get(), DummyCannibal.createAttributes().build());
        event.put(ModEntities.CRAVE.get(), CraveCannibal.createAttributes().build());

    }
}
