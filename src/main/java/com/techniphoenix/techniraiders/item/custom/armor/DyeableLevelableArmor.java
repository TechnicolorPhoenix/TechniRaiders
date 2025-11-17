package com.techniphoenix.techniraiders.item.custom.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.IArmorMaterial;

public class DyeableLevelableArmor extends DyeableArmorItem {
    public final int armorLevel;

    public DyeableLevelableArmor(IArmorMaterial armorMaterial, EquipmentSlotType slotType, Properties properties, int armorLevel) {
        super(armorMaterial, slotType, properties);
        this.armorLevel = armorLevel;
    }
}
