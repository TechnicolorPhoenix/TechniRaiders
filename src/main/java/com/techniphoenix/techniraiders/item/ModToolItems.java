package com.techniphoenix.techniraiders.item;

import com.techniphoenix.techniraiders.item.tool.RaidWeaponItems;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModToolItems {
    public static void register(IEventBus eventBus) {
        RaidWeaponItems.ITEMS.register(eventBus);
    }
}
