package com.prohitman.unsortedcannibals.common.entities.living;

import com.google.common.collect.Maps;
import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.goals.CraveAvoidPlayerGoal;
import com.prohitman.unsortedcannibals.common.entities.living.goals.FollowCannibalGoal;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import com.prohitman.unsortedcannibals.core.init.ModSounds;
import net.minecraft.Util;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CraveCannibal extends PatrollingCannibal implements GeoEntity, Enemy {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(CraveCannibal.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> IS_ALONE = SynchedEntityData.defineId(CraveCannibal.class, EntityDataSerializers.BOOLEAN);
    private static final Predicate<Mob> CANNIBAL_PREDICATE =
            mob -> mob != null && mob.getMobType() == ModMobTypes.CANNIBAL;
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    static final Map<Integer, SoundEvent> MOB_SOUND_MAP = Util.make(Maps.newHashMap(), (map) -> {
        map.put(0, SoundEvents.PIG_AMBIENT);
        map.put(1, SoundEvents.CHICKEN_AMBIENT);
        map.put(2, SoundEvents.SHEEP_AMBIENT);
        map.put(3, SoundEvents.COW_AMBIENT);
        map.put(4, SoundEvents.PARROT_AMBIENT);
        map.put(5, SoundEvents.OCELOT_AMBIENT);
    });

    public CraveCannibal(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public boolean isAlone() {
        return this.entityData.get(IS_ALONE);
    }

    private void setAlone(boolean is_lonely) {
        this.entityData.set(IS_ALONE, is_lonely);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FollowCannibalGoal(this, 0.55D, 6.0F, 25f));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new CraveCannibal.CraveMeleeAttackGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Mob.class, 0, true, false, (livingEntity -> {
            if(livingEntity instanceof Player){
                return !this.isAlone();
            }
            if(livingEntity instanceof Mob mob){
                return mob.getMobType() != ModMobTypes.CANNIBAL;
            }
            return false;
        })));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 50, 0.75f));
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, (livingEntity -> {
            return !livingEntity.isSpectator() && !((Player)livingEntity).isCreative() && !this.isAlone();
        })));
        //this.goalSelector.addGoal(9, new CraveAvoidPlayerGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.FOLLOW_RANGE, 40D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 8f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        SpawnGroupData spawngroupdata = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);

        RandomSource randomsource = pLevel.getRandom();

        this.populateDefaultEquipmentSlots(randomsource, pDifficulty);

        return spawngroupdata;
    }

    protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {

        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.SERRATED_SPEAR.get()));
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new WallClimberNavigation(this, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(IS_ALONE, false);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

        List<PathfinderMob> list = this.level().getEntitiesOfClass(PathfinderMob.class, this.getBoundingBox().inflate(20.0D), CANNIBAL_PREDICATE);
        list.remove(this);
        if(list.isEmpty()){
            this.setAlone(true);
            if(this.getTarget() instanceof Player){
                this.setTarget(null);
                this.setAggressive(false);
                /*Vec3 vec3 = DefaultRandomPos.getPosAway(this, 16, 7, player.position());
                if (vec3 != null && player.distanceToSqr(vec3.x, vec3.y, vec3.z) >= player.distanceToSqr(this)) {
                    Path path = this.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                    if(path != null){
                        this.getNavigation().moveTo(path, 1.1);
                    }
                }*/
            }
        } else {
            this.setAlone(false);
        }

        List<Player> players = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(20.0D), EntitySelector.NO_CREATIVE_OR_SPECTATOR);

        if(!players.isEmpty() && this.isAlone()){
            for(Player player : players){
                Vec3 vec3 = DefaultRandomPos.getPosAway(this, 16, 7, player.position());
                if (vec3 != null && player.distanceToSqr(vec3.x, vec3.y, vec3.z) >= player.distanceToSqr(this)) {
                    Path path = this.getNavigation().createPath(vec3.x, vec3.y, vec3.z, 0);
                    if(path != null){
                        this.getNavigation().moveTo(path, 1.1);
                    }
                }
            }
        }
    }

    @Override
    public boolean canAttack(LivingEntity pTarget) {
        return (!(pTarget instanceof Player) || !this.isAlone()) && super.canAttack(pTarget);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if(pEntity instanceof LivingEntity livingEntity && this.getItemBySlot(EquipmentSlot.MAINHAND).is(ModItems.SERRATED_SPEAR.get())){
            livingEntity.addEffect(new MobEffectInstance(ModEffects.VISCERAL_PAIN.get(), 90 + random.nextInt(20)), livingEntity);
        }
        return super.doHurtTarget(pEntity);
    }

    @Override
    protected float getEquipmentDropChance(EquipmentSlot pSlot) {
        return 0;
    }

    @Override
    public void aiStep() {
        if (this.level().random.nextInt(200) == 0) {
            imitateNearbyMobs(this.level(), this);
        }

        super.aiStep();
    }

    public static boolean imitateNearbyMobs(Level pLevel, Entity cannibal) {
        if (cannibal.isAlive() && ((CraveCannibal)cannibal).getTarget() == null && !cannibal.isSilent() && pLevel.random.nextInt(2) == 0) {
            //List<Mob> list = pLevel.getEntitiesOfClass(Mob.class, pParrot.getBoundingBox().inflate(20.0D), NOT_PARROT_PREDICATE);
            //if (!list.isEmpty()) {
                //Mob mob = list.get(pLevel.random.nextInt(list.size()));
            int biome = getBiomeIn(cannibal);

            SoundEvent soundevent = getImitatedSound(biome, cannibal);
            pLevel.playSound((Player)null, cannibal.getX(), cannibal.getY(), cannibal.getZ(), soundevent, cannibal.getSoundSource(), 0.9F, 0.825F);
            return true;

            //}

        } else {
            return false;
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.CRAVE_IDLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 600;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.CRAVE_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.CRAVE_HURT.get();
    }

    public static float getPitch(RandomSource pRandom) {
        return  1.0F - (pRandom.nextFloat() - pRandom.nextFloat()) * 0.15F;
    }

    public static int getBiomeIn(Entity cannibal){
        int biome = 0;
        if(cannibal.level().getBiome(cannibal.getOnPos()).is(BiomeTags.IS_JUNGLE)){
            biome = 1;
        }

        return biome;
    }

    private static SoundEvent getImitatedSound(int biome, Entity cannibal) {
        return MOB_SOUND_MAP.getOrDefault(
                cannibal.level().random.nextInt(biome == 1 ? 6 : 4),
                SoundEvents.PIG_AMBIENT);
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
     * false.
     */
    public void setClimbing(boolean pClimbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (pClimbing) {
            b0 = (byte)(b0 | 1);
        } else {
            b0 = (byte)(b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Override
    public @NotNull MobType getMobType() {
        return ModMobTypes.CANNIBAL;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height * 0.875f;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    class CraveMeleeAttackGoal extends MeleeAttackGoal {
        public CraveMeleeAttackGoal(CraveCannibal crave) {
            super(crave, 1.0D, true);
        }

        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
           return super.getAttackReachSqr(pAttackTarget) + 3.0f;
        }
    }
}
