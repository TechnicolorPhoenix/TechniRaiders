package com.techniphoenix.techniraiders.item.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.techniphoenix.techniraiders.TechniRaiders;
import com.techniphoenix.techniraiders.item.ModArmorMaterial;
import com.techniphoenix.techniraiders.item.ModItemGroup;
import com.techniphoenix.techniraiders.item.custom.armor.RaidArmor;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class RaidArmorItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TechniRaiders.MOD_ID);

    private static Multimap<Attribute, Double> createRaidAttributes() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        Multimap<Attribute, Double> bonuses = HashMultimap.create();
        bonuses.put(Attributes.MAX_HEALTH, 1D);
        return bonuses;
    }

    private static Multimap<Attribute, Double> createOmenAttributes() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        Multimap<Attribute, Double> bonuses = HashMultimap.create();
        bonuses.put(Attributes.MAX_HEALTH, 1D);
        return bonuses;
    }

    private static Map<Effect, Integer> createRaidEffects() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        Map<Effect, Integer> bonuses = new HashMap<>();
        bonuses.put(Effects.ABSORPTION, 1);
        bonuses.put(Effects.DAMAGE_RESISTANCE, 1);
        return bonuses;
    }

    private static Map<Effect, Integer> createOmenEffects() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        Map<Effect, Integer> bonuses = new HashMap<>();
        bonuses.put(Effects.ABSORPTION, 1);
        bonuses.put(Effects.DAMAGE_RESISTANCE, 1);
        return bonuses;
    }

    static final Multimap<Attribute, Double> raidAttributes = createRaidAttributes();
    static final Map<Effect, Integer> raidEffects = createRaidEffects();
    static final Multimap<Attribute, Double> omenAttributes = createOmenAttributes();
    static final Map<Effect, Integer> omenEffects = createOmenEffects();

    public static final RegistryObject<Item> RAID_LEATHER_ARMOR_BOOTS = ITEMS.register("raid_leather_boots",
            () -> new RaidArmor(ModArmorMaterial.RAID_LEATHER, EquipmentSlotType.FEET,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 0,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_LEATHER_ARMOR_LEGGINGS = ITEMS.register("raid_leather_leggings",
            () -> new RaidArmor(ModArmorMaterial.RAID_LEATHER, EquipmentSlotType.LEGS,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 0,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_LEATHER_ARMOR_CHESTPLATE = ITEMS.register("raid_leather_chestplate",
            () -> new RaidArmor(ModArmorMaterial.RAID_LEATHER, EquipmentSlotType.CHEST,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 0,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_LEATHER_ARMOR_HELMET = ITEMS.register("raid_leather_helmet",
            () -> new RaidArmor(ModArmorMaterial.RAID_LEATHER, EquipmentSlotType.HEAD,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 0,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));

    public static final RegistryObject<Item> RAID_IRON_ARMOR_BOOTS = ITEMS.register("raid_iron_boots",
            () -> new RaidArmor(ModArmorMaterial.RAID_IRON, EquipmentSlotType.FEET,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 1,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_IRON_ARMOR_LEGGINGS = ITEMS.register("raid_iron_leggings",
            () -> new RaidArmor(ModArmorMaterial.RAID_IRON, EquipmentSlotType.LEGS,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 1,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_IRON_ARMOR_CHESTPLATE = ITEMS.register("raid_iron_chestplate",
            () -> new RaidArmor(ModArmorMaterial.RAID_IRON, EquipmentSlotType.CHEST,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 1,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_IRON_ARMOR_HELMET = ITEMS.register("raid_iron_helmet",
            () -> new RaidArmor(ModArmorMaterial.RAID_IRON, EquipmentSlotType.HEAD,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 1,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));

    public static final RegistryObject<Item> RAID_EMERALD_ARMOR_BOOTS = ITEMS.register("raid_emerald_boots",
            () -> new RaidArmor(ModArmorMaterial.RAID_EMERALD, EquipmentSlotType.FEET,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 2,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_EMERALD_ARMOR_LEGGINGS = ITEMS.register("raid_emerald_leggings",
            () -> new RaidArmor(ModArmorMaterial.RAID_EMERALD, EquipmentSlotType.LEGS,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 2,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_EMERALD_ARMOR_CHESTPLATE = ITEMS.register("raid_emerald_chestplate",
            () -> new RaidArmor(ModArmorMaterial.RAID_EMERALD, EquipmentSlotType.CHEST,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 2,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
    public static final RegistryObject<Item> RAID_EMERALD_ARMOR_HELMET = ITEMS.register("raid_emerald_helmet",
            () -> new RaidArmor(ModArmorMaterial.RAID_EMERALD, EquipmentSlotType.HEAD,
                    omenAttributes, omenEffects, raidAttributes, raidEffects, 2,
                    new Item.Properties().rarity(Rarity.UNCOMMON).tab(ModItemGroup.RAID_ARMOR_GROUP)));
}
