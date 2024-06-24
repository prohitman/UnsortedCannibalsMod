package com.prohitman.unsortedcannibals.common.entities.living.goals;

import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.List;

public class CannibalFollowItemGoal extends Goal {
    private PathfinderMob cannibal;

    public CannibalFollowItemGoal(PathfinderMob mob) {
        this.cannibal = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        if (!cannibal.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            return false;
        } else {
            List<ItemEntity> list = cannibal.level().getEntitiesOfClass(ItemEntity.class, cannibal.getBoundingBox().inflate(10.0D, 10.0D, 10.0D), YearnCannibal.ALLOWED_ITEMS);
            return !list.isEmpty() && cannibal.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        List<ItemEntity> list = cannibal.level().getEntitiesOfClass(ItemEntity.class, cannibal.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), YearnCannibal.ALLOWED_ITEMS);
        ItemStack itemstack = cannibal.getItemBySlot(EquipmentSlot.MAINHAND);
        if (itemstack.isEmpty() && !list.isEmpty()) {
            cannibal.getNavigation().moveTo(list.get(0), (double)1.015F);
        }

    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        List<ItemEntity> list = cannibal.level().getEntitiesOfClass(ItemEntity.class, cannibal.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), YearnCannibal.ALLOWED_ITEMS);
        if (!list.isEmpty()) {
            cannibal.getNavigation().moveTo(list.get(0), (double)1.015F);
        }

    }

}
