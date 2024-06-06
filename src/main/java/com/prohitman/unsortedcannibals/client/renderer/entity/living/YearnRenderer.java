package com.prohitman.unsortedcannibals.client.renderer.entity.living;

import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.client.model.entity.living.YearnModel;
import com.prohitman.unsortedcannibals.client.renderer.entity.living.layer.CraveGlowLayerRenderer;
import com.prohitman.unsortedcannibals.client.renderer.entity.living.layer.YearnGlowLayerRenderer;
import com.prohitman.unsortedcannibals.common.entities.living.YearnCannibal;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class YearnRenderer extends GeoEntityRenderer<YearnCannibal> {

    public YearnRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new YearnModel());
        this.shadowRadius = 1.0F;


        //addRenderLayer(new YearnGlowLayerRenderer(this));
    }
}
