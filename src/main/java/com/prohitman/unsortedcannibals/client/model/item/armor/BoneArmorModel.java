package com.prohitman.unsortedcannibals.client.model.item.armor;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.items.armor.BoneArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BoneArmorModel extends GeoModel<BoneArmorItem> {
    @Override
    public ResourceLocation getModelResource(BoneArmorItem animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "geo/armor/bone_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BoneArmorItem animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "textures/armor/bone_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BoneArmorItem animatable) {
        return new ResourceLocation(UnsortedCannibalsMod.MODID, "animations/armor/bone_armor.animation.json");
    }
}
