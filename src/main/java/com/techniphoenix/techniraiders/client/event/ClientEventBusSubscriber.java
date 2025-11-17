package com.techniphoenix.techniraiders.client.event;

import com.techniphoenix.techniraiders.TechniRaiders;
import com.techniphoenix.techniraiders.item.armor.RaidArmorItems;
import com.techniphoenix.techniraiders.item.custom.armor.RaidArmor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TechniRaiders.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Get the ItemColors instance
            ItemColors itemColors = event.getMinecraftSupplier().get().getItemColors();

            // Create the IItemColor implementation:
            // It gets the color from the ItemStack (which RaidArmor handles via DyeableArmorItem)
            // The layer is always 0 (for the base texture)
            IItemColor brownColorHandler = (stack, tintIndex) -> {
                // tintIndex 0 is the colored layer (layer0 in your model)
                // tintIndex 1 is the uncolored layer (layer1 in your model, the overlay)
                if (tintIndex == 0) {
                    // Use the getColor method from DyeableArmorItem
                    return ((RaidArmor) stack.getItem()).getColor(stack);
                }
                return 0x78563A; // Default color (white) for the overlay layer
            };
            IItemColor whiteColorHandler = (stack, tintIndex) -> {
                // tintIndex 0 is the colored layer (layer0 in your model)
                // tintIndex 1 is the uncolored layer (layer1 in your model, the overlay)
                if (tintIndex == 0) {
                    // Use the getColor method from DyeableArmorItem
                    return ((RaidArmor) stack.getItem()).getColor(stack);
                }
                return 0xFFFFFF; // Default color (white) for the overlay layer
            };

            // Register the color handler for ALL your RaidArmor pieces
            // You will need to replace `YourModItems.RAID_HELMET.get()` with your actual item registry references.
            itemColors.register(brownColorHandler,
                    RaidArmorItems.RAID_LEATHER_ARMOR_HELMET.get(),
                    RaidArmorItems.RAID_LEATHER_ARMOR_CHESTPLATE.get(),
                    RaidArmorItems.RAID_LEATHER_ARMOR_LEGGINGS.get(),
                    RaidArmorItems.RAID_LEATHER_ARMOR_BOOTS.get()
            );
            itemColors.register(whiteColorHandler,

                    RaidArmorItems.RAID_IRON_ARMOR_HELMET.get(),
                    RaidArmorItems.RAID_IRON_ARMOR_CHESTPLATE.get(),
                    RaidArmorItems.RAID_IRON_ARMOR_LEGGINGS.get(),
                    RaidArmorItems.RAID_IRON_ARMOR_BOOTS.get(),

                    RaidArmorItems.RAID_EMERALD_ARMOR_HELMET.get(),
                    RaidArmorItems.RAID_EMERALD_ARMOR_CHESTPLATE.get(),
                    RaidArmorItems.RAID_EMERALD_ARMOR_LEGGINGS.get(),
                    RaidArmorItems.RAID_EMERALD_ARMOR_BOOTS.get()
            );
        });
    }
}