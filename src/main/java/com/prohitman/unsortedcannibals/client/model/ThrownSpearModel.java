package com.prohitman.unsortedcannibals.client.model;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.projectile.ThrownSpear;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.RaceCarEntity;
import software.bernie.geckolib.GeckoLib;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ThrownSpearModel extends DefaultedEntityGeoModel<ThrownSpear> {
    public ThrownSpearModel() {
        super(new ResourceLocation(UnsortedCannibalsMod.MODID, "serrated_spear"));
    }
    /*@Override
    public RenderType getRenderType(ThrownSpear animatable, ResourceLocation texture) {
        return RenderType.cutout();
    }*/
}
