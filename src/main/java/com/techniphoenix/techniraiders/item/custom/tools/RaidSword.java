package com.techniphoenix.techniraiders.item.custom.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.techniphoenix.techniraiders.item.custom.tools.effect.EffectSword;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class RaidSword extends EffectSword {
    // Unique ID for the persistent attribute modifier
    private static final UUID SCALING_DAMAGE_MODIFIER_UUID = UUID.fromString("6F21A77E-F0C6-44D1-A12A-14A1C8097E9C");
    // NBT key to store the current damage bonus
    private static final String NBT_KEY_BONUS_DAMAGE = "PillagerSlayer_BonusDamage";

    private final double initialDamageBonus = 0;
    private int weaponLevel = 0;

    private static final Random RANDOM = new Random();

    private static List<Effect> createEffectList() {
        List<Effect> effects = new ArrayList<>();
        effects.add(Effects.DAMAGE_RESISTANCE);
        effects.add(Effects.DAMAGE_BOOST);
        effects.add(Effects.DIG_SPEED);
        effects.add(Effects.MOVEMENT_SPEED);
        return effects;
    }

    private static final List<Effect> effects = createEffectList();

    /**
     * Constructor for the EffectSword.
     * * @param tier The material tier of the pickaxe.
     *
     * @param tier
     * @param attackDamage         The base attack damage of the tool.
     * @param attackSpeed          The attack speed modifier of the tool.
     * @param weaponLevel          The level of the weapon.
     * @param properties           Item properties.
     */
    public RaidSword(IItemTier tier, int attackDamage, float attackSpeed, int weaponLevel, Properties properties) {
        super(tier, attackDamage, attackSpeed, null, 200, 0, properties);
        this.weaponLevel = weaponLevel;
    }

    /**
     * Helper to get the current damage bonus from the ItemStack's NBT.
     */
    public static double getBonusDamage(ItemStack stack) {
        CompoundNBT tag = stack.getTag();
        if (tag != null && tag.contains(NBT_KEY_BONUS_DAMAGE, Constants.NBT.TAG_DOUBLE)) {
            return tag.getDouble(NBT_KEY_BONUS_DAMAGE);
        }
        // If no tag is present, return the default initial bonus (which should be 0.0)
        return ((RaidSword)stack.getItem()).initialDamageBonus;
    }

    /**
     * Helper to set the damage bonus in the ItemStack's NBT.
     */
    public static void setBonusDamage(ItemStack stack, double damage) {
        stack.getOrCreateTag().putDouble(NBT_KEY_BONUS_DAMAGE, damage);
    }

    /**
     * Overrides the default attribute map to inject our custom scaling damage modifier.
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
        // Get the base map from the superclass (this includes the base ATTACK_DAMAGE and ATTACK_SPEED)
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(equipmentSlot, stack));

        if (equipmentSlot == EquipmentSlotType.MAINHAND) {
            double currentBonus = getBonusDamage(stack);

            // Add the dynamic, scaling damage modifier
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(
                    SCALING_DAMAGE_MODIFIER_UUID,
                    "Weapon modifier", // Name of the modifier
                    currentBonus,      // The value from NBT
                    AttributeModifier.Operation.ADDITION // Modifier adds to the base value
            ));
        }

        return builder.build();
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player.hasEffect(Effects.BAD_OMEN)) {
            EffectInstance badOmenEffect = player.getEffect(Effects.BAD_OMEN);

            this.effectDuration = badOmenEffect.getDuration();

            int randomFactor = RANDOM.nextInt(effects.size()) + 1;
            Effect pickedEffect = effects.get(randomFactor);

            if (pickedEffect == Effects.DAMAGE_BOOST) {
                if (weaponLevel == 1) {
                    pickedEffect = Effects.ABSORPTION;
                } else {

                }
            }
            if (weaponLevel > 1) {
                this.effectDuration *= 2;
            }
            this.effectMap = new HashMap<>();
            this.effectMap.put(Effects.REGENERATION, weaponLevel);
            this.effectMap.put(pickedEffect, Math.max(weaponLevel - 1, 0));

            player.removeEffect(Effects.BAD_OMEN);

            return super.use(world, player, hand);
        }
        return ActionResult.pass(player.getItemInHand(hand));
    }
}
