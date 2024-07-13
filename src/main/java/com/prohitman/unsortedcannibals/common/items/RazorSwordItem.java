package com.prohitman.unsortedcannibals.common.items;

import com.prohitman.unsortedcannibals.client.UCKeyHandler;
import com.prohitman.unsortedcannibals.client.keybindings.ModKeyBindings;
import net.minecraft.client.*;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.settings.KeyMappingLookup;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class RazorSwordItem extends SwordItem {
    public RazorSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });

        float maxHealth = pTarget.getMaxHealth();
        float currentHealth = pTarget.getHealth();
        float healthRatio = currentHealth / maxHealth;

        float additionalDamage = (1.0f - healthRatio) * 17f;

        pTarget.hurt(pTarget.damageSources().mobAttack(pAttacker), additionalDamage);

        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(UCKeyHandler.isKeyPressed(ModKeyBindings.INSTANCE.detailsKey)){
            pTooltipComponents.add(Component.translatable("item.tooltip.razor_sword"));
        } else {
            pTooltipComponents.add(Component.translatable("item.tooltip.press_shift"));
        }
    }
}
