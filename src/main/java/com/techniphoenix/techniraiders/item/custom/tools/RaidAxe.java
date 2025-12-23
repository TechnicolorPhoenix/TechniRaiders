package com.techniphoenix.techniraiders.item.custom.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.techniphoenix.techniraiders.item.component.RaidItemComponent;
import com.techniphoenix.techniraiders.item.interfaces.DynamicAttributeComponent;
import com.techniphoenix.techniraiders.item.interfaces.IDamageScaling;
import com.techniphoenix.techniraiders.item.interfaces.IRaidItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.*;

public class RaidAxe extends AxeItem implements IDamageScaling, IRaidItem
{
    private final RaidItemComponent raidItemHandler;
    private final DynamicAttributeComponent dynamicAttributeHandler;

    private static List<Effect> createEffectList() {
        List<Effect> effects = new ArrayList<>();
        effects.add(Effects.REGENERATION);
        effects.add(Effects.DAMAGE_RESISTANCE);
        effects.add(Effects.DIG_SPEED);
        effects.add(Effects.LUCK);
        return effects;
    }

    private static final List<Effect> effects = createEffectList();

    /**
     * Constructor for the RaidAxe.
     * * @param tier The material tier of the pickaxe.
     *
     * @param tier         The weapon tier.
     * @param attackDamage The base attack damage.
     * @param attackSpeed  The attack speed modifier.
     * @param properties   Item properties.
     */
    public RaidAxe(IItemTier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
        this.raidItemHandler = new RaidItemComponent(EquipmentSlotType.MAINHAND);
        this.dynamicAttributeHandler = new DynamicAttributeComponent(RaidItemComponent.SCALING_DAMAGE_MODIFIER_UUID, raidItemHandler.equipSlot);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float baseSpeed = super.getDestroySpeed(stack, state);
        return baseSpeed * (1.0F + raidItemHandler.getDestroyBonus(stack));
    }

    /**
     * Overrides the default attribute map to inject our custom scaling damage modifier.
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(super.getAttributeModifiers(equipmentSlot, stack));

        dynamicAttributeHandler.setAttributeModifiers(
            equipmentSlot, builder,
            () -> true,
            () -> raidItemHandler.getBonusDamage(stack)
        );

        return builder.build();
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        return raidItemHandler.useBadOmen(world, player, stack, effects);
    }

    @Override
    public RaidItemComponent getRaidComponent() {
        return this.raidItemHandler;
    }
}
