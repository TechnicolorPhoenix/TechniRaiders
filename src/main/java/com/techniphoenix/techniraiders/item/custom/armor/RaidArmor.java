package com.techniphoenix.techniraiders.item.custom.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.techniphoenix.techniraiders.helper.ArmorHelper;
import com.techniphoenix.techniraiders.helper.RaidHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RaidArmor extends DyeableLevelableArmor {

    public static final float damageReductionPerPiece = 0.06f;
    public static final float damageReductionPerLevel = 0.02f;

    public static final UUID RAID_MODIFIERS_UUID = UUID.fromString("845DB3C2-CD78-40FB-8A8C-6BC55D1F6E1B")
    public static final UUID OMEN_MODIFIERS_UUID = UUID.fromString("D8499B04-0E66-4726-AB29-3731E40BC747")
    public static final UUID BASE_MODIFIERS_UUID = UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E")

    private final Multimap<Attribute, Double> omenAttributes;
    private final Map<Effect, Integer> omenEffects;
    private final Multimap<Attribute, Double> raidAttributes;
    private final Map<Effect, Integer> raidEffects;

    public boolean inRaid = false;

    public RaidArmor(IArmorMaterial materialIn, EquipmentSlotType slot, Multimap<Attribute, Double> omenAttributes, Map<Effect, Integer> omenEffects, Multimap<Attribute, Double> raidAttributes, Map<Effect, Integer> raidEffects, int armorLevel, Properties builder) {
        super(materialIn, slot, builder, armorLevel);
        this.omenAttributes = omenAttributes;
        this.omenEffects = omenEffects;
        this.raidAttributes = raidAttributes;
        this.raidEffects = raidEffects;
    }


    // FIX: Instead of calling super for unmatched slots, return an empty map.
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slotIn) {
        if (slotIn == this.slot) {
            // Standard attribute map creation (Defense, Toughness)
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

            float toughness = this.material.getToughness();
            int defense = this.material.getDefenseForSlot(slotIn);
            int healthBonus = 0;

            if (inRaid) {
                healthBonus=4;
                defense++;
                toughness++;
            }

            // This is the default defense attribute from ArmorItem, required for vanilla tooltip
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                    uuid,
                    "Armor toughness",
                    toughness,
                    AttributeModifier.Operation.ADDITION)
            );
            builder.put(Attributes.ARMOR, new AttributeModifier(
                    uuid,
                    "Armor modifier",
                    defense,
                    AttributeModifier.Operation.ADDITION)
            );
            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(
                    uuid,
                    "Raid health modifier",
                    healthBonus,
                    AttributeModifier.Operation.ADDITION)
            );

            // Add custom attributes here if needed, but for the base armor stats, this is sufficient.

            return builder.build();
        }

        // FIX: If the game asks for modifiers for a different slot, return an empty map
        // to prevent the tooltip from showing attributes for every slot.
        return ImmutableMultimap.of();
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isClientSide) {

            inRaid = RaidHelper.isEntityInRaid(player);

            // 1. Get the current status of all RaidArmor pieces worn
            int[] countAndLevel = ArmorHelper.getArmorCountAndTotalLevel(player, RaidArmor.class);
            int pieceCount = countAndLevel[0];
            int totalLevel = countAndLevel[1];

            if (pieceCount > 0) {
                double averageLevel = (double) totalLevel / pieceCount;
                // 2. Apply/Update Attributes
                applyDynamicAttributes(player, pieceCount, totalLevel, averageLevel, RAID_ARMOR_SET_UUID);
            } else {
                removeSetAttributes(player);
                removeEffects(player);
            }

            // Full Set (RAID) Bonus: Apply RAID effects and attributes
            if (inRaid) {
                addRaidAttributes(player, raidAttributes, ARMOR_MODIFIERS_UUID[this.slot.getIndex()], "raid_armor_bonus");
                addRaidEffects(player, raidEffects);
            } else {
                // Partial or No Set: Remove all attributes and effects
                removeRaidAttributes(player, raidAttributes, ARMOR_MODIFIERS_UUID[this.slot.getIndex()]);
                removeRaidEffects(player, raidEffects);
            }

            // Check for Raid Omen and apply Omen-related bonuses
            if (player.hasEffect(Effects.BAD_OMEN)) {
                addOmenAttributes(player, omenAttributes, ARMOR_MODIFIERS_UUID[this.slot.getIndex()], "raid_omen_bonus");
                addOmenEffects(player, omenEffects);
            } else {
                // Remove Omen attributes/effects if Bad Omen is gone
                removeOmenAttributes(player, omenAttributes, ARMOR_MODIFIERS_UUID[this.slot.getIndex()]);
                removeOmenEffects(player, omenEffects);
            }
        }
    }
    private void applyRaidAttributes(PlayerEntity player, int pieceCount, int totalLevel, double averageLevel, UUID raidUUID) {

    }

    private void applyDynamicAttributes(PlayerEntity player, int pieceCount, int totalLevel, double averageLevel, UUID baseUUID) {
        // Remove existing modifiers first to ensure clean updates
        removeAttributes(player);

        // Loop up to the number of pieces worn to apply the corresponding attribute
        for (int i = 0; i < pieceCount; i++) {
            Attribute attributeKey = DYNAMIC_ATTRIBUTES.get(i);
            double value = 0;
            AttributeModifier.Operation operation = AttributeModifier.Operation.ADDITION;

            // Set calculation rules based on your request
            switch (attributeKey.getDescriptionId()) {
                case "attribute.name.generic.attack_damage":
                    // Damage (0.01 * total armor level)
                    value = 0.01 * totalLevel;
                    operation = AttributeModifier.Operation.MULTIPLY_BASE; // Use MULTIPLY_BASE for flat percentage boost
                    break;
                case "attribute.name.generic.max_health":
                    // Health Boost (average armor level * 2) - Round up to 2.
                    value = Math.max(2, Math.ceil(averageLevel) * 2.0);
                    operation = AttributeModifier.Operation.ADDITION;
                    break;
                case "attribute.name.generic.luck":
                    // Luck (based on average armor level) - Use rounded average level
                    value = Math.round(averageLevel);
                    operation = AttributeModifier.Operation.ADDITION;
                    break;
                case "attribute.name.generic.attack_speed":
                    // Attack Speed (0.01 * total armor level)
                    value = 0.01 * totalLevel;
                    operation = AttributeModifier.Operation.MULTIPLY_BASE;
                    break;
            }

            // Create a unique UUID for this attribute/modifier
            UUID attributeUUID = new UUID(baseUUID.getMostSignificantBits(), baseUUID.getLeastSignificantBits() + i);

            // Apply the modifier
            ModifiableAttributeInstance instance = player.getAttribute(attributeKey);
            if (instance != null && value != 0) {
                AttributeModifier modifier = new AttributeModifier(attributeUUID,
                        "Raid Armor Set Bonus " + attributeKey.getDescriptionId(),
                        value,
                        operation);

                // Add the modifier if it doesn't exist
                if (instance.getModifier(attributeUUID) == null) {
                    instance.addTransientModifier(modifier);
                }
            }
        }
    }

    /**
     * Helper to remove all custom RaidArmor attribute modifiers when the armor is unequipped.
     * This method is now public static for external use (e.g., in event handler or when piece count is 0).
     */
    public static void removeAttributes(LivingEntity entity) {
        if (entity == null) return;

        int i = 0;
        for (Attribute attributeKey : DYNAMIC_ATTRIBUTES) {
            // Create a unique UUID for this attribute/modifier
            UUID attributeUUID = new UUID(RAID_ARMOR_SET_UUID.getMostSignificantBits(), RAID_ARMOR_SET_UUID.getLeastSignificantBits() + i);

            ModifiableAttributeInstance instance = entity.getAttribute(attributeKey);

            if (instance != null) {
                if (instance.getModifier(attributeUUID) != null) {
                    instance.removeModifier(attributeUUID);
                }
            }
            i++;
        }
    }

    // --- Effect Application and Removal ---

    private void applyDynamicEffects(PlayerEntity player, List<Effect> effectsToApply, int amplifier) {
        // Duration is short (e.g., 20 ticks = 1 second) so it reapplies every tick the player is wearing the armor.
        int duration = 25; // Re-applied every tick, ensures continuous presence.

        for (int i = 0; i < effectsToApply.size(); i++) {
            Effect effect = effectsToApply.get(i);
            int currentAmplifier = amplifier;

            // Special rules
            if (effect.equals(Effects.FIRE_RESISTANCE)) {
                // Fire Resistance (Level +1)
                currentAmplifier = Math.max(0, amplifier + 1);
            } else if (effect.equals(Effects.ABSORPTION)) {
                // Absorption (Always 0, which is amplifier -1)
                currentAmplifier = 0;
            }

            // The method automatically handles existing effects (renewing duration/updating amplifier)
            EffectInstance newInstance = new EffectInstance(effect, duration, currentAmplifier, false, false);
            player.addEffect(newInstance);
        }
    }

    private void removeEffects(PlayerEntity player) {
        // Clears the effects that are applied by the full set bonus
        // We iterate over both possible lists to ensure all custom effects are cleared
        for (Effect effect : raidEffects) {
            if (player.hasEffect(effect)) {
                player.removeEffect(effect);
            }
        }
        for (Effect effect : omenEffects) {
            if (player.hasEffect(effect)) {
                player.removeEffect(effect);
            }
        }
    }
}
