package com.techniphoenix.techniraiders.item;

import com.techniphoenix.techniraiders.item.armor.*;

import net.minecraftforge.eventbus.api.IEventBus;

public class ModArmorItems {

    public static void register(IEventBus eventBus) {
        RaidArmorItems.ITEMS.register(eventBus);
    }
}
