package com.prohitman.unsortedcannibals.core.init;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.DummyCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import com.prohitman.unsortedcannibals.common.entities.projectile.BlowDart;
import com.prohitman.unsortedcannibals.common.entities.projectile.ThrownSpear;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, UnsortedCannibalsMod.MODID);

    public static final RegistryObject<EntityType<ThrownSpear>> SERRATED_SPEAR = ENTITY_TYPES.register("serrated_spear",
            () -> EntityType.Builder.<ThrownSpear>of(ThrownSpear::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("serrated_spear"));

    public static final RegistryObject<EntityType<BlowDart>> BLOW_DART = ENTITY_TYPES.register("blow_dart",
            () -> EntityType.Builder.<BlowDart>of(BlowDart::new, MobCategory.MISC).sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(20).build("blow_dart"));

    public static final RegistryObject<EntityType<CraveCannibal>> CRAVE = ENTITY_TYPES.register("crave",
            () -> EntityType.Builder.of(CraveCannibal::new, MobCategory.MONSTER).sized(0.82F, 2.68F).clientTrackingRange(20).build("crave"));

    public static final RegistryObject<EntityType<YearnCannibal>> YEARN = ENTITY_TYPES.register("yearn",
            () -> EntityType.Builder.of(YearnCannibal::new, MobCategory.MONSTER).sized(1.65F, 2.85F).clientTrackingRange(20).build("yearn"));

    public static final RegistryObject<EntityType<DummyCannibal>> DUMMY_CANNIBAL = ENTITY_TYPES.register("dummy_cannibal",
            () -> EntityType.Builder.of(DummyCannibal::new, MobCategory.CREATURE).sized(1.5F, 1.5F).clientTrackingRange(4).updateInterval(20).build("dummy_cannibal"));
}
