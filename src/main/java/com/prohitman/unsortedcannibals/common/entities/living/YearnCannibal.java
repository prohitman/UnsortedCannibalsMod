package com.prohitman.unsortedcannibals.common.entities.living;

import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.goals.*;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import com.prohitman.unsortedcannibals.core.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.client.renderer.entity.GremlinRenderer;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Predicate;

public class YearnCannibal extends PathfinderMob implements GeoEntity, Enemy {
    protected static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("Walk");
    protected static final RawAnimation EATING_ANIM = RawAnimation.begin().thenLoop("Eating2");
    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("Idle");
    protected static final RawAnimation RUN_ANIM = RawAnimation.begin().thenLoop("Run");
    protected static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenLoop("Attack");

    private static final EntityDataAccessor<Boolean> IS_ATTACKING = SynchedEntityData.defineId(YearnCannibal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(YearnCannibal.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_RUNNING = SynchedEntityData.defineId(YearnCannibal.class, EntityDataSerializers.BOOLEAN);

    public static final Ingredient FOOD_ITEMS = Ingredient.of(ModItems.REEKING_FLESH.get(), ModItems.SEVERED_NOSE.get());
    public static final Predicate<ItemEntity> ALLOWED_ITEMS = (itemEntity) -> {
        return !itemEntity.hasPickUpDelay() && itemEntity.isAlive() && FOOD_ITEMS.test(itemEntity.getItem());
    };
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public int attackAnimationTimeout = 0;
    public boolean shouldStartAnim = false;

    public YearnCannibal(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(1);
        this.setCanPickUpLoot(true);
    }

    public boolean isRunning() {
        return this.entityData.get(IS_RUNNING);
    }

    public void setIsRunning(boolean is_running) {
        this.entityData.set(IS_RUNNING, is_running);
    }

    public boolean canTakeItem(ItemStack pItemstack) {
        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(pItemstack);
        if (!this.getItemBySlot(equipmentslot).isEmpty()) {
            return false;
        } else {
            return equipmentslot == EquipmentSlot.MAINHAND && super.canTakeItem(pItemstack);
        }
    }
    @Override
    public boolean isPersistenceRequired() {
        return true;
    }


    public boolean isEating() {
        return this.entityData.get(EAT_COUNTER) > 0;
    }

    public void eat(boolean pEating) {
        this.entityData.set(EAT_COUNTER, pEating ? 1 : 0);
    }

    private int getEatCounter() {
        return this.entityData.get(EAT_COUNTER);
    }

    private void setEatCounter(int pEatCounter) {
        this.entityData.set(EAT_COUNTER, pEatCounter);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(IS_ATTACKING, attacking);
    }

    public boolean isAttacking() {
        return this.entityData.get(IS_ATTACKING);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EAT_COUNTER, 0);
        this.entityData.define(IS_RUNNING, false);
        this.entityData.define(IS_ATTACKING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CannibalMeleeAttackGoal(this, 1.2D, true, 14, 20, 0.5));
        //this.goalSelector.addGoal(1, new FollowCannibalGoal(this, 0.85D, 6.0F, 12.0F));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, FrenzyCannibal.class, YearnCannibal.class, CraveCannibal.class));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 5, 0.25f));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Mob.class, 5, true, false, (livingEntity -> {
            if(livingEntity instanceof Mob mob){
                return mob.getMobType() != ModMobTypes.CANNIBAL && !mob.isUnderWater();
            }
            return false;
        })));
        this.goalSelector.addGoal(7, new MoveToBlockGoal(this, 0.5F, 10, 3) {
            @Override
            protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
                BlockState state = pLevel.getBlockState(pPos);
                return state.getLightEmission(pLevel, pPos) > 4 && this.mob.distanceToSqr(pPos.getX(), pPos.getY(), pPos.getZ()) > 16;
            }
        });
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, (livingEntity -> {
            return !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
        })));
        this.goalSelector.addGoal(9, new CannibalTemptGoal(this, 0.75D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(9, new CannibalFollowItemGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 55D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ARMOR_TOUGHNESS, 3f)
                .add(Attributes.ATTACK_DAMAGE, 20f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f);
    }

    @Override
    public void tick() {
        super.tick();
        this.handleEating();

        if(!this.level().isClientSide){
            this.setIsRunning(this.moveControl.getSpeedModifier() >= 0.75);
        } else {
            this.setupAttackAnimation();
        }
    }

    private void setupAttackAnimation() {
        if(this.isAttacking() && attackAnimationTimeout <= 0) {
            attackAnimationTimeout = 20;
            shouldStartAnim = true;
        } else {
            --this.attackAnimationTimeout;
        }

        if(!this.isAttacking()) {
            shouldStartAnim = false;
        }
    }

    private void handleEating() {
        if (!this.isEating() && !this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1 && !this.level().isClientSide) {
            this.eat(true);
        } else if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && !this.level().isClientSide) {
            this.eat(false);
        }

        if (this.isEating()) {
            this.addEatingParticles();
            this.getNavigation().stop();
            this.setZza(0);
            if (!this.level().isClientSide && this.getEatCounter() > 20 && this.random.nextInt(5) == 1) {
                if (this.getEatCounter() > 45 && FOOD_ITEMS.test(this.getItemBySlot(EquipmentSlot.MAINHAND))) {
                    if (!this.level().isClientSide) {

                        int num = this.getItemBySlot(EquipmentSlot.MAINHAND).getCount();
                        this.heal(num);

                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.gameEvent(GameEvent.EAT);

                        this.eat(false);

                        return;
                    }
                }
            }
            this.setEatCounter(this.getEatCounter() + 1);
        }

    }

    private void addEatingParticles() {
        if (this.getEatCounter() % 5 == 0) {
            this.playSound(ModSounds.YEARN_EATING.get(), 0.2F + 0.1F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            for(int i = 0; i < 6; ++i) {
                Vec3 vec3 = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double)this.random.nextFloat() - 0.5D) * 0.1D);
                vec3 = vec3.xRot(-this.getXRot() * ((float)Math.PI / 180F));
                vec3 = vec3.yRot(-this.getYRot() * ((float)Math.PI / 180F));
                double d0 = (double)(-this.random.nextFloat()) * 0.6D - 0.3D;
                Vec3 vec31 = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.4D, d0, 0.5D + ((double)this.random.nextFloat() - 0.5D) * 0.2D);
                vec31 = vec31.yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                vec31 = vec31.add(this.getX(), this.getEyeY() - 0.085D, this.getZ());
                this.level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemBySlot(EquipmentSlot.MAINHAND)), vec31.x, vec31.y, vec31.z, vec3.x, vec3.y + 0.05D, vec3.z);
            }
        }

    }

    protected void pickUpItem(ItemEntity pItemEntity) {
        if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && ALLOWED_ITEMS.test(pItemEntity)) {
            this.onItemPickup(pItemEntity);
            ItemStack itemstack = pItemEntity.getItem();
            this.setItemSlot(EquipmentSlot.MAINHAND, itemstack);
            this.setGuaranteedDrop(EquipmentSlot.MAINHAND);
            this.take(pItemEntity, itemstack.getCount());
            pItemEntity.discard();
        }

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

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height * 0.9f;
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if(super.doHurtTarget(pEntity) && pEntity instanceof LivingEntity livingEntity){
            if(pEntity.level().random.nextInt(4) == 0){
                livingEntity.addEffect(new MobEffectInstance(ModEffects.SHATTERED_BONES.get(), 45 + random.nextInt(20)), livingEntity);
                livingEntity.level().playSound(livingEntity, livingEntity.blockPosition(), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.BLOCKS, 1, 1);
            }
            Vec3 vector3d = (new Vec3(this.getX() - livingEntity.getX(), this.getY() - livingEntity.getY(), this.getZ() - livingEntity.getZ())).scale(0.5D);
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(vector3d));

            this.heal(2);

            return true;
        }

        return false;
    }

    @Override
    public MobType getMobType() {
        return ModMobTypes.CANNIBAL;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 400;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.YEARN_IDLE.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.YEARN_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.YEARN_HURT.get();
    }


    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Walk", 3, this::walkAnimController));
        //controllers.add(new AnimationController<>(this, "Eating", 3, this::eatingAnimController));

    }


    private PlayState walkAnimController(AnimationState<YearnCannibal> state) {
        if(state.isMoving()){
            System.out.println("I am moving");
        }
        if(shouldStartAnim){
            return state.setAndContinue(ATTACK_ANIM);
        }
        else if(this.isEating()){
            return state.setAndContinue(EATING_ANIM);
        }
        else if(this.isRunning() && state.isMoving()){
            return state.setAndContinue(RUN_ANIM);
        }
        else if (state.isMoving()){
            return state.setAndContinue(WALK_ANIM);
        }

        return state.setAndContinue(IDLE_ANIM);
    }

    /*private PlayState attackAnimController(AnimationState<YearnCannibal> state) {
        if (this.isAttacking() && this.attackAnimationTimeout <=0)
            return state.setAndContinue(ATTACK_ANIM);
        else {
            --this.attackAnimationTimeout;
        }
        if()
        return PlayState.STOP;
    }*/

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
