package com.prohitman.unsortedcannibals.client.model.entity.living;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.FrenzyCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FrenzyModel extends DefaultedEntityGeoModel<FrenzyCannibal> {
    public FrenzyModel() {
        super(new ResourceLocation(UnsortedCannibalsMod.MODID, "frenzy"));
    }


    @Override
    public void setCustomAnimations(FrenzyCannibal animatable, long instanceId, AnimationState<FrenzyCannibal> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX((entityData.headPitch() + 55) * Mth.DEG_TO_RAD);
            head.setRotY((entityData.netHeadYaw()) * Mth.DEG_TO_RAD);
        }
    }
/*@Override
    public ResourceLocation getModelResource(CraveCannibal animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "geo/entity/crave_cannibal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CraveCannibal animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "textures/entity/crave_cannibal.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CraveCannibal animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "animations/entity/crave_cannibal.animation.json");
    }*/
}
