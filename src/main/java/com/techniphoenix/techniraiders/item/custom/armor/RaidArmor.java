package com.techniphoenix.techniraiders.item.custom.armor;

import com.techniphoenix.techniraiders.TechniRaiders;
import com.techniphoenix.techniraiders.helper.ArmorHelper;
import com.techniphoenix.techniraiders.helper.RaidHelper;
import com.techniphoenix.techniraiders.item.custom.interfaces.ILevelableItem;
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

import java.util.*;

public class RaidArmor extends DyeableArmorItem implements ILevelableItem {

    public static final float damageReductionPerPiece = 0.06f;
    public static final float damageReductionPerLevel = 0.02f;

    public static final UUID BASE_MODIFIERS_UUID = UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E");
    public static final UUID RAID_MODIFIERS_UUID = UUID.fromString("845DB3C2-CD78-40FB-8A8C-6BC55D1F6E1B");
    public static final UUID OMEN_MODIFIERS_UUID = UUID.fromString("D8499B04-0E66-4726-AB29-3731E40BC747");// --- Static Bonus Maps (Tiered by Piece Count) ---

    // 1. BASE BONUSES (Always Active)
    private static final Map<Integer, BonusData> BASE_BONUSES = new HashMap<>();
    static {
        // Correctly uses BASE_MODIFIERS_UUID components
        BASE_BONUSES.put(1, new BonusData(Attributes.MAX_HEALTH, null, new UUID(BASE_MODIFIERS_UUID.getMostSignificantBits(),BASE_MODIFIERS_UUID.getLeastSignificantBits() + 1)));
        BASE_BONUSES.put(2, new BonusData(Attributes.ATTACK_DAMAGE, null, new UUID(BASE_MODIFIERS_UUID.getMostSignificantBits(),BASE_MODIFIERS_UUID.getLeastSignificantBits() + 2)));
        BASE_BONUSES.put(3, new BonusData(Attributes.ARMOR_TOUGHNESS, null, new UUID(BASE_MODIFIERS_UUID.getMostSignificantBits(),BASE_MODIFIERS_UUID.getLeastSignificantBits() + 3)));
        BASE_BONUSES.put(4, new BonusData(null, null, null));
    }

    // 2. RAID BONUSES (Only Active In Raid)
    private static final Map<Integer, BonusData> RAID_BONUSES = new HashMap<>();
    static {
        RAID_BONUSES.put(1, new BonusData(Attributes.ARMOR, null, new UUID(RAID_MODIFIERS_UUID.getMostSignificantBits(), RAID_MODIFIERS_UUID.getLeastSignificantBits() + 1)));
        RAID_BONUSES.put(2, new BonusData(null, Effects.SATURATION, null));
        RAID_BONUSES.put(3, new BonusData(null, Effects.DAMAGE_RESISTANCE, null));
        RAID_BONUSES.put(4, new BonusData(null, Effects.ABSORPTION, null));
    }

    // 3. OMEN BONUSES (Only Active With Bad Omen Effect)
    private static final Map<Integer, BonusData> OMEN_BONUSES = new HashMap<>();
    static {
        OMEN_BONUSES.put(1, new BonusData(null, Effects.LUCK, null));
        OMEN_BONUSES.put(2, new BonusData(null, Effects.NIGHT_VISION, null)); // Note: Overrides BASE NV if applicable
        OMEN_BONUSES.put(3, new BonusData(null, Effects.INVISIBILITY, null));
        OMEN_BONUSES.put(4, new BonusData(null, Effects.REGENERATION, null));
    }

    private int armorLevel;

    public RaidArmor(IArmorMaterial materialIn, EquipmentSlotType slot, int armorLevel, Properties builder) {
        super(materialIn, slot, builder);
        this.armorLevel = armorLevel;
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (world.isClientSide) return;

        // --- Only the EFFECT LOGIC remains in onArmorTick ---
        int[] countAndLevel = ArmorHelper.getArmorCountAndTotalLevel(player, RaidArmor.class);
        int pieceCount = countAndLevel[0];
        int averageLevel = pieceCount > 0 ? countAndLevel[1] / pieceCount : 0;

        boolean inRaid = RaidHelper.isEntityInRaid(player);
        boolean hasOmen = player.hasEffect(Effects.BAD_OMEN);

        // EFFECT LOGIC (Always runs every 20 ticks if pieceCount > 0)
        if (player.tickCount % 100 == 0 && pieceCount > 0) {
            for (int tier = 1; tier <= 4; tier++) {
                if (tier <= pieceCount) {
                    applySingleEffectBonus(player, BASE_BONUSES.get(tier), averageLevel);
                    if (inRaid) {
                        applySingleEffectBonus(player, RAID_BONUSES.get(tier), averageLevel);
                    }
                    if (hasOmen) {
                        applySingleEffectBonus(player, OMEN_BONUSES.get(tier), averageLevel);
                    }
                }
            }
        }
    }

    /**
     * Handles the application of a single ATTRIBUTE bonus.
     */
    private static void applySingleAttributeBonus(PlayerEntity player, BonusData bonus, int tierIndex, int totalLevel, int averageLevel) {
        if (bonus == null || bonus.attribute == null) return;

        Attribute attributeKey = bonus.attribute;
        double value = 0;
        AttributeModifier.Operation operation = AttributeModifier.Operation.ADDITION;

        // Custom calculation logic:
        if (attributeKey.equals(Attributes.ATTACK_DAMAGE)) {
            // Damage (0.01 * total armor level)
            value = 0.01 * totalLevel;
            operation = AttributeModifier.Operation.MULTIPLY_BASE;
        } else if (attributeKey.equals(Attributes.MAX_HEALTH)) {
            // Health Boost (average armor level * 2) - Round up to 2.
            value = averageLevel + 1;
            operation = AttributeModifier.Operation.ADDITION;
        } else if (attributeKey.equals(Attributes.ARMOR_TOUGHNESS)) {
            // Armor Toughness (0.1 * total armor level)
            value = 0.1 * totalLevel;
            operation = AttributeModifier.Operation.ADDITION;
        } else if (attributeKey.equals(Attributes.ARMOR)) {
            // Armor (0.1 * average armor level)
            value = 0.1 * averageLevel + 1;
            operation = AttributeModifier.Operation.ADDITION;
        }

        ModifiableAttributeInstance modifierInstance = player.getAttribute(attributeKey);

        // Crucial Check: Only add if value is not zero and the modifier is NOT already present.
        // Since we removed all modifiers beforehand, we just need to check if value != 0
        if (modifierInstance != null && value != 0) {
            AttributeModifier modifier = new AttributeModifier(bonus.attributeUUID,
                    "Raid Armor Tier " + tierIndex + " Bonus",
                    value,
                    operation);

            // Double check to ensure it doesn't already exist from the same tick (shouldn't happen with the full remove)
            if (modifierInstance.getModifier(bonus.attributeUUID) == null) {
                modifierInstance.addTransientModifier(modifier);
            }
        }
    }

    // New Static Helper to apply attributes (Called by Event Handler)
    public static void applyAllAttributes(PlayerEntity player, int pieceCount, int totalLevel, boolean inRaid, boolean hasOmen) {
        int averageLevel = pieceCount > 0 ? totalLevel / pieceCount : 0;
        for (int tier = 1; tier <= 4; tier++) {
            // BASE BONUSES
            if (tier <= pieceCount) {
                applySingleAttributeBonus(player, BASE_BONUSES.get(tier), tier, totalLevel, averageLevel);
            }
            // RAID BONUSES
            if (inRaid && tier <= pieceCount) {
                applySingleAttributeBonus(player, RAID_BONUSES.get(tier), tier, totalLevel, averageLevel);
            }
            // OMEN BONUSES
            if (hasOmen && tier <= pieceCount) {
                applySingleAttributeBonus(player, OMEN_BONUSES.get(tier), tier, totalLevel, averageLevel);
            }
        }
    }

    /**
     * Handles the application of a single EFFECT bonus (every 20 ticks).
     */
    private static void applySingleEffectBonus(PlayerEntity player, BonusData bonus, int averageLevel) {
        if (bonus == null || bonus.effect == null) return;

        Effect effect = bonus.effect;
        int duration = 105; // 1.25 seconds
        int currentAmplifier = averageLevel;

        // Hard Caps (Regeneration, Nightvision, Invisibility should not go beyond amplifier 0)
        if (
                effect.equals(Effects.REGENERATION) ||
                        effect.equals(Effects.NIGHT_VISION) ||
                        effect.equals(Effects.INVISIBILITY)
        ) {
            currentAmplifier = 0;
        } else if (effect.equals(Effects.ABSORPTION)) {
            currentAmplifier = Math.max(0, currentAmplifier - 1); // Absorption amplifier starts at 0 for 2 hearts
        }

        // Night Vision Duration Fix (set to 30s)
        if (effect.equals(Effects.NIGHT_VISION)) {
            duration = 600; // 30 seconds * 20 ticks/sec
        }

        // Ensure currentAmplifier is not negative
        currentAmplifier = Math.max(0, currentAmplifier);

        EffectInstance newInstance = new EffectInstance(effect, duration, currentAmplifier, true, true);
        player.addEffect(newInstance);
    }

    /**
     * Utility method to remove all possible Raid Armor attribute modifiers.
     */
    public static void removeAllAttributes(LivingEntity entity) {
        if (entity == null) return;

        // Use a combined list of all potential attribute-modifying tiers
        List<BonusData> allAttributeBonuses = new ArrayList<>();

        // Add all BASE attribute bonuses (tiers 1-4)
        for (int tier = 1; tier <= 4; tier++) {
            BonusData baseBonus = BASE_BONUSES.get(tier);
            if (baseBonus != null && baseBonus.attribute != null) {
                allAttributeBonuses.add(baseBonus);
            }
        }

        // Add all RAID attribute bonuses (tiers 1-4)
        for (int tier = 1; tier <= 4; tier++) {
            BonusData raidBonus = RAID_BONUSES.get(tier);
            if (raidBonus != null && raidBonus.attribute != null) {
                allAttributeBonuses.add(raidBonus);
            }
        }

        // Add all OMEN attribute bonuses (tiers 1-4) - (currently none, but good for future-proofing)
        for (int tier = 1; tier <= 4; tier++) {
            BonusData omenBonus = OMEN_BONUSES.get(tier);
            if (omenBonus != null && omenBonus.attribute != null) {
                allAttributeBonuses.add(omenBonus);
            }
        }

        for (BonusData bonus : allAttributeBonuses) {
            // The UUID is the key to removal
            if (bonus.attribute != null) {
                ModifiableAttributeInstance instance = entity.getAttribute(bonus.attribute);
                if (instance != null && instance.getModifier(bonus.attributeUUID) != null) {
                    instance.removeModifier(bonus.attributeUUID);
                    TechniRaiders.LOGGER.debug("Removed Attribute: {} with UUID: {}", bonus.attribute.getDescriptionId(), bonus.attributeUUID);
                }
            }
        }
    }

    @Override
    public int getItemLevel() {
        return armorLevel;
    }

    // Struct Class
    private static class BonusData {
        public final Attribute attribute;
        public final Effect effect;
        public final UUID attributeUUID;

        // Use a string for "none" to handle the case where a tier doesn't grant a specific bonus
        public BonusData(Attribute attribute, Effect effect, UUID attributeUUID) {
            this.attribute = attribute;
            this.effect = effect;
            this.attributeUUID = attributeUUID;
        }
    }
}
