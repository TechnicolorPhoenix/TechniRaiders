package com.techniphoenix.techniraiders.event;

import com.techniphoenix.techniraiders.TechniRaiders;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModEvents {

    public static void register(IEventBus bus){
        bus.register(new RaidArmorEventHandler());
    }
}
