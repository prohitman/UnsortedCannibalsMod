package com.prohitman.unsortedcannibals.common.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Consumer;

public class LiveBaitEffect extends MobEffect {
    public LiveBaitEffect() {
        super(MobEffectCategory.HARMFUL, 0xCBC6A5);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        Level world = entity.level();
        for (int i = 0; i < 5; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * entity.getBbWidth();
            double offsetY = world.random.nextDouble() * entity.getBbHeight();
            double offsetZ = (world.random.nextDouble() - 0.5) * entity.getBbWidth();
            world.addParticle(ParticleTypes.EFFECT,
                    entity.getX() + offsetX,
                    entity.getY() + offsetY,
                    entity.getZ() + offsetZ,
                    0, 0, 0);

        }
    }
}
