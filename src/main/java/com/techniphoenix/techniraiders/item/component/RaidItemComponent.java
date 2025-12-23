package com.techniphoenix.techniraiders.item.component;

import com.techniphoenix.techniraiders.item.interfaces.ILevelableItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RaidItemComponent {
    public static final String NBT_KEY_BONUS_DAMAGE = "PillagerSlayer_BonusDamage";
    public static final UUID SCALING_DAMAGE_MODIFIER_UUID = UUID.randomUUID();
    public double initialDamageBonus = 0;
    public int effectDuration = 200;
    public EquipmentSlotType equipSlot;

    public RaidItemComponent(EquipmentSlotType equipSlot){
        this.equipSlot = equipSlot;
    }

    public float getDestroyBonus(ItemStack stack) {
        return (1F + (float) (this.getBonusDamage(stack) * 0.25));
    }

    /**
     * Helper to get the current damage bonus from the ItemStack's NBT.
     */
    public double getBonusDamage(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(NBT_KEY_BONUS_DAMAGE, Constants.NBT.TAG_DOUBLE)) {
            return tag.getDouble(NBT_KEY_BONUS_DAMAGE);
        }
        return this.initialDamageBonus;
    }

    /**
     * Helper to set the damage bonus in the ItemStack's NBT.
     */
    public void setBonusDamage(ItemStack stack, double damage) {
        stack.getOrCreateTag().putDouble(NBT_KEY_BONUS_DAMAGE, damage);
    }

    public ActionResult<ItemStack> useBadOmen(World world, PlayerEntity player, ItemStack stack, List<Effect> effects) {
        ILevelableItem levelableItem = (ILevelableItem) stack.getItem();
        int weaponLevel = levelableItem.getItemLevel();
        EffectInstance badOmenEffect = player.getEffect(Effects.BAD_OMEN);

        if (player.hasEffect(Effects.BAD_OMEN) && badOmenEffect != null) {

            this.effectDuration = badOmenEffect.getDuration();

            int randomFactor = world.getRandom().nextInt(effects.size());
            Effect pickedEffect = effects.get(randomFactor);

            if (pickedEffect == Effects.DAMAGE_BOOST) {
                if (weaponLevel == 1) {
                    pickedEffect = Effects.ABSORPTION;
                }
            }
            if (weaponLevel > 1) {
                this.effectDuration *= 2;
            }

            player.removeEffect(Effects.BAD_OMEN);
            player.addEffect(new EffectInstance(
                    pickedEffect,
                    effectDuration,
                    weaponLevel - 1,
                    true,
                    true
            ));

            return ActionResult.consume(stack);
        }

        return ActionResult.pass(stack);
    }
}
