package com.prohitman.unsortedcannibals.client.model;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.projectile.BlowDart;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BlowDartModel extends DefaultedEntityGeoModel<BlowDart> {
    public BlowDartModel() {
        super(new ResourceLocation(UnsortedCannibalsMod.MODID, "blow_dart"));
    }
}
