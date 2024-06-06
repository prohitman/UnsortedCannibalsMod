package com.prohitman.unsortedcannibals.client.renderer.entity.living;

import com.prohitman.unsortedcannibals.client.model.entity.living.FrenzyModel;
import com.prohitman.unsortedcannibals.client.model.entity.living.YearnModel;
import com.prohitman.unsortedcannibals.client.renderer.entity.living.layer.FrenzyGlowLayerRenderer;
import com.prohitman.unsortedcannibals.common.entities.living.FrenzyCannibal;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FrenzyRenderer extends GeoEntityRenderer<FrenzyCannibal> {

    public FrenzyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FrenzyModel());
        this.shadowRadius = 0.5F;
        this.withScale(0.8f, 0.8f);


        addRenderLayer(new FrenzyGlowLayerRenderer(this));
    }
}
