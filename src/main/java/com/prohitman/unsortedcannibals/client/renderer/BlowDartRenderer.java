package com.prohitman.unsortedcannibals.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.prohitman.unsortedcannibals.client.model.BlowDartModel;
import com.prohitman.unsortedcannibals.client.model.ThrownSpearModel;
import com.prohitman.unsortedcannibals.common.entities.projectile.BlowDart;
import com.prohitman.unsortedcannibals.common.entities.projectile.ThrownSpear;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BlowDartRenderer extends GeoEntityRenderer<BlowDart> {
    public BlowDartRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BlowDartModel());
    }

    @Override
    public void preRender(PoseStack poseStack, BlowDart animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, animatable.yRotO, animatable.getYRot()) + 180F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTick, animatable.xRotO, animatable.getXRot()) - 180.0F));
        poseStack.translate(0, -0.25, 0);

    }
}
