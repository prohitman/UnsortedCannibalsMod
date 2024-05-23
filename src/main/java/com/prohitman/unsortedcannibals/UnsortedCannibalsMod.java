package com.prohitman.unsortedcannibals;

import com.prohitman.unsortedcannibals.common.CommonForgeEvents;
import com.prohitman.unsortedcannibals.core.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(UnsortedCannibalsMod.MODID)
public class UnsortedCannibalsMod
{
    public static final String MODID = "unsortedcannibals";

    public UnsortedCannibalsMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
    }
}
