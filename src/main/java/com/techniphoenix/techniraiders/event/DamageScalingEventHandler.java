package com.techniphoenix.techniraiders.event;

import com.techniphoenix.techniraiders.TechniRaiders;
import com.techniphoenix.techniraiders.item.custom.tools.RaidSword;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractRaiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechniRaiders.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DamageScalingEventHandler {
    private static final double DAMAGE_INCREASE_AMOUNT = 0.1D;
    private static final double MAX_BONUS_DAMAGE = 5.0D;

    /**
     * Handles the LivingDeathEvent to either increase or reset the sword's damage bonus.
     */
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntityLiving();
        DamageSource source = event.getSource();

        // --------------------------------------------------
        // Part 1: Increase Damage on Pillager-type Kill
        // --------------------------------------------------

        // Check if the killer is a Player
        if (source.getDirectEntity() instanceof PlayerEntity) {
            PlayerEntity killer = (PlayerEntity) source.getDirectEntity();
            ItemStack heldItem = killer.getItemInHand(Hand.MAIN_HAND);

            // Check if the killer is using our custom sword
            if (heldItem.getItem() instanceof RaidSword) {

                // Check if the victim is a Pillager-type unit
                if (isRaiderType(victim)) {
                    double currentBonus = RaidSword.getBonusDamage(heldItem);

                    // Calculate the new bonus damage, capping at MAX_BONUS_DAMAGE
                    double newBonus = Math.min(currentBonus + DAMAGE_INCREASE_AMOUNT, MAX_BONUS_DAMAGE);

                    if (newBonus > currentBonus) {
                        RaidSword.setBonusDamage(heldItem, newBonus);
                        // Force the client to update the item attributes (e.g., refresh tooltip)
                        heldItem.getAttributeModifiers(EquipmentSlotType.MAINHAND);

                        // Optional: Send a chat message to the player
                        killer.displayClientMessage(
                                new net.minecraft.util.text.StringTextComponent(
                                        String.format("§aPillager Slayer damage increased to +%.1f!", newBonus)
                                ),
                                true
                        );
                    }
                }
            }
        }

        // --------------------------------------------------
        // Part 2: Reset Damage on Player Death
        // --------------------------------------------------

        // Check if the dying entity is a Player
        if (victim instanceof PlayerEntity) {
            PlayerEntity dyingPlayer = (PlayerEntity) victim;
            ItemStack heldItem = dyingPlayer.getItemInHand(Hand.MAIN_HAND);

            // Check if the player was holding our custom sword
            if (heldItem.getItem() instanceof RaidSword) {

                // Check if the bonus damage is currently greater than 0.0 (the base)
                if (RaidSword.getBonusDamage(heldItem) > 0.0D) {

                    // Reset the damage bonus back to its initial value (0.0D)
                    RaidSword.setBonusDamage(heldItem, 0.0D);

                    // Force the client to update the item attributes
                    heldItem.getAttributeModifiers(EquipmentSlotType.MAINHAND);

                    // Optional: Send a chat message to the player
                    dyingPlayer.displayClientMessage(
                            new net.minecraft.util.text.StringTextComponent(
                                    "§cYou died! Pillager Slayer damage bonus reset."
                            ),
                            false
                    );
                }
            }
        }
    }

    /**
     * Checks if the entity is a type of Pillager, Vindicator, or Evoker.
     */
    private static boolean isRaiderType(LivingEntity entity) {
        return entity instanceof AbstractRaiderEntity;
        // You can add more types if you consider Witches or Ravagers "Pillager-type"
    }
}