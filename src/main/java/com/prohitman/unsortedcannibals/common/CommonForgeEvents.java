package com.prohitman.unsortedcannibals.common;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.items.armor.BoneArmorItem;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import com.prohitman.unsortedcannibals.core.init.ModEntities;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import com.prohitman.unsortedcannibals.core.init.ModSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {

        });
    }

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
        if(entity instanceof Player player){
            if(entity.level().random.nextFloat() < 0.0005f){
                if(entity.level().getBiome(entity.blockPosition()).is(BiomeTags.IS_FOREST)){
                    player.playSound(ModSounds.CANNIBAL_AMBIENT_FOREST.get(), 0.5F + entity.level().random.nextInt(3)*0.2F, 1);
                } else if(entity.level().getBiome(entity.blockPosition()).is(BiomeTags.IS_JUNGLE)){
                    player.playSound(ModSounds.CANNIBAL_AMBIENT_JUNGLE.get(), 0.5F + entity.level().random.nextInt(3)*0.2F, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public static void livingUseItemEvent(LivingEntityUseItemEvent.Tick event){
        if(event.getItem().is(ModItems.DEATH_WHISTLE.get())){
            List<PathfinderMob> mobs = event.getEntity().level().getEntitiesOfClass(PathfinderMob.class, event.getEntity().getBoundingBox().inflate(15, 5, 15));
            for(PathfinderMob entityIn : mobs){
                if(entityIn != null){
                    if(entityIn instanceof Animal || entityIn instanceof Monster || entityIn instanceof Npc){

                        ((Mob) entityIn).setAggressive(false);
                        Vec3 vec3 = DefaultRandomPos.getPosAway(entityIn, 16, 7, event.getEntity().position());
                        if (vec3 != null && event.getEntity().distanceToSqr(vec3.x, vec3.y, vec3.z) >= event.getEntity().distanceToSqr(entityIn)) {
                            Path path = ((Mob) entityIn).getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                            if(path != null){
                                entityIn.getNavigation().moveTo(path, 1.25);
                            }
                        }
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public static void onCritHit(CriticalHitEvent event){
        if(event.getTarget() instanceof LivingEntity livingEntity && event.isVanillaCritical() && event.getEntity().getMainHandItem().is(ModItems.CRUSHER_AXE.get())){
            livingEntity.addEffect(new MobEffectInstance(ModEffects.SHATTERED_BONES.get(), 100), livingEntity);
            event.getEntity().level().playSound(livingEntity, livingEntity.blockPosition(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.BLOCKS, 1, 1);
        }
    }



    @SubscribeEvent
    public static void onLiveBaitExpire(MobEffectEvent.Expired event) {
        if (event.getEffectInstance().getEffect() == ModEffects.LIVE_BAIT.get()) {
            Level level = event.getEntity().level();
            BlockPos playerPos = event.getEntity().blockPosition();
            RandomSource random = level.random;
            int craveSize = 3 + random.nextInt(3);
            int frenzySize = 2 + random.nextInt(2);
            int yearnSize = 1 + random.nextInt(1);
            boolean isFromSkull = event.getEffectInstance().getAmplifier() == 1;
            int minDistance = 7;
            int spawnRadius = 13;

            level.playSound(null, playerPos, ModSounds.LIVE_BAIT.get(), SoundSource.HOSTILE, 1.3F, 1.3F);

            for (int i = 0; i < craveSize; i++) {
                spawnCannibal(level, event.getEntity(), playerPos, random, minDistance, spawnRadius, 0);
            }

            for (int i = 0; i < frenzySize; i++) {
                spawnCannibal(level, event.getEntity(), playerPos, random, minDistance, spawnRadius, 1);
            }

            if(isFromSkull){
                for (int i = 0; i < yearnSize; i++) {
                    spawnCannibal(level, event.getEntity(), playerPos, random, minDistance, spawnRadius, 2);
                }
            }
        }
    }

    private static void spawnCannibal(Level level, LivingEntity target, BlockPos playerPos, RandomSource random, int minDistance, int spawnRadius, int cannibalId) {
        PathfinderMob cannibal;
        if (cannibalId == 1){
            cannibal = ModEntities.FRENZY.get().create(level);
        } else if(cannibalId == 2){
            cannibal = ModEntities.YEARN.get().create(level);
        } else {
            cannibal = ModEntities.CRAVE.get().create(level);
        }

        if (cannibal != null) {
            double angle = random.nextDouble() * Math.PI * 2;
            int distance = minDistance + random.nextInt(spawnRadius - minDistance);
            int dx = Mth.floor(Math.cos(angle) * distance);
            int dz = Mth.floor(Math.sin(angle) * distance);
            BlockPos spawnPos = playerPos.offset(dx, 0, dz);

            spawnPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnPos);

            double deltaX = playerPos.getX() - spawnPos.getX();
            double deltaZ = playerPos.getZ() - spawnPos.getZ();
            float yRot = (float) (Mth.atan2(deltaZ, deltaX) * (180 / Math.PI)) - 90.0F;
            float xRot = 0.0F;

            cannibal.moveTo(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), yRot, xRot);
            if(target instanceof Player player){
                if(!player.isSpectator() && !player.isCreative()){
                    cannibal.setTarget(target);
                }
            }
            if(!level.isClientSide){
                cannibal.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(cannibal.blockPosition()), MobSpawnType.EVENT, null, null);
            }
            level.addFreshEntity(cannibal);
        }
    }
}
