package com.techniphoenix.techniraiders.item.component;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.Map;

public class EffectItemComponent
{
    protected Map<Effect, Integer> effectMap;
    protected int effectDuration;
    protected int baseDurabilityCost;

    public EffectItemComponent(Map<Effect, Integer> effectAmplifications, int effectDuration, int durabilityCost){
        this.effectMap = effectAmplifications;
        this.effectDuration = effectDuration;
        this.baseDurabilityCost = durabilityCost;
    }

    /**
     * Handles the item use event (Right Click) to apply status effects to the player.
     */
    public ActionResult<ItemStack> useEffect(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (world.isClientSide) return ActionResult.pass(stack);

        for (Map.Entry<Effect, Integer> entry : this.effectMap.entrySet()) {
            Effect effect = entry.getKey();
            int amplificationLevel = entry.getValue();

            int amplifier = Math.max(0, amplificationLevel - 1);

            player.addEffect(new EffectInstance(
                    effect,
                    this.effectDuration,
                    amplifier,
                    false,
                    true
            ));
        }

        stack.hurtAndBreak(this.baseDurabilityCost, player, (p) -> p.broadcastBreakEvent(hand));

        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5F, 1.0F);

        return ActionResult.success(stack);
    }
}
