package com.prohitman.unsortedcannibals.client;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)

public class ClientForgeEvents {

    @SubscribeEvent
    public static void onKeyPressed(TickEvent.ClientTickEvent event){

    }
}
