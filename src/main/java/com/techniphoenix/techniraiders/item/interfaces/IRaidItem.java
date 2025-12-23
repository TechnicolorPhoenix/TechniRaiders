package com.techniphoenix.techniraiders.item.interfaces;

import com.techniphoenix.techniraiders.item.component.RaidItemComponent;

public interface IRaidItem {
    String NBT_KEY_BONUS_DAMAGE = "Bonus_Damage";
    RaidItemComponent getRaidComponent();
}
