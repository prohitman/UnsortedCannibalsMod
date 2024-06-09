package com.prohitman.unsortedcannibals.common.entities.living.goals;

import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class CraveAvoidPlayerGoal extends AvoidEntityGoal<Player> {
    //private CraveCannibal cannibal;
    private final double maxDist = 10;
    private final float sprintSpeedModifier = 1.25f;
    private final float walkSpeedModifier = 1.25f;

    public static final Predicate<LivingEntity> NO_CREATIVE_OR_SPECTATOR = (p_20436_) -> {
        return !(p_20436_ instanceof Player) || !p_20436_.isSpectator() && !((Player)p_20436_).isCreative();
    };

    private final TargetingConditions avoidEntityTargeting = TargetingConditions.forCombat().range(maxDist).selector(NO_CREATIVE_OR_SPECTATOR);

    private static final Predicate<Mob> CANNIBAL_PREDICATE =
            mob -> mob != null && mob.getMobType() == ModMobTypes.CANNIBAL;


    public CraveAvoidPlayerGoal(CraveCannibal pMob) {
        super(pMob, Player.class, 15, 1.25f, 1.25F);

        //this.cannibal = pMob;
    }

    public boolean canUse() {
        this.toAvoid = this.mob.level().getNearestEntity(this.mob.level().getEntitiesOfClass(this.avoidClass, this.mob.getBoundingBox().inflate((double)this.maxDist, 3.0D, (double)this.maxDist), (p_148078_) -> {
            return true;
        }), avoidEntityTargeting, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());

        List<Mob> list = mob.level().getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(20.0D));

        for(Mob mob1 : list){
            if(mob1.getMobType() == ModMobTypes.CANNIBAL && mob1 != mob){
                return false;
            }
        }

        if (this.toAvoid == null) {
            return false;
        } else {
            Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.position());
            if (vec3 == null) {
                return false;
            } else if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr(this.mob)) {
                return false;
            } else {
                this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                if(this.path != null){
                    this.mob.setTarget(null);
                }
                return this.path != null;
            }
        }
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        List<Mob> list = mob.level().getEntitiesOfClass(Mob.class, mob.getBoundingBox().inflate(20.0D), CANNIBAL_PREDICATE);

        for(Mob mob1 : list){
            if(mob1.getMobType() == ModMobTypes.CANNIBAL){
                return false;
            }
        }

        return !this.pathNav.isDone();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.pathNav.moveTo(this.path, this.walkSpeedModifier);
        this.mob.setTarget(null);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.toAvoid = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.mob.distanceToSqr(this.toAvoid) < 49.0D) {
            this.mob.getNavigation().setSpeedModifier(this.sprintSpeedModifier);
        } else {
            this.mob.getNavigation().setSpeedModifier(this.walkSpeedModifier);
        }
        this.mob.setTarget(null);
    }
}
