package com.prohitman.unsortedcannibals.client.model.entity.living;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.DummyCannibal;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DummyCannibalModel extends GeoModel<DummyCannibal> {
    @Override
    public ResourceLocation getModelResource(DummyCannibal animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "geo/entity/crave.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DummyCannibal animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "textures/entity/crave.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DummyCannibal animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "animations/entity/crave.animation.json");
    }
}
