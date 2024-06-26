package com.prohitman.unsortedcannibals.common.entities.projectile;

import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.FrenzyCannibal;
import com.prohitman.unsortedcannibals.core.init.ModEntities;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import com.prohitman.unsortedcannibals.core.init.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BlowDart extends AbstractArrow implements GeoAnimatable {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public BlowDart(EntityType<? extends BlowDart> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public BlowDart(Level pLevel, LivingEntity pShooter) {
        super(ModEntities.BLOW_DART.get(), pShooter, pLevel);
    }

    @Override
    protected @NotNull SoundEvent getDefaultHitGroundSoundEvent() {
        return super.getDefaultHitGroundSoundEvent();
    }

    @Override
    protected boolean tryPickup(Player pPlayer) {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();

    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if(entity instanceof PathfinderMob mob){
            if(mob.getMobType() == ModMobTypes.CANNIBAL && this.getOwner() instanceof FrenzyCannibal){
                return false;
            }
        }
        return super.canHitEntity(entity);
    }

    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.BLOW_DART.get());
    }

    protected void doPostHurtEffects(LivingEntity pLiving) {
        super.doPostHurtEffects(pLiving);
        MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.BLINDNESS, 100, 0);
        MobEffectInstance mobeffectinstance1 = new MobEffectInstance(MobEffects.POISON, 80, 1);

        pLiving.addEffect(mobeffectinstance, this.getEffectSource());
        pLiving.addEffect(mobeffectinstance1, this.getEffectSource());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);

    }

    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    @Override
    public double getTick(Object object) {
        return ((Entity)object).tickCount;
    }
}
