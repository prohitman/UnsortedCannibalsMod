package com.prohitman.unsortedcannibals.client.renderer.item.armor;

import com.prohitman.unsortedcannibals.client.model.item.armor.BoneArmorModel;
import com.prohitman.unsortedcannibals.common.items.armor.BoneArmorItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BoneArmorRenderer extends GeoArmorRenderer<BoneArmorItem> {
    public BoneArmorRenderer() {
        super(new BoneArmorModel());
    }

}
