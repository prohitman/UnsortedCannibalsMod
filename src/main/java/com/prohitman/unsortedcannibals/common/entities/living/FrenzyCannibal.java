package com.prohitman.unsortedcannibals.common.entities.living;

import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.goals.FollowCannibalGoal;
import com.prohitman.unsortedcannibals.common.entities.living.goals.RangedFrenzyAttackGoal;
import com.prohitman.unsortedcannibals.common.entities.projectile.BlowDart;
import com.prohitman.unsortedcannibals.core.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class FrenzyCannibal extends PathfinderMob implements GeoEntity, RangedAttackMob, Enemy {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final EntityDataAccessor<Boolean> IS_SHOOTING = SynchedEntityData.defineId(FrenzyCannibal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RUNNING = SynchedEntityData.defineId(FrenzyCannibal.class, EntityDataSerializers.BOOLEAN);
    protected static final RawAnimation SHOOTING_ANIM = RawAnimation.begin().thenLoop("Shoot2");
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("Walk");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("Run");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("Idle");
    public int shootAnimationTimeout = 0;

    public FrenzyCannibal(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.xpReward = 10;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedFrenzyAttackGoal(this, 1.1D, 30, 8.5F));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, FrenzyCannibal.class, YearnCannibal.class, CraveCannibal.class));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 25, 0.35f));
        /*this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Mob.class, 0, true, false, (livingEntity -> {
            if(livingEntity instanceof Mob mob){
                return mob.getMobType() != ModMobTypes.CANNIBAL && !mob.isUnderWater();
            }
            return false;
        })));
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, (livingEntity -> {
            return !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
        })));*/
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, false, false, (livingEntity -> {
            if(livingEntity instanceof Mob mob){
                return mob.getMobType() != ModMobTypes.CANNIBAL && !mob.isUnderWater();
            }
            return true;
        })));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 30D)
                .add(Attributes.ARMOR_TOUGHNESS, 1f)
                .add(Attributes.ATTACK_DAMAGE, 5f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.25f);
    }

    public boolean isShooting() {
        return this.entityData.get(IS_SHOOTING);
    }

    public void setIsShooting(boolean is_shooting) {
        this.entityData.set(IS_SHOOTING, is_shooting);
    }
    public boolean isRunning() {
        return this.entityData.get(IS_RUNNING);
    }

    public void setIsRunning(boolean is_running) {
        this.entityData.set(IS_RUNNING, is_running);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_RUNNING, false);
        this.entityData.define(IS_SHOOTING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("is_shooting", this.entityData.get(IS_SHOOTING));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(IS_SHOOTING, pCompound.getBoolean("is_shooting"));

    }

    public static boolean checkFrenzySpawnRules(EntityType<? extends FrenzyCannibal> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        /*if(pSpawnType == MobSpawnType.NATURAL){
            boolean isOnSpruce = pLevel.getBlockState(pPos.below()).is(Blocks.SPRUCE_STAIRS);
            //boolean isOnSpruce = pLevel.getBlockState(pPos.below()).is(Blocks.SPRUCE_STAIRS);
            boolean isUnderRoots = pLevel.getBlockState(pPos.above(2)).is(Blocks.HANGING_ROOTS);
            System.out.println("Trying to spawn!" + isOnSpruce + pLevel.getBlockState(pPos).is(Blocks.SPRUCE_TRAPDOOR));
            return isOnSpruce && pLevel.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
        }*/
        return (pLevel.getBlockState(pPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) || pLevel.getBlockState(pPos.below()).is(BlockTags.JUNGLE_LOGS)) && pLevel.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }
    @Override
    public MobType getMobType() {
        return ModMobTypes.CANNIBAL;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.FRENZY_IDLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 100;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }
    @Override
    public boolean shouldDropExperience() {
        return true;
    }
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.FRENZY_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.FRENZY_HURT.get();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk/Run/Idle", 3, this::walkAnimController));
        controllers.add(new AnimationController<>(this, "Shoot", 1, this::shootAnimController));

    }

    @Override
    public void tick() {
        super.tick();
        if(!this.level().isClientSide){
            this.setIsRunning(this.moveControl.getSpeedModifier() >= 0.75);
        }

        //this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 3, 1));
    }

    private PlayState walkAnimController(AnimationState<FrenzyCannibal> state) {
        if(this.isRunning() && state.isMoving()){
            return state.setAndContinue(RUN_ANIM);
        }
        else if (state.isMoving()){
            return state.setAndContinue(WALK_ANIM);
        }
        else{
            return state.setAndContinue(IDLE_ANIM);
        }
    }

    private PlayState shootAnimController(AnimationState<FrenzyCannibal> state) {
        return this.isShooting() ? state.setAndContinue(SHOOTING_ANIM) : PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
        BlowDart dart = new BlowDart(this.level(), this);
        double d0 = pTarget.getEyeY() - (double)1.25F;
        double d1 = pTarget.getX() - this.getX();
        double d2 = d0 - dart.getY();
        double d3 = pTarget.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        dart.shoot(d1, d2 + d4, d3, 1.75F, 12.0F);
        this.playSound(ModSounds.BLOWGUN_SHOOT.get(), 1.35F, 1/*0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F)*/);
        this.level().addFreshEntity(dart);
    }
}
