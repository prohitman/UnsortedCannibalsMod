package com.prohitman.unsortedcannibals.client.renderer.entity.living.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.FrenzyCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class FrenzyGlowLayerRenderer extends GeoRenderLayer<FrenzyCannibal> {
    private static final RenderType FRENZY_EYES = RenderType.eyes(new ResourceLocation(UnsortedCannibalsMod.MODID, "textures/entity/frenzy_glow_layer.png"));

    public FrenzyGlowLayerRenderer(GeoRenderer entityRendererIn) {
        super(entityRendererIn);

    }

    @Override
    public void render(PoseStack poseStack, FrenzyCannibal animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        VertexConsumer vertexconsumer = bufferSource.getBuffer(FRENZY_EYES);

        this.getRenderer().reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, FRENZY_EYES, vertexconsumer, partialTick, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        //this.getParentModel().renderToBuffer(pMatrixStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }


}
