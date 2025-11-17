package com.techniphoenix.techniraiders.event;

import com.techniphoenix.techniraiders.helper.ArmorHelper;
import com.techniphoenix.techniraiders.helper.RaidHelper;
import com.techniphoenix.techniraiders.item.custom.armor.RaidArmor;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;


@Mod.EventBusSubscriber(modid = "TechniRaiders")
public class RaidArmorEventHandler {
    // Removed static final EffectInstance declarations. They will be instantiated safely inside the event method.

// Define a static UUID for the Health Boost Attribute Modifier
    private static final UUID HEALTH_BOOST_MODIFIER_UUID = UUID.fromString("08800D61-1279-4A7B-900D-016E2DE45B0A");

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().tickCount % 20 != 0) {
            return;
        }

        if (event.getEntityLiving() instanceof CreatureEntity && event.getEntityLiving() instanceof IAngerable) {
            CreatureEntity creatureEntity = (CreatureEntity) event.getEntityLiving();

            int[] countAndTotalLevel = ArmorHelper.getArmorCountAndTotalLevel(creatureEntity, RaidArmor.class);

            int armorPieceCount = countAndTotalLevel[0];
            int totalLevel = countAndTotalLevel[1];
            int armorLevel = totalLevel / armorPieceCount;

            if (RaidHelper.isEntityInRaid(creatureEntity))
                armorLevel ++;

            if (armorPieceCount > 0) {

                // Calculation: 1 health point per 2 armor pieces + 1 health point per armor level
                // Max HP boost: (4/2) + 1 = 3 (for a full level 1 set)
                double maxHealthIncrease = Math.max(armorPieceCount*2, 2) + armorLevel*2;

                ModifiableAttributeInstance attributeInstance = creatureEntity.getAttribute(Attributes.MAX_HEALTH);

                if (attributeInstance != null) {
                    AttributeModifier existingModifier = attributeInstance.getModifier(HEALTH_BOOST_MODIFIER_UUID);

                    // Add or Update the modifier
                    AttributeModifier newModifier = new AttributeModifier(
                            HEALTH_BOOST_MODIFIER_UUID,
                            "RaidArmorHealthBoost",
                            maxHealthIncrease,
                            AttributeModifier.Operation.ADDITION
                    );

                    // Check if a modifier is already present with the same UUID
                    if (existingModifier == null) {
                        attributeInstance.addTransientModifier(newModifier);
                    } else if (existingModifier.getAmount() != maxHealthIncrease) {
                        // If the level has changed, remove the old one and add the new one
                        attributeInstance.removeModifier(HEALTH_BOOST_MODIFIER_UUID);
                        attributeInstance.addTransientModifier(newModifier);
                    }
                }
            } else {
                // If no armor is worn, ensure the modifier is removed
                ModifiableAttributeInstance attributeInstance = creatureEntity.getAttribute(Attributes.MAX_HEALTH);
                if (attributeInstance != null) {
                    attributeInstance.removeModifier(HEALTH_BOOST_MODIFIER_UUID);
                }
            }
            if (armorPieceCount > 1) {

                EffectInstance villagerMovespeed = new EffectInstance(Effects.MOVEMENT_SPEED,
                        21,
                         Math.max(armorPieceCount-2, armorLevel),
                        false,
                        false
                );

                creatureEntity.addEffect(villagerMovespeed);
            }
            if (armorPieceCount > 2) {

                EffectInstance villagerDamageResistance = new EffectInstance(Effects.DAMAGE_RESISTANCE,
                        21,
                        Math.max(armorPieceCount-2, armorLevel),
                        false,
                        false
                );

                creatureEntity.addEffect(villagerDamageResistance);
            }
            if (armorPieceCount > 3) {
                EffectInstance villagerAbsorption = new EffectInstance(Effects.ABSORPTION,
                        21,
                        0,
                        false,
                        false
                );

                creatureEntity.addEffect(villagerAbsorption);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamageEvent(LivingDamageEvent event) {
        // Prevent event logic on the client side for this server-side event
        if (event.getEntity().level.isClientSide) {
            return;
        }

        LivingEntity victim = event.getEntityLiving();
        LivingEntity attacker = (event.getSource().getEntity() instanceof LivingEntity)
                ? (LivingEntity) event.getSource().getEntity()
                : null;

        int[] victimCountAndTotalLevel = ArmorHelper.getArmorCountAndTotalLevel(victim, RaidArmor.class);

        int victimArmorCount = victimCountAndTotalLevel[0];
        int victimTotalLevel = victimCountAndTotalLevel[1];
        int victimArmorLevel = victimTotalLevel / victimArmorCount;

        if (RaidHelper.isEntityInRaid(victim))
            victimArmorLevel ++;

        if ((attacker instanceof AbstractIllagerEntity || attacker instanceof AgeableEntity) && victimArmorCount > 0) {

            if (RaidHelper.isEntityInRaid(victim))
                victimArmorCount ++;

            // Damage reduction is 6% per armor piece + 2% per armor level (max 48% for full set and level)
            float reductionPerPiece = RaidArmor.damageReductionPerPiece;
            float reductionPerLevel = RaidArmor.damageReductionPerLevel;
            float damageDecreasePercentage = reductionPerPiece * victimArmorCount + reductionPerLevel * victimArmorLevel;

            // Calculate new damage: damage * (1 + increase_percentage)
            float newDamage = event.getAmount() * (1.0f - damageDecreasePercentage);
            event.setAmount(newDamage);
        }

        // --- 2. Raid Armor Death Prevention Effect (4-piece set bonus) ---

        // ONLY proceed if the damage is lethal (or would be lethal)
        if (victim.getHealth() - event.getAmount() <= 0.0F) {
            // Check if the entity is wearing the full Raid Armor set
            if (victimArmorCount == 4) {

                // Define effects inside the method for safe registry access
                final EffectInstance absorption = new EffectInstance(Effects.ABSORPTION,
                        600, // 30 seconds
                        0,
                        true,
                        true
                );
                final EffectInstance regen = new EffectInstance(Effects.REGENERATION,
                        100, // 5 seconds
                        1, // Regeneration II
                        true,
                        true
                );

                // 3. Prevent the damage (and thus the death)
                event.setCanceled(true);

                victim.addEffect(regen);
                victim.addEffect(absorption);

                for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                    // 1.16.5 compatible check for armor slots
                    if (slot == EquipmentSlotType.HEAD || slot == EquipmentSlotType.CHEST || slot == EquipmentSlotType.LEGS || slot == EquipmentSlotType.FEET) {
                        if (victim.getItemBySlot(slot).getItem() instanceof RaidArmor) {
                            int toDamage = 550;
                            if (RaidHelper.isEntityInRaid(victim)) {
                                toDamage = 225;
                            }
                            victim.getItemBySlot(slot).hurtAndBreak(toDamage, victim, (e) -> e.broadcastBreakEvent(slot));
                        }
                    }
                }
            }
        }
    }
}