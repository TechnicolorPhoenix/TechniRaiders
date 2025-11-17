package com.techniphoenix.techniraiders.helper;

import com.techniphoenix.techniraiders.item.custom.armor.DyeableLevelableArmor;
import com.techniphoenix.techniraiders.item.custom.armor.LevelableArmor;
import com.techniphoenix.techniraiders.item.custom.armor.RaidArmor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;

public class ArmorHelper {
    public static int[] getArmorCountAndTotalLevel(LivingEntity entity, Class<? extends ArmorItem> desiredArmorClass) {
        int[] countAndLevel = new int[2];

        // Check if the entity is null before proceeding
        if (entity == null) {
            return countAndLevel;
        }

        // Iterate over standard armor slots
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getType() == EquipmentSlotType.Group.ARMOR) {

                Item item = entity.getItemBySlot(slot).getItem();

                if (desiredArmorClass.isInstance(item)) {
                    if (item instanceof DyeableLevelableArmor) {
                        DyeableLevelableArmor armor = (DyeableLevelableArmor) item;
                        countAndLevel[1] += armor.armorLevel; // Accumulate total level
                    } else if (item instanceof LevelableArmor) {
                        LevelableArmor armor = (LevelableArmor) item;
                        countAndLevel[1] += armor.armorLevel; // Accumulate total level
                    }
                    countAndLevel[0]++;
                }
            }
        }
        return countAndLevel;
    }
}
