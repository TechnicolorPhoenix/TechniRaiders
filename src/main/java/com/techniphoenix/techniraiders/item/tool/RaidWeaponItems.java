package com.techniphoenix.techniraiders.item.tool;

import com.techniphoenix.techniraiders.TechniRaiders;
import com.techniphoenix.techniraiders.item.ModItemGroup;
import com.techniphoenix.techniraiders.item.custom.tools.RaidSword;
import net.minecraft.item.Item;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class RaidWeaponItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TechniRaiders.MOD_ID);

    public static final RegistryObject<Item> RAID_SWORD_TIER_1 = ITEMS.register("raid_sword_1",
            () -> new RaidSword(ItemTier.WOOD, 3, -2.4F,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));

}
