package com.prohitman.unsortedcannibals.common.entities.living;

import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public abstract class PatrollingCannibal extends PathfinderMob {
    private static final EntityDataAccessor<Boolean> IS_LEADER = SynchedEntityData.defineId(PatrollingCannibal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_PATROLLING = SynchedEntityData.defineId(PatrollingCannibal.class, EntityDataSerializers.BOOLEAN);
    public static final Predicate<PatrollingCannibal> LEADER_CANNIBAL_PREDICATE =
            mob -> mob != null && mob.getMobType() == ModMobTypes.CANNIBAL && mob.isLeader();
    protected PatrollingCannibal(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public boolean isLeader() {
        return this.entityData.get(IS_LEADER);
    }

    private void setIsLeader(boolean is_leader) {
        this.entityData.set(IS_LEADER, is_leader);
    }

    public boolean isPatrolling() {
        return this.entityData.get(IS_PATROLLING);
    }

    private void setPatrolling(boolean is_patrolling) {
        this.entityData.set(IS_PATROLLING, is_patrolling);
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if(pReason != MobSpawnType.STRUCTURE){
            this.setPatrolling(true);
        }

        if (this.isLeader()) {
            this.setItemSlot(EquipmentSlot.HEAD, Raid.getLeaderBannerInstance());
            this.setDropChance(EquipmentSlot.HEAD, 2.0F);
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public void tick() {
        super.tick();

        List<PatrollingCannibal> list = this.level().getEntitiesOfClass(PatrollingCannibal.class, this.getBoundingBox().inflate(35.0D), LEADER_CANNIBAL_PREDICATE);

        if(list.isEmpty()){
            this.setIsLeader(true);
        }
        if(this.isLeader()){
            this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 3));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_PATROLLING, false);
        this.entityData.define(IS_LEADER, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("is_patrolling", this.entityData.get(IS_PATROLLING));
        pCompound.putBoolean("is_leader", this.entityData.get(IS_LEADER));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(IS_PATROLLING, pCompound.getBoolean("is_patrolling"));
        this.entityData.set(IS_LEADER, pCompound.getBoolean("is_leader"));
    }
}
