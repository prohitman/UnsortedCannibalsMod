package com.prohitman.unsortedcannibals.common.entities.living;

import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.goals.CraveAvoidPlayerGoal;
import com.prohitman.unsortedcannibals.common.entities.living.goals.FollowCannibalGoal;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import com.prohitman.unsortedcannibals.core.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Predicate;

public class YearnCannibal extends PathfinderMob implements GeoEntity {
    public static final Ingredient FOOD_ITEMS = Ingredient.of(ModItems.REEKING_FLESH.get(), ModItems.SEVERED_NOSE.get());
    public static final Predicate<ItemEntity> ALLOWED_ITEMS = (itemEntity) -> {
        return !itemEntity.hasPickUpDelay() && itemEntity.isAlive() && FOOD_ITEMS.test(itemEntity.getItem());
    };
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public YearnCannibal(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(1, new FollowCannibalGoal(this, 0.85D, 6.0F, 12.0F));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 5, 0.25f));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Camel.class, 10, true, false, (livingEntity -> true)));
        this.goalSelector.addGoal(5, new TemptGoal(this, 0.85D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new MoveToBlockGoal(this, 0.75F, 10, 3) {
            @Override
            protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
                BlockState state = pLevel.getBlockState(pPos);
                return state.getLightEmission(pLevel, pPos) > 4;
            }
        });
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 50D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ARMOR_TOUGHNESS, 6f)
                .add(Attributes.ATTACK_DAMAGE, 10f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f);
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return pDimensions.height * 0.9f;
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if(super.doHurtTarget(pEntity) && pEntity instanceof LivingEntity livingEntity){
            if(pEntity.level().random.nextInt(5) == 0){
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
