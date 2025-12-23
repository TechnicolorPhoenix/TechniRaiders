package com.techniphoenix.techniraiders.helper;

import com.techniphoenix.techniraiders.item.interfaces.ILevelableItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
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
                    if (item instanceof ILevelableItem) {
                        ILevelableItem armor = (ILevelableItem) item;
                        countAndLevel[1] += armor.getItemLevel(); // Accumulate total level
                    }
                    countAndLevel[0]++;
                }
            }
        }
        return countAndLevel;
    }
}
