package com.techniphoenix.techniraiders.item.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.techniphoenix.techniraiders.TechniRaiders;
import com.techniphoenix.techniraiders.item.ModArmorMaterial;
import com.techniphoenix.techniraiders.item.ModItemGroup;
import com.techniphoenix.techniraiders.item.custom.armor.RaidArmor;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RaidArmorItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TechniRaiders.MOD_ID);

    public static final RegistryObject<Item> RAID_LEATHER_ARMOR_BOOTS = ITEMS.register("raid_leather_boots",
            () -> new RaidArmor(ModArmorMaterial.RAID_LEATHER, EquipmentSlotType.FEET, 0,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_LEATHER_ARMOR_LEGGINGS = ITEMS.register("raid_leather_leggings",
            () -> new RaidArmor(ModArmorMaterial.RAID_LEATHER, EquipmentSlotType.LEGS, 0,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_LEATHER_ARMOR_CHESTPLATE = ITEMS.register("raid_leather_chestplate",
            () -> new RaidArmor(ModArmorMaterial.RAID_LEATHER, EquipmentSlotType.CHEST, 0,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_LEATHER_ARMOR_HELMET = ITEMS.register("raid_leather_helmet",
            () -> new RaidArmor(ModArmorMaterial.RAID_LEATHER, EquipmentSlotType.HEAD, 0,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));

    public static final RegistryObject<Item> RAID_IRON_ARMOR_BOOTS = ITEMS.register("raid_iron_boots",
            () -> new RaidArmor(ModArmorMaterial.RAID_IRON, EquipmentSlotType.FEET, 1,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_IRON_ARMOR_LEGGINGS = ITEMS.register("raid_iron_leggings",
            () -> new RaidArmor(ModArmorMaterial.RAID_IRON, EquipmentSlotType.LEGS, 1,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_IRON_ARMOR_CHESTPLATE = ITEMS.register("raid_iron_chestplate",
            () -> new RaidArmor(ModArmorMaterial.RAID_IRON, EquipmentSlotType.CHEST, 1,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_IRON_ARMOR_HELMET = ITEMS.register("raid_iron_helmet",
            () -> new RaidArmor(ModArmorMaterial.RAID_IRON, EquipmentSlotType.HEAD, 1,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));

    public static final RegistryObject<Item> RAID_EMERALD_ARMOR_BOOTS = ITEMS.register("raid_emerald_boots",
            () -> new RaidArmor(ModArmorMaterial.RAID_EMERALD, EquipmentSlotType.FEET, 2,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_EMERALD_ARMOR_LEGGINGS = ITEMS.register("raid_emerald_leggings",
            () -> new RaidArmor(ModArmorMaterial.RAID_EMERALD, EquipmentSlotType.LEGS, 2,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_EMERALD_ARMOR_CHESTPLATE = ITEMS.register("raid_emerald_chestplate",
            () -> new RaidArmor(ModArmorMaterial.RAID_EMERALD, EquipmentSlotType.CHEST, 2,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_EMERALD_ARMOR_HELMET = ITEMS.register("raid_emerald_helmet",
            () -> new RaidArmor(ModArmorMaterial.RAID_EMERALD, EquipmentSlotType.HEAD, 2,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
}
