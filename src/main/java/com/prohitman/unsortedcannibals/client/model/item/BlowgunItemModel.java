package com.prohitman.unsortedcannibals.client.model.item;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.items.BlowGunItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class BlowgunItemModel extends DefaultedItemGeoModel<BlowGunItem> {
    public BlowgunItemModel() {
        super(new ResourceLocation(UnsortedCannibalsMod.MODID, "blowgun"));
    }
}
