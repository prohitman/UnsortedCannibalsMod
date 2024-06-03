package com.prohitman.unsortedcannibals.client.keybindings;


import com.mojang.blaze3d.platform.InputConstants;
import com.prohitman.unsortedcannibals.UnsortedCannibalsMod;
import cpw.mods.util.Lazy;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import javax.swing.text.JTextComponent;
public final class ModKeyBindings {
    public static final ModKeyBindings INSTANCE = new ModKeyBindings();

    private ModKeyBindings() {}

    private static final String CATEGORY = "key.categories." + UnsortedCannibalsMod.MODID;

    public final KeyMapping detailsKey =
            new KeyMapping(
                    "key.unsortedcannibals.detailskey",
                    KeyConflictContext.GUI,
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_LEFT_SHIFT,
                    "key.categories.unsortedcannibals");

}
