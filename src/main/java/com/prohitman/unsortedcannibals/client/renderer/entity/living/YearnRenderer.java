package com.prohitman.unsortedcannibals.client.renderer.entity.living;

import com.prohitman.unsortedcannibals.client.model.entity.living.YearnModel;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class YearnRenderer extends GeoEntityRenderer<YearnCannibal> {

    public YearnRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new YearnModel());
        this.shadowRadius = 1.0F;
        this.withScale(1.15f, 1.15f);

        //addRenderLayer(new YearnGlowLayerRenderer(this));
    }
}
