package com.prohitman.unsortedcannibals.common.entities.living;

import com.google.common.collect.Maps;
import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.goals.CraveMeleeAttackGoal;
import com.prohitman.unsortedcannibals.common.entities.living.goals.FollowCannibalGoal;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import com.prohitman.unsortedcannibals.core.init.ModSounds;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
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
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.CryingObsidianBlock;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.archivers.sevenz.CLI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CraveCannibal extends PatrollingCannibal implements GeoEntity, Enemy {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(CraveCannibal.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> IS_ALONE = SynchedEntityData.defineId(CraveCannibal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_RUNNING = SynchedEntityData.defineId(CraveCannibal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(CraveCannibal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_MOVING = SynchedEntityData.defineId(CraveCannibal.class, EntityDataSerializers.BOOLEAN);

    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("Walk");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("Idle");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("Run");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenLoop("Attack");
    protected static final RawAnimation CLIMB_ANIM = RawAnimation.begin().thenLoop("Climb3");

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

    public boolean isRunning() {
        return this.entityData.get(IS_RUNNING);
    }
    public void setIsRunning(boolean is_running) {
        this.entityData.set(IS_RUNNING, is_running);
    }
    public void setAttacking(boolean attacking) {
        this.entityData.set(IS_ATTACKING, attacking);
    }
    public boolean isAttacking() {
        return this.entityData.get(IS_ATTACKING);
    }

    public void setMoving(boolean moving) {
        this.entityData.set(IS_MOVING, moving);
    }
    public boolean isMoving() {
        return this.entityData.get(IS_MOVING);
    }

    public int attackAnimationTimeout = 0;
    public boolean shouldStartAnim = false;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CraveMeleeAttackGoal(this, 0.9D, true, 12, 25, 3));
        this.goalSelector.addGoal(1, new FollowCannibalGoal(this, 0.55D, 6.0F, 25f));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (livingEntity -> {
            if(livingEntity instanceof Mob mob){
                return mob.getMobType() != ModMobTypes.CANNIBAL && !mob.isUnderWater();
            }
            return !(this.getTarget() instanceof Player);
        })));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 50, 0.75f));
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, (livingEntity -> {
            return !livingEntity.isSpectator() && !((Player)livingEntity).isCreative() && !this.isAlone();
        })));
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

    @Override
    public boolean isPersistenceRequired() {
        return !this.isPatrolling();
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
        this.entityData.define(IS_RUNNING, false);
        this.entityData.define(IS_ATTACKING, false);
        this.entityData.define(IS_MOVING, false);

    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public static boolean checkCraveSpawnRules(EntityType<? extends CraveCannibal> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getBlockState(pPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && pLevel.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }
    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
            this.setIsRunning(this.moveControl.getSpeedModifier() > 0.75);
            this.checkIsMoving();
        } else {
            this.setupAttackAnimation();
        }

        List<PathfinderMob> list = this.level().getEntitiesOfClass(PathfinderMob.class, this.getBoundingBox().inflate(20.0D), CANNIBAL_PREDICATE);
        list.remove(this);
        if(list.isEmpty() && this.isPatrolling()){
            this.setAlone(true);
            if(this.getTarget() instanceof Player){
                this.setTarget(null);
                this.setAggressive(false);
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
                        this.setAggressive(false);
                        this.setTarget(null);
                    }
                }
            }
        }
    }

    private void setupAttackAnimation() {
        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 25;
            shouldStartAnim = true;
        } else {
            --this.attackAnimationTimeout;
        }

        if(!this.isAttacking()) {
            shouldStartAnim = false;
        }
    }


    @Override
    public boolean shouldDropExperience() {
        return true;
    }

    protected SoundEvent getSwimSound() {
        return SoundEvents.HOSTILE_SWIM;
    }

    protected SoundEvent getSwimSplashSound() {
        return SoundEvents.HOSTILE_SPLASH;
    }

    @Override
    public boolean canAttack(LivingEntity pTarget) {
        return (!(pTarget instanceof Player) || !this.isAlone()) && super.canAttack(pTarget);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if(pEntity instanceof LivingEntity livingEntity && this.getItemBySlot(EquipmentSlot.MAINHAND).is(ModItems.SERRATED_SPEAR.get())){
            if(!(livingEntity.getUseItem().is(Items.SHIELD) && livingEntity.isUsingItem())){
                livingEntity.addEffect(new MobEffectInstance(ModEffects.VISCERAL_PAIN.get(), 90 + random.nextInt(20)), livingEntity);
            }
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
            int biome = getBiomeIn(cannibal);

            SoundEvent soundevent = getImitatedSound(biome, cannibal);
            pLevel.playSound((Player)null, cannibal.getX(), cannibal.getY(), cannibal.getZ(), soundevent, cannibal.getSoundSource(), 0.9F, 0.825F);
            return true;
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
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk", 4, this::walkAnimController));
    }

    private PlayState walkAnimController(AnimationState<CraveCannibal> state) {
        if(shouldStartAnim){
            return state.setAndContinue(ATTACK_ANIM);
        }
        else if (this.isClimbing() && this.isMoving()){
            return state.setAndContinue(CLIMB_ANIM);
        }
        else if(this.isRunning() && state.isMoving()){
            return state.setAndContinue(RUN_ANIM);
        }
        else if (state.isMoving()){
            return state.setAndContinue(WALK_ANIM);
        }

        return state.setAndContinue(IDLE_ANIM);
    }

    public void checkIsMoving(){
        CompoundTag entityData = this.getPersistentData();

        CompoundTag position = entityData.getCompound("position");

        double oldX = position.getDouble("posX");
        double oldY = position.getDouble("posY");
        double oldZ = position.getDouble("posZ");

        Vec3 oldPos = new Vec3(oldX, oldY, oldZ);
        Vec3 currentPos = this.position();

        position.putDouble("posX", this.position().x);
        position.putDouble("posY", this.position().y);
        position.putDouble("posZ", this.position().z);

        entityData.put("position", position);

        this.setMoving(!currentPos.equals(oldPos));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
