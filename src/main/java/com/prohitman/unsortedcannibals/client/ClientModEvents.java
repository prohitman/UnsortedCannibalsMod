package com.prohitman.unsortedcannibals.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.client.keybindings.ModKeyBindings;
import com.prohitman.unsortedcannibals.client.renderer.entity.living.DummyCannibalRenderer;
import com.prohitman.unsortedcannibals.client.renderer.entity.projectile.BlowDartRenderer;
import com.prohitman.unsortedcannibals.client.renderer.entity.projectile.ThrownSpearRenderer;
import com.prohitman.unsortedcannibals.common.entities.living.DummyCannibal;
import com.prohitman.unsortedcannibals.core.init.ModBlocks;
import com.prohitman.unsortedcannibals.core.init.ModEffects;
import com.prohitman.unsortedcannibals.core.init.ModEntities;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.client.color.block.BlockColors;
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
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = UnsortedCannibalsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    private static long effectStartTime = -1;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {


            EntityRenderers.register(ModEntities.SERRATED_SPEAR.get(), ThrownSpearRenderer::new);
            EntityRenderers.register(ModEntities.BLOW_DART.get(), BlowDartRenderer::new);
            EntityRenderers.register(ModEntities.DUMMY_CANNIBAL.get(), DummyCannibalRenderer::new);

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
    public void registerBindings(RegisterKeyMappingsEvent event){
        event.register(ModKeyBindings.INSTANCE.detailsKey);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent event){
        event.registerBelowAll("visceral_pain", (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            gui.setupOverlayRenderState(true, false);
            if (gui.getMinecraft().player.hasEffect(ModEffects.VISCERAL_PAIN.get()))
            {
                if (effectStartTime == -1) {
                    effectStartTime = gui.getMinecraft().player.tickCount;
                }

                int fade_duration = gui.getMinecraft().player.getEffect(ModEffects.VISCERAL_PAIN.get()).getDuration() / 3;

                long currentTime = gui.getMinecraft().player.tickCount;
                float alpha = getAlpha(currentTime, partialTick, fade_duration);

                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
                guiGraphics.blit(new ResourceLocation(UnsortedCannibalsMod.MODID, "textures/misc/visceral_pain_outline.png"), 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            } else{
                effectStartTime = -1;

            }
        });
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

    private static float getAlpha(long currentTime, float partialTicks, int fade_duration) {
        if (effectStartTime == -1) {
            return 0.0F;
        }

        long elapsedTime = currentTime - effectStartTime;
        float totalElapsedTicks = elapsedTime + partialTicks;

        if (totalElapsedTicks < fade_duration) {
            return totalElapsedTicks / fade_duration;
        } else {
            return 1.0F;
        }
    }
}
