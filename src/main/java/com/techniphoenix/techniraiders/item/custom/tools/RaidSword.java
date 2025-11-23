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

    private final double initialDamageBonus;

    private static final Random RANDOM = new Random();

    private static List<Effect> damageBonus() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        List<Effect> bonuses = new ArrayList<>();
        bonuses.add(Effects.HEALTH_BOOST);
        bonuses.add(Effects.DAMAGE_BOOST);
        return bonuses;
    }
    private static List<Effect> speedBonus() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        List<Effect> bonuses = new ArrayList<>();
        bonuses.add(Effects.HEALTH_BOOST);
        bonuses.add(Effects.DIG_SPEED);
        return bonuses;
    }
    private static List<Effect> movementBonus() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        List<Effect> bonuses = new ArrayList<>();
        bonuses.add(Effects.HEALTH_BOOST);
        bonuses.add(Effects.MOVEMENT_SPEED);
        return bonuses;
    }
    private static List<Effect> regenerationBonus() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        List<Effect> bonuses = new ArrayList<>();
        bonuses.add(Effects.HEALTH_BOOST);
        bonuses.add(Effects.REGENERATION);
        return bonuses;
    }
    private static List<Effect> resistanceBonus() {
        // This is where the executable code (.put) is now correctly placed (inside a method).
        List<Effect> bonuses = new ArrayList<>();
        bonuses.add(Effects.HEALTH_BOOST);
        bonuses.add(Effects.DAMAGE_RESISTANCE);
        return bonuses;
    }


    /**
     * Constructor for the EffectSword.
     * * @param tier The material tier of the pickaxe.
     *
     * @param tier
     * @param attackDamage         The base attack damage of the tool.
     * @param attackSpeed          The attack speed modifier of the tool.
     * @param properties           Item properties.
     */
    public RaidSword(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, null, 200, 0, properties);
        initialDamageBonus = attackDamage;
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
        ImmutableMultimap.Builder<net.minecraft.entity.ai.attributes.Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
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
            Effect pickedEffect;

            this.effectDuration = badOmenEffect.getDuration();

            int randomFactor = RANDOM.nextInt(5) + 1; // Gives 1, 2, 3, 4, or 5

            switch (randomFactor) {
                case 1:

                case 2:
                    pickedEffect = Effects.DAMAGE_RESISTANCE;
                case 3:
                    pickedEffect = Effects.REGENERATION;
                case 4:
                    pickedEffect = Effects.MOVEMENT_SPEED;
                case 5:
                    pickedEffect = Effects.DIG_SPEED;
            }

            player.removeEffect(Effects.BAD_OMEN);


            return super.use(world, player, hand);
        }
        return ActionResult.pass(player.getItemInHand(hand));
    }
}
