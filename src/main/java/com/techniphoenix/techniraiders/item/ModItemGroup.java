package com.techniphoenix.techniraiders.item;

import com.techniphoenix.techniraiders.item.armor.RaidArmorItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {

    public static final ItemGroup RAID_ARMOR_GROUP = new ItemGroup("raidArmorTab")
    {
        @Override
        public ItemStack makeIcon()
        {
            return new ItemStack(RaidArmorItems.RAID_LEATHER_ARMOR_CHESTPLATE.get());
        }
    };
}
