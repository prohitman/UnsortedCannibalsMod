package com.prohitman.unsortedcannibals.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.client.model.SpearItemModel;
import com.prohitman.unsortedcannibals.common.items.SerratedSpearItem;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SpearItemRenderer extends GeoItemRenderer<SerratedSpearItem> {
    public SpearItemRenderer() {
        super(new SpearItemModel());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        System.out.println("Called in Item Rendered!!");

        if (stack.is(ModItems.SERRATED_SPEAR.get()) && stack.getUseDuration() > 0 && (/*pDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || */transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            System.out.println("Spear Rendered!!");
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(-180));
            poseStack.popPose();
        }
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
