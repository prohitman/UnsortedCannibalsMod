package com.prohitman.unsortedcannibals.common.entities.living.goals;

import com.prohitman.unsortedcannibals.common.entities.ModMobTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class FollowCannibalGoal extends Goal {
    private final Mob mob;
    private final Predicate<Mob> followPredicate;
    @Nullable
    private Mob followingMob;
    private final double speedModifier;
    private final PathNavigation navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private float oldWaterCost;
    private final float areaSize;

    /**
     * Constructs a goal allowing a mob to follow others. The mob must have Ground or Flying navigation.
     */
    public FollowCannibalGoal(Mob pMob, double pSpeedModifier, float pStopDistance, float pAreaSize) {
        this.mob = pMob;
        this.followPredicate = Objects::nonNull;
        this.speedModifier = pSpeedModifier;
        this.navigation = pMob.getNavigation();
        this.stopDistance = pStopDistance;
        this.areaSize = pAreaSize;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(pMob.getNavigation() instanceof GroundPathNavigation) && !(pMob.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        List<Mob> list = this.mob.level().getEntitiesOfClass(Mob.class, this.mob.getBoundingBox().inflate((double)this.areaSize), this.followPredicate);
        if (!list.isEmpty()) {
            for(Mob mob : list) {
                if (!mob.isInvisible() && mob.getMobType() == ModMobTypes.CANNIBAL && mob != this.mob) {
                    if (mob.distanceToSqr(this.mob) > (double)(this.stopDistance * this.stopDistance)) {
                        this.followingMob = mob;
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.followingMob != null /*&& !this.navigation.isDone()*/ && this.mob.distanceToSqr(this.followingMob) > (double)(this.stopDistance * this.stopDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public boolean isInterruptable() {
        return true;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.followingMob = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        if (this.followingMob != null && !this.mob.isLeashed()) {
            this.mob.getLookControl().setLookAt(this.followingMob, 10.0F, (float)this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                double d0 = this.mob.getX() - this.followingMob.getX();
                double d1 = this.mob.getY() - this.followingMob.getY();
                double d2 = this.mob.getZ() - this.followingMob.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                if (!(d3 <= (double)(this.stopDistance * this.stopDistance))) {
                    this.navigation.moveTo(this.followingMob, this.speedModifier);
                } else {
                    this.navigation.stop();
                    LookControl lookcontrol = this.followingMob.getLookControl();
                    if (d3 <= (double)this.stopDistance || lookcontrol.getWantedX() == this.mob.getX() && lookcontrol.getWantedY() == this.mob.getY() && lookcontrol.getWantedZ() == this.mob.getZ()) {
                        double d4 = this.followingMob.getX() - this.mob.getX();
                        double d5 = this.followingMob.getZ() - this.mob.getZ();
                        this.navigation.moveTo(this.mob.getX() - d4, this.mob.getY(), this.mob.getZ() - d5, this.speedModifier);
                    }

                }
            }
        }
    }
}
