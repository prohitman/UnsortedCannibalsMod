package com.prohitman.unsortedcannibals.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.prohitman.unsortedcannibals.core.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBEWLR extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation SERRATED_SPEAR_TEXTURE = new ResourceLocation("unsortedcannibals:textures/entity/serrated_spear.png");
    //private static final LimestoneSpearModel SERRATED_SPEAR_MODEL = new LimestoneSpearModel();

    public ModBEWLR() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }


    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

        ClientLevel level = Minecraft.getInstance().level;
        float partialTick = Minecraft.getInstance().getPartialTick();
        boolean heldIn3d = transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || transformType == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;


        if (stack.is(ModItems.SERRATED_SPEAR.get())) {
            poseStack.translate(0.5F, 0.5f, 0.5f);
            ItemStack spriteItem = new ItemStack(ModItems.SERRATED_SPEAR_INVENTORY.get());
            spriteItem.setTag(stack.getTag());

            if(heldIn3d){
                poseStack.pushPose();

                poseStack.translate(0, -0.25, 0);

                if(stack.getUseDuration() > 0){
                    poseStack.mulPose(Axis.XP.rotationDegrees(-180));
                }
                //VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(LIMESTONE_SPEAR_TEXTURE), false, itemStackIn.hasFoil());

                //this.model..renderToBuffer(poseStack, vertexconsumer, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);

                poseStack.popPose();

            } else{
                Minecraft.getInstance().getItemRenderer().renderStatic(spriteItem, transformType, transformType == ItemDisplayContext.GROUND ? packedLight : 240, packedOverlay, poseStack, bufferSource, level, 0);
            }

        }
        super.renderByItem(stack, transformType, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
