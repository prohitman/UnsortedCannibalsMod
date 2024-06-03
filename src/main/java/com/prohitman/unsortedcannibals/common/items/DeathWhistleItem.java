package com.prohitman.unsortedcannibals.common.items;

import com.prohitman.unsortedcannibals.client.UCKeyHandler;
import com.prohitman.unsortedcannibals.client.keybindings.ModKeyBindings;
import com.prohitman.unsortedcannibals.core.init.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeathWhistleItem extends Item {
    public DeathWhistleItem(Properties pProperties) {
        super(pProperties);
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        pPlayer.startUsingItem(pUsedHand);
        play(pLevel, pPlayer);
        pPlayer.getCooldowns().addCooldown(this, 100);
        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.consume(itemstack);

    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack pStack) {
        return 45000;
    }

    /**
     * Returns the action that specifies what animation to play when the item is being used.
     */
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.TOOT_HORN;
    }

    private static void play(Level pLevel, Player pPlayer) {
        SoundEvent soundevent = ModSounds.DEATH_WHISTLE.get();

        pLevel.playSound(pPlayer, pPlayer, soundevent, SoundSource.RECORDS, 1.5f, 1.0F);
        pLevel.gameEvent(GameEvent.INSTRUMENT_PLAY, pPlayer.position(), GameEvent.Context.of(pPlayer));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if(UCKeyHandler.isKeyPressed(ModKeyBindings.INSTANCE.detailsKey)){
            pTooltipComponents.add(Component.translatable("item.tooltip.death_whistle"));
        } else {
            pTooltipComponents.add(Component.translatable("item.tooltip.press_shift"));
        }
    }
}
