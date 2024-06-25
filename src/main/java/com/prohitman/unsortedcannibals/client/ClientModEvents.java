package com.prohitman.unsortedcannibals.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.client.keybindings.ModKeyBindings;
import com.prohitman.unsortedcannibals.client.renderer.entity.living.CraveRenderer;
import com.prohitman.unsortedcannibals.client.renderer.entity.living.DummyCannibalRenderer;
import com.prohitman.unsortedcannibals.client.renderer.entity.living.FrenzyRenderer;
import com.prohitman.unsortedcannibals.client.renderer.entity.living.YearnRenderer;
import com.prohitman.unsortedcannibals.client.renderer.entity.projectile.BlowDartRenderer;
import com.prohitman.unsortedcannibals.client.renderer.entity.projectile.ThrownSpearRenderer;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.DummyCannibal;
import com.prohitman.unsortedcannibals.common.particles.BloodParticle;
import com.prohitman.unsortedcannibals.core.init.*;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    private static long effectStartTime = -1;
    private static int totalDuration = -1;
    private static int fadeDuration = -1;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {


            EntityRenderers.register(ModEntities.SERRATED_SPEAR.get(), ThrownSpearRenderer::new);
            EntityRenderers.register(ModEntities.BLOW_DART.get(), BlowDartRenderer::new);
            EntityRenderers.register(ModEntities.DUMMY_CANNIBAL.get(), DummyCannibalRenderer::new);
            EntityRenderers.register(ModEntities.CRAVE.get(), CraveRenderer::new);
            EntityRenderers.register(ModEntities.YEARN.get(), YearnRenderer::new);
            EntityRenderers.register(ModEntities.FRENZY.get(), FrenzyRenderer::new);

            ItemProperties.register(ModItems.SERRATED_SPEAR.get(), new ResourceLocation(UnsortedCannibalsMod.MODID, "throwing"), (stack, level, living, j) -> {
                return living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F;
            });

            ItemProperties.register(ModItems.DEATH_WHISTLE.get(), new ResourceLocation(UnsortedCannibalsMod.MODID, "tooting"), (stack, level, living, j) -> {
                return living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F;
            });

            ItemProperties.register(ModItems.BLOWGUN.get(), new ResourceLocation(UnsortedCannibalsMod.MODID, "blowing"), (stack, level, living, j) -> {
                return living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F;
            });
        });
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(ModParticles.SINISTER_SKULL_PARTICLE.get(), SoulParticle.EmissiveProvider::new);
        event.registerSpriteSet(ModParticles.BLOOD_PARTICLE.get(), BloodParticle.Provider::new);

    }

    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event){
        event.register(ModKeyBindings.INSTANCE.detailsKey);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelowAll("visceral_pain", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);

            if (gui.getMinecraft().player.hasEffect(ModEffects.VISCERAL_PAIN.get())) {
                if (effectStartTime == -1) {
                    effectStartTime = gui.getMinecraft().player.tickCount;

                    totalDuration = gui.getMinecraft().player.getEffect(ModEffects.VISCERAL_PAIN.get()).getDuration();
                    fadeDuration = totalDuration / 3;
                }

                long currentTime = gui.getMinecraft().player.tickCount;
                float alpha = getAlpha(currentTime, partialTick);

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
                guiGraphics.blit(new ResourceLocation(UnsortedCannibalsMod.MODID, "textures/misc/visceral_pain_outline.png"), 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                effectStartTime = -1;
                totalDuration = -1;
                fadeDuration = -1;
            }
        });
    }

    private static float getAlpha(long currentTime, float partialTicks) {
        if (effectStartTime == -1 || totalDuration == -1 || fadeDuration == -1) {
            return 0.0F;
        }

        long elapsedTime = currentTime - effectStartTime;
        float totalElapsedTicks = elapsedTime + partialTicks;

        if (totalElapsedTicks < fadeDuration) {
            return totalElapsedTicks / fadeDuration;
        }

        if (totalElapsedTicks < totalDuration - fadeDuration) {
            return 1.0F;
        }

        if (totalElapsedTicks <= totalDuration) {
            float fadeOutElapsedTicks = totalElapsedTicks - (totalDuration - fadeDuration);
            return 1.0F - (fadeOutElapsedTicks / fadeDuration);
        }

        return 0.0F;
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onBlockColorHandlerEvent(RegisterColorHandlersEvent.Block event) {
        event.register((state, blockAndTintGetter, blockPos, tintIndex) -> {
            if(tintIndex == 0){
                return blockAndTintGetter != null && blockPos != null ? BiomeColors.getAverageGrassColor(blockAndTintGetter, blockPos) : GrassColor.getDefaultColor();
            }
            else{
                return -1;
            }
        }, ModBlocks.FALL_TRAP.get());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onItemColorHandlerEvent(RegisterColorHandlersEvent.Item event) {
        BlockColors blockColors = event.getBlockColors();
        event.register((stack, tintIndex) -> {
            if(tintIndex == 0){
                BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
                return blockColors.getColor(state, null, null, tintIndex);
            }
            else{
                return -1;
            }
        }, ModBlocks.FALL_TRAP.get().asItem());
    }


}
