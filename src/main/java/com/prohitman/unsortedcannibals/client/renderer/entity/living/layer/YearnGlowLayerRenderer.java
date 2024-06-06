package com.prohitman.unsortedcannibals.client.renderer.entity.living.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class YearnGlowLayerRenderer extends GeoRenderLayer<YearnCannibal> {
    private static final RenderType YEARN_EYES = RenderType.eyes(new ResourceLocation(UnsortedCannibalsMod.MODID, "textures/entity/yearn_glow_layer.png"));

    public YearnGlowLayerRenderer(GeoRenderer entityRendererIn) {
        super(entityRendererIn);

    }

    @Override
    public void render(PoseStack poseStack, YearnCannibal animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        VertexConsumer vertexconsumer = bufferSource.getBuffer(YEARN_EYES);

        this.getRenderer().reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, YEARN_EYES, vertexconsumer, partialTick, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        //this.getParentModel().renderToBuffer(pMatrixStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }


}
