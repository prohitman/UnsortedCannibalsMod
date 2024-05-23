package com.prohitman.unsortedcannibals.core.init;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.effects.LiveBaitEffect;
import com.prohitman.unsortedcannibals.common.effects.ShatteredBones;
import com.prohitman.unsortedcannibals.common.effects.VisceralPainEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, UnsortedCannibalsMod.MODID);

    public static final RegistryObject<MobEffect> VISCERAL_PAIN = MOB_EFFECTS.register("visceral_pain",
            VisceralPainEffect::new);
    public static final RegistryObject<MobEffect> LIVE_BAIT = MOB_EFFECTS.register("live_bait",
            LiveBaitEffect::new);

    public static final RegistryObject<MobEffect> SHATTERED_BONES = MOB_EFFECTS.register("shattered_bones", () ->
            new ShatteredBones().addAttributeModifier(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", (double)-3F, AttributeModifier.Operation.MULTIPLY_TOTAL));

}
