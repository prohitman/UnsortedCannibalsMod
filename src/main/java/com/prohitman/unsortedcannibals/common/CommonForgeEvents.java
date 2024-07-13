package com.prohitman.unsortedcannibals.common;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.FrenzyCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import com.prohitman.unsortedcannibals.common.items.armor.BoneArmorItem;
import com.prohitman.unsortedcannibals.core.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    private static ResourceKey<Structure> CAMPSITE = ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(UnsortedCannibalsMod.MODID, "campsite"));

    @SubscribeEvent
    public static void onSpawnEvent(MobSpawnEvent.PositionCheck event){
        LivingEntity livingEntity = event.getEntity();
        if(livingEntity.getMobType() == ModMobTypes.CANNIBAL && event.getSpawnType() == MobSpawnType.NATURAL){
            BlockPos pos = livingEntity.blockPosition();
            boolean hasStructure = hasStructure(event.getEntity().level().registryAccess(), CAMPSITE, ((ServerLevel)event.getEntity().level()).structureManager().getAllStructuresAt(pos).keySet());

            if(hasStructure){
                boolean shouldRespawn = ModConfiguration.SHOULD_RESPAWN_CANNIBALS.get();

                if(shouldRespawn){
                    boolean shouldRespawnCraves = ModConfiguration.SHOULD_RESPAWN_CRAVE.get();
                    boolean shouldRespawnYearns = ModConfiguration.SHOULD_RESPAWN_YEARN.get();
                    boolean shouldRespawnFrenzy = ModConfiguration.SHOULD_RESPAWN_FRENZY.get();

                    double craveRespawnChance = ModConfiguration.CRAVE_RESPAWN_CHANCE.get();
                    double yearnRespawnChance = ModConfiguration.YEARN_RESPAWN_CHANCE.get();
                    double frenzyRespawnChance = ModConfiguration.FRENZY_RESPAWN_CHANCE.get();

                    if(event.getEntity() instanceof CraveCannibal craveCannibal){

                        craveCannibal.setPatrolling(false);
                        //System.out.println("We are called here:");
                        //System.out.println("Current chance: " + craveRespawnChance + " " + checkShouldRespawn(livingEntity, shouldRespawnCraves, craveRespawnChance));
                        //System.out.println("Is patrolling??" + craveCannibal.isPatrolling());
                        event.setResult(checkShouldRespawn(livingEntity, shouldRespawnCraves, craveRespawnChance) ? Event.Result.DEFAULT : Event.Result.DENY);
                    } else if(event.getEntity() instanceof YearnCannibal){
                        event.setResult(checkShouldRespawn(livingEntity, shouldRespawnYearns, yearnRespawnChance) ? Event.Result.DEFAULT : Event.Result.DENY);
                    } else if(event.getEntity() instanceof FrenzyCannibal){
                        event.setResult(checkShouldRespawn(livingEntity, shouldRespawnFrenzy, frenzyRespawnChance) ? Event.Result.DEFAULT : Event.Result.DENY);
                    }

                } else{
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    public static boolean checkShouldRespawn(LivingEntity livingEntity, boolean shouldRespawn, double respawnChance){
        if(!shouldRespawn){
            return false;
        }
        else return !(livingEntity.getRandom().nextDouble() > respawnChance);
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

            if(event.getEntity().level().random.nextInt(10) == 0){
                for (int i = 0; i < 5; i++) {
                    double offsetX = entity.getX() + (entity.level().random.nextDouble() - 0.5) * entity.getBbWidth();
                    double offsetY = entity.getY() + entity.level().random.nextDouble() * entity.getBbHeight();
                    double offsetZ = entity.getZ() + (entity.level().random.nextDouble() - 0.5) * entity.getBbWidth();
                    entity.level().addParticle(ModParticles.BLOOD_PARTICLE.get(), offsetX, offsetY, offsetZ, 0.0D, -0.1D, 0.0D);
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
            boolean shouldPlay = ModConfiguration.SHOULD_PLAY_CANNIBAL_AMBIENT_SOUNDS.get();
            if(shouldPlay){
                if(entity.level().random.nextFloat() < 0.0002f){
                    if(entity.level().getBiome(entity.blockPosition()).is(BiomeTags.IS_FOREST)){
                        player.playSound(ModSounds.CANNIBAL_AMBIENT_FOREST.get(), 0.5F + entity.level().random.nextInt(3)*0.2F, 1);
                    } else if(entity.level().getBiome(entity.blockPosition()).is(BiomeTags.IS_JUNGLE)){
                        player.playSound(ModSounds.CANNIBAL_AMBIENT_JUNGLE.get(), 0.5F + entity.level().random.nextInt(3)*0.2F, 1);
                    }
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
        if(event.getTarget() instanceof LivingEntity livingEntity && event.isVanillaCritical() && event.getEntity().getMainHandItem().is(ModItems.CRUSHER_AXE.get()) && !event.getEntity().isCreative() && !event.getEntity().isSpectator()){
            livingEntity.addEffect(new MobEffectInstance(ModEffects.SHATTERED_BONES.get(), 100), livingEntity);
            event.getEntity().level().playSound(livingEntity, livingEntity.blockPosition(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.BLOCKS, 1, 1);
        }
    }

    @SubscribeEvent
    public static void onJumpEntity(LivingEvent.LivingJumpEvent event){
        if(event.getEntity().hasEffect(ModEffects.SHATTERED_BONES.get())){
                event.getEntity().setDeltaMovement(Vec3.ZERO);
        }
    }

    @SubscribeEvent
    public static void onLiveBaitExpire(MobEffectEvent.Expired event) {
        if (event.getEffectInstance().getEffect() == ModEffects.LIVE_BAIT.get()) {
            Level level = event.getEntity().level();
            BlockPos playerPos = event.getEntity().blockPosition();
            RandomSource random = level.random;
            int craveSize = ModConfiguration.MIN_CRAVE_SIZE.get() + random.nextInt(ModConfiguration.MAX_CRAVE_SIZE.get());
            int frenzySize = ModConfiguration.MIN_FRENZY_SIZE.get() + random.nextInt(ModConfiguration.MAX_FRENZY_SIZE.get());
            int yearnSize = ModConfiguration.MIN_YEARN_SIZE.get() + random.nextInt(ModConfiguration.MAX_YEARN_SIZE.get());
            boolean isFromSkull = event.getEffectInstance().getAmplifier() == 1;
            int minDistance = ModConfiguration.MIN_SPAWN_DISTANCE.get();
            int spawnRadius = ModConfiguration.MAX_SPAWN_DISTANCE.get();

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
            cannibal.setYBodyRot(yRot);
            cannibal.setXRot(xRot);
            if(target instanceof Player player){
                if(!player.isSpectator() && !player.isCreative()){
                    cannibal.setTarget(null);
                    cannibal.setTarget(target);
                }
            }
            if(!level.isClientSide){
                cannibal.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(cannibal.blockPosition()), MobSpawnType.TRIGGERED, null, null);
            }
            level.addFreshEntity(cannibal);
        }
    }

    private static boolean hasStructure(final RegistryAccess registryAccess, final ResourceKey<Structure> key, final Set<Structure> structures) {
        final Registry<Structure> registry = registryAccess.registryOrThrow(Registries.STRUCTURE);
        for(Structure structure : structures) {
            // load structure resource key
            Optional<ResourceKey<Structure>> resourceKey = registry.getResourceKey(structure);
            if(resourceKey.isEmpty()) continue;
            // check equality
            if(resourceKey.get().equals(key)) {
                return true;
            }
        }
        // all checks failed
        return false;
    }

}
