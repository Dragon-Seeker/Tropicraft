package net.tropicraft.core.common.item.scuba;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.bermuda.common.forge.ItemEx;
import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.LazyLoadedValue;
import net.bermuda.common.forge.ForgeAttributes;
import net.tropicraft.Constants;

import java.util.UUID;

public class ScubaFlippersItem extends ScubaArmorItem implements DynamicAttributeTool, ItemEx {

    private static final AttributeModifier SWIM_SPEED_BOOST = new AttributeModifier(UUID.fromString("d0b3c58b-ff33-41f2-beaa-3ffa15e8342b"), Constants.MODID + ".scuba", 0.25, Operation.MULTIPLY_TOTAL);

    private final LazyLoadedValue<Multimap<Attribute, AttributeModifier>> boostedModifiers;

    public ScubaFlippersItem(ScubaType type, Properties properties) {
        super(type, EquipmentSlot.FEET, properties);

        this.boostedModifiers = new LazyLoadedValue<>(() ->
                ImmutableMultimap.<Attribute, AttributeModifier>builder()
                        .putAll(super.getDefaultAttributeModifiers(EquipmentSlot.FEET))
                        .put(ForgeAttributes.SWIM_SPEED, SWIM_SPEED_BOOST)
                        .build()
        );
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.FEET && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack) == 0) {
            return boostedModifiers.get();
        } else {
            return null;
        }
    }
}
