package com.prohitman.unsortedcannibals.client.model.entity.living;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import java.time.Year;

public class YearnModel extends DefaultedEntityGeoModel<YearnCannibal> {
    public YearnModel() {
        super(new ResourceLocation(UnsortedCannibalsMod.MODID, "yearn"), true);
    }

}
