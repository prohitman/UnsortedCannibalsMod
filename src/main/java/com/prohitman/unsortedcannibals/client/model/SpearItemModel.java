package com.prohitman.unsortedcannibals.client.model;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.common.items.SerratedSpearItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class SpearItemModel extends DefaultedItemGeoModel<SerratedSpearItem> {
    /**
     * Create a new instance of this model class.<br>
     * The asset path should be the truncated relative path from the base folder.<br>
     * E.G.
     * <pre>{@code
     * 	new ResourceLocation("myMod", "armor/obsidian")
     * }</pre>
     *
     * @param assetSubpath
     */
    public SpearItemModel() {
        super(new ResourceLocation(UnsortedCannibalsMod.MODID, "entity/serrated_spear"));
    }
}
