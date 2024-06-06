package com.prohitman.unsortedcannibals.client.model.entity.living;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.FrenzyCannibal;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class FrenzyModel extends DefaultedEntityGeoModel<FrenzyCannibal> {
    public FrenzyModel() {
        super(new ResourceLocation(UnsortedCannibalsMod.MODID, "frenzy"), true);
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
