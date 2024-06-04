package com.prohitman.unsortedcannibals.client.renderer.entity.living;

import com.prohitman.unsortedcannibals.client.model.entity.living.CraveModel;
import com.prohitman.unsortedcannibals.common.entities.living.CraveCannibal;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CraveRenderer extends GeoEntityRenderer<CraveCannibal> {
    public CraveRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CraveModel());
    }
}
