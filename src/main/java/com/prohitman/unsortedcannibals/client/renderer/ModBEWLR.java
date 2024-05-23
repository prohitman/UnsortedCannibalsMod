package com.prohitman.unsortedcannibals.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModBEWLR extends BlockEntityWithoutLevelRenderer {
    public ModBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        boolean heldIn3d = pDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || pDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || pDisplayContext == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || pDisplayContext == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;

        if (pStack.is(ModItems.SERRATED_SPEAR.get()) && pStack.getUseDuration() > 0 && (/*pDisplayContext == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || */pDisplayContext == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)) {
            pPoseStack.pushPose();
            pPoseStack.mulPose(Axis.XP.rotationDegrees(-180));
            pPoseStack.popPose();
        }
    }
}
