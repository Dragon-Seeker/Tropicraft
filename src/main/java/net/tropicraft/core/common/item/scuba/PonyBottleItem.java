package net.tropicraft.core.common.item.scuba;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.bermuda.common.events.ItemUseTickEvent;

//import javax.annotation.ParametersAreNonnullByDefault;

//@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PonyBottleItem extends Item implements ItemUseTickEvent.CanContinueUsing, ItemUseTickEvent.DuringItemUseTick {

    private static final int FILL_RATE = 6;
    
    public PonyBottleItem(Item.Properties properties) {
        super(properties);
    }
    
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }
    
    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getAirSupply() < player.getMaxAirSupply()) {
            player.startUsingItem(hand);
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
        } else {
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
    }

    @Override
    public void onUsingTick(LivingEntity entity, ItemStack stack, int useItemRemaining) {
        int fillAmt = FILL_RATE + 1; // +1 to counteract the -1 per tick while underwater
        // Wait for drink sound to start, and don't add air that won't fit
        if(entity instanceof Player player){
            if (player.getUseItemRemainingTicks() <= 25 && player.getAirSupply() < player.getMaxAirSupply() - fillAmt) {
                player.setAirSupply(player.getAirSupply() + fillAmt);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(p.getUsedItemHand()));
            }
        }
    }
    
    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return !shouldCauseReequipAnimation(oldStack, newStack, false);
    }
    

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack.getItem() != newStack.getItem();
    }


}