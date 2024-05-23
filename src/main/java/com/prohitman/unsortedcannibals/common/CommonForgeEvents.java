package com.prohitman.unsortedcannibals.common;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void livingTickEntity(LivingEvent.LivingTickEvent event){
        if(event.getEntity().hasEffect(ModEffects.VISCERAL_PAIN.get())){
            CompoundTag entityData = event.getEntity().getPersistentData();

            CompoundTag position = entityData.getCompound("position");

            double oldX = position.getDouble("posX");
            double oldY = position.getDouble("posY");
            double oldZ = position.getDouble("posZ");

            Vec3 oldPos = new Vec3(oldX, oldY, oldZ);
            Vec3 currentPos = event.getEntity().position();

            position.putDouble("posX", event.getEntity().position().x);
            position.putDouble("posY", event.getEntity().position().y);
            position.putDouble("posZ", event.getEntity().position().z);

            entityData.put("position", position);

            if(!currentPos.equals(oldPos)){
                event.getEntity().hurt(event.getEntity().damageSources().magic(), 1.5F);
            }
        }
    }
}
