package com.prohitman.unsortedcannibals.common.items;

import com.prohitman.unsortedcannibals.client.UCKeyHandler;
import com.prohitman.unsortedcannibals.client.keybindings.ModKeyBindings;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class YearnFoodItem extends Item {
    public YearnFoodItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(UCKeyHandler.isKeyPressed(ModKeyBindings.INSTANCE.detailsKey)){
            pTooltipComponents.add(Component.translatable("item.tooltip.yearn_food_item"));
        } else {
            pTooltipComponents.add(Component.translatable("item.tooltip.press_shift"));
        }
    }
}
