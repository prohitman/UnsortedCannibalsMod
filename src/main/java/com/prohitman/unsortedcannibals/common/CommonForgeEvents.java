package com.prohitman.unsortedcannibals.common;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.items.armor.BoneArmorItem;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void livingTickEntity(LivingEvent.LivingTickEvent event){
        LivingEntity entity = event.getEntity();

        if(entity.hasEffect(ModEffects.VISCERAL_PAIN.get())){
            for(ItemStack itemStack : entity.getArmorSlots()){
                if(itemStack.getItem() instanceof BoneArmorItem){
                    entity.removeEffect(ModEffects.VISCERAL_PAIN.get());
                }
            }
        }
        if(entity.hasEffect(ModEffects.SHATTERED_BONES.get())){
            entity.setSprinting(false);

            if (entity.getDeltaMovement().y > 0) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0.05D, 1));
            }
        }
    }


}
