package com.techniphoenix.techniraiders.item.custom;

import com.google.common.collect.Multimap;
import com.techniphoenix.techniraiders.item.custom.tools.RaidAxe;
import com.techniphoenix.techniraiders.item.custom.tools.RaidSword;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import java.util.UUID;

public interface IDamageScaling {

    UUID SCALING_DAMAGE_MODIFIER_UUID = UUID.fromString("6F21A77E-F0C6-44D1-A12A-14A1C8097E9C");
    // NBT key to store the current damage bonus
    String NBT_KEY_BONUS_DAMAGE = "PillagerSlayer_BonusDamage";

    double initialDamageBonus = 0;
    int weaponLevel = 0;

    float getDestroySpeed(ItemStack stack, BlockState state);
    Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack);

    static double getBonusDamage(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(NBT_KEY_BONUS_DAMAGE, Constants.NBT.TAG_DOUBLE)) {
            return tag.getDouble(NBT_KEY_BONUS_DAMAGE);
        }
        // If no tag is present, return the default initial bonus (which should be 0.0)
        return initialDamageBonus;
    }

    /**
     * Helper to set the damage bonus in the ItemStack's NBT.
     */
    static void setBonusDamage(ItemStack stack, double damage) {
        stack.getOrCreateTag().putDouble(NBT_KEY_BONUS_DAMAGE, damage);
    }
}
