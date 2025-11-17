package com.techniphoenix.techniraiders.item;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum ModItemTier implements IItemTier {
    STORM_BASE_GOLD(1, 100, 11.0F, 0.0F, 22, () -> {
        return Ingredient.of(Items.HONEYCOMB);
    }),
    STORM_UPGRADED_GOLD(2, 300, 13.0F, 1.0F, 25, () -> {
        return Ingredient.of(Items.SLIME_BLOCK);
    }),
    STORM_POWER_GOLD(3, 900, 15.0F, 2.0F, 28, () -> {
        return Ingredient.of(Items.SHULKER_SHELL);
    }),
    ;

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final LazyValue<Ingredient> repairMaterial;

    private ModItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyValue<>(repairMaterialIn);
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial.get();
    }
}
