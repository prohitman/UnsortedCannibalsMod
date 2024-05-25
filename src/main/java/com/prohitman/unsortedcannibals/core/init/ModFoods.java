package com.prohitman.unsortedcannibals.core.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public class ModFoods {
    public static final FoodProperties SEVERED_NOSE = (new FoodProperties.Builder()).nutrition(3).effect(new MobEffectInstance(MobEffects.HUNGER, 160, 0), 1F).meat().build();
    public static final FoodProperties REEKING_FLESH = (new FoodProperties.Builder()).nutrition(4).effect(new MobEffectInstance(MobEffects.CONFUSION, 240, 1), 1F).meat().build();

}
