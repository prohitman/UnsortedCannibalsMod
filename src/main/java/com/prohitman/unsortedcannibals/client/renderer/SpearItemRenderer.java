package com.prohitman.unsortedcannibals.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.client.model.SpearItemModel;
import com.prohitman.unsortedcannibals.common.items.SerratedSpearItem;
import net.minecraft.client.renderer.entity.ItemRenderer;

import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
        ClientLevel level = Minecraft.getInstance().level;
        boolean heldIn3d = transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;


        if (stack.is(ModItems.SERRATED_SPEAR.get())) {
            poseStack.translate(0.5F, 0.5f, 0.5f);
            ItemStack spriteItem = new ItemStack(ModItems.SERRATED_SPEAR_INVENTORY.get());
            spriteItem.setTag(stack.getTag());

            if(heldIn3d){
                poseStack.pushPose();

                poseStack.mulPose(Axis.XP.rotationDegrees(-180));
                poseStack.translate(0, -0.85F, -0.1F);
                if (transformType.firstPerson()) {
                    poseStack.translate(0, 0.5F, 0F);
                    poseStack.scale(0.75F, 0.75F, 0.75F);
                }

                poseStack.popPose();

            } /*else{

                Minecraft.getInstance().getItemRenderer().renderStatic(spriteItem, transformType, transformType == ItemDisplayContext.GROUND ? packedLight : 240, packedOverlay, poseStack, bufferSource, level, 0);
            }*/

        }
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
