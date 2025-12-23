package com.techniphoenix.techniraiders.item;

import com.techniphoenix.techniraiders.TechniRaiders;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum ModArmorMaterial implements IArmorMaterial {
    RAID_LEATHER("raid_leather", 7, new int[]{1, 2, 3, 1}, 14,
            SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, 1, () -> {
        return Ingredient.of(Items.LEATHER);
    }),
    RAID_IRON("raid_iron", 15, new int[]{1, 4, 5, 2}, 13,
            SoundEvents.ARMOR_EQUIP_IRON, 0.33F, 0.0F, 1, () -> {
        return Ingredient.of(Items.IRON_INGOT);
    }),
    RAID_EMERALD("raid_emerald", 26, new int[]{2, 5, 6, 2}, 11,
            SoundEvents.ARMOR_EQUIP_TURTLE, 1.0F, 0.0F, 1, () -> {
        return Ingredient.of(Items.EMERALD_ORE);
    }),
    ;
;

    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;
    private final SoundEvent soundEvent;
    private final float toughness;
    private final float knockbackResistance;
    private final int armorLevel;
    private final LazyValue<Ingredient> repairMaterial;

    private ModArmorMaterial(String name, int maxDamageFactor, int[] damageReductionAmountArray, int enchantability, SoundEvent soundEvent, float toughness, float knockbackResistance, int armorLevel, Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairMaterial = new LazyValue<>(repairMaterial);
        this.armorLevel = armorLevel;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slotIn) {
        return this.damageReductionAmountArray[slotIn.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.soundEvent;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return TechniRaiders.MOD_ID + ":" + this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
    public int getItemLevel() { return this.armorLevel; }
}
