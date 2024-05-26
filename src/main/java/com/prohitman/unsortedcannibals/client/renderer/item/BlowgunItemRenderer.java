package com.prohitman.unsortedcannibals.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.prohitman.unsortedcannibals.client.model.item.BlowgunItemModel;
import com.prohitman.unsortedcannibals.common.items.BlowGunItem;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BlowgunItemRenderer extends GeoItemRenderer<BlowGunItem> {
    public BlowgunItemRenderer() {
        super(new BlowgunItemModel());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ClientLevel level = Minecraft.getInstance().level;
        boolean heldIn3d = transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;

        if (stack.is(ModItems.BLOWGUN.get())) {
            poseStack.translate(0.5F, 0.5f, 0.5f);
            ItemStack spriteItem = new ItemStack(ModItems.BLOWGUN_INVENTORY.get());
            spriteItem.setTag(stack.getTag());

            if(heldIn3d){
                poseStack.pushPose();

                poseStack.popPose();

            }
            else {

                Minecraft.getInstance().getItemRenderer().renderStatic(spriteItem, transformType, transformType == ItemDisplayContext.GROUND ? packedLight : 240, packedOverlay, poseStack, bufferSource, level, 0);
            }

        }

        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }

    @Override
    protected void renderInGui(ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    }
}
