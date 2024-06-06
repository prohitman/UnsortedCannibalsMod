package com.prohitman.unsortedcannibals.common.entities.living;

import com.google.common.collect.Maps;
import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.goals.CraveAvoidPlayerGoal;
import com.prohitman.unsortedcannibals.common.entities.living.goals.FollowCannibalGoal;
import net.minecraft.Util;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class CraveCannibal extends PathfinderMob implements GeoEntity {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Spider.class, EntityDataSerializers.BYTE);

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


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new FollowCannibalGoal(this, 0.7D, 6.0F, 12.0F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.5D));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 20, 0.5f));

        this.goalSelector.addGoal(9, new CraveAvoidPlayerGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes().add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ARMOR_TOUGHNESS, 0.1f)
                .add(Attributes.ATTACK_DAMAGE, 4f)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5f);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new WallClimberNavigation(this, pLevel);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
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

    }

    @Override
    public void aiStep() {
        if (this.level().random.nextInt(150) == 0) {
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
}
