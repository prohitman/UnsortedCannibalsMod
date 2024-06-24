package com.prohitman.unsortedcannibals.client.model.entity.living;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.DummyCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.client.model.PigModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CraveModel extends DefaultedEntityGeoModel<CraveCannibal> {
    public CraveModel() {
        super(new ResourceLocation(UnsortedCannibalsMod.MODID, "crave"));
    }


    @Override
    public void setCustomAnimations(CraveCannibal animatable, long instanceId, AnimationState<CraveCannibal> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null /*&& !animatable.isClimbing()*/) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
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
