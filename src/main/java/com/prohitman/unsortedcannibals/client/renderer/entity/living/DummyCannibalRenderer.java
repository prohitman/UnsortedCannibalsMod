package com.prohitman.unsortedcannibals.client.renderer.entity.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import com.prohitman.unsortedcannibals.client.model.entity.living.DummyCannibalModel;
import com.prohitman.unsortedcannibals.common.entities.living.DummyCannibal;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

public class DummyCannibalRenderer extends GeoEntityRenderer<DummyCannibal> {

    public DummyCannibalRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DummyCannibalModel());
    }
}
