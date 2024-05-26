package com.prohitman.unsortedcannibals.common.effects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class VisceralPainEffect extends MobEffect {
    public VisceralPainEffect() {
        super(MobEffectCategory.HARMFUL, 0xA41825);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        CompoundTag entityData = pLivingEntity.getPersistentData();

        CompoundTag position = entityData.getCompound("position");

        double oldX = position.getDouble("posX");
        double oldY = position.getDouble("posY");
        double oldZ = position.getDouble("posZ");

        Vec3 oldPos = new Vec3(oldX, oldY, oldZ);
        Vec3 currentPos = pLivingEntity.position();

        position.putDouble("posX", pLivingEntity.position().x);
        position.putDouble("posY", pLivingEntity.position().y);
        position.putDouble("posZ", pLivingEntity.position().z);

        entityData.put("position", position);

        if(!currentPos.equals(oldPos)){
            pLivingEntity.hurt(pLivingEntity.damageSources().magic(), 1.5F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

}
