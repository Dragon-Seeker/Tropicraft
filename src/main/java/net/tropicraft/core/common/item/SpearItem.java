package net.tropicraft.core.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.spear.ThrownBambooSpear;

import javax.annotation.Nullable;
import java.util.List;

public class SpearItem extends TridentItem {

	private final Tier tier;

	private final Multimap<Attribute, AttributeModifier> defaultModifiers;

	public SpearItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
		super(properties.defaultDurability(tier.getUses()));
		this.tier = tier;

		this.defaultModifiers = ImmutableMultimap.<Attribute, AttributeModifier>builder()
				.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamage, AttributeModifier.Operation.ADDITION))
				.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION))
				.build();
	}

	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		//Taken from Trident Item release Using
		if (entityLiving instanceof Player) {
			Player player = (Player)entityLiving;
			int i = this.getUseDuration(stack) - timeLeft;
			if (i >= 10) {
				int j = EnchantmentHelper.getRiptide(stack);
				if (j <= 0 || player.isInWaterOrRain()) {
					if (!worldIn.isClientSide) {
						stack.hurtAndBreak(1, player, (playerEntity) -> {
							playerEntity.broadcastBreakEvent(player.getUsedItemHand());
						});
						if (j == 0) {
							ThrownBambooSpear thrownspear = this.createSpear(worldIn, player, stack);
							thrownspear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
							if (player.getAbilities().instabuild) {
								thrownspear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							}

							worldIn.addFreshEntity(thrownspear);
							worldIn.playSound((Player)null, thrownspear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
							if (!player.getAbilities().instabuild) {
								player.getInventory().removeItem(stack);
							}
						}
					}

					player.awardStat(Stats.ITEM_USED.get(this));
					if (j > 0) {
						float f7 = player.getYRot();
						float f = player.getXRot();
						float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
						float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
						float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
						float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
						float f5 = 3.0F * ((1.0F + (float)j) / 4.0F);
						f1 = f1 * (f5 / f4);
						f2 = f2 * (f5 / f4);
						f3 = f3 * (f5 / f4);
						player.push((double)f1, (double)f2, (double)f3);
						player.startAutoSpinAttack(20);
						if (player.isOnGround()) {
							float f6 = 1.1999999F;
							player.move(MoverType.SELF, new Vec3(0.0D, (double)1.1999999F, 0.0D));
						}

						SoundEvent soundevent;
						if (j >= 3) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
						} else if (j == 2) {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
						} else {
							soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
						}

						worldIn.playSound((Player)null, player, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
					}

				}
			}
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
		return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getAttributeModifiers(slot, stack);
	}

	@Override
	public int getItemEnchantability(ItemStack stack) {
		return this.tier.getEnchantmentValue();
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
		return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if(enchantment.equals(Enchantments.CHANNELING)) {
			return false;
		}
		else {
			return super.canApplyAtEnchantingTable(stack, enchantment);
		}
	}

	private ThrownBambooSpear createSpear(Level level, Player player, ItemStack stack){
		ThrownBambooSpear spear = new ThrownBambooSpear(level, player, stack);
		spear.setEffectsFromItem(stack);
		return spear;
	}

	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		PotionUtils.addPotionTooltip(pStack, pTooltip, 0.125F);
	}

	public String getDescriptionId(ItemStack pStack) {
		return PotionUtils.getPotion(pStack).getName(this.getDescriptionId() + ".effect.");
	}

	public ItemStack getDefaultInstance() {
		return PotionUtils.setPotion(super.getDefaultInstance(), Potions.POISON);
	}
}
