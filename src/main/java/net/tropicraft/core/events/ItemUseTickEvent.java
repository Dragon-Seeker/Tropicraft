package net.tropicraft.core.events;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("all")
public interface ItemUseTickEvent {

    /**
     * An event that is called right before the LivingEntity is updatingUsingItem.
     *
     * @see net.minecraft.world.entity.LivingEntity#updatingUsingItem()
     */
    public static final Event<CanContinueUsing> CAN_CONTINUE_USING_EVENT = EventFactory.createArrayBacked(CanContinueUsing.class, callbacks -> (stack1, stack2) -> {
        for (CanContinueUsing callback : callbacks) {
            return callback.canContinueUsing(stack1, stack2);
        }
        return true;
    });

    /**
     * An event that is called right before the LivingEntity is updatingUsingItem.
     *
     * @see net.minecraft.world.entity.LivingEntity#updatingUsingItem()
     */
    public static final Event<BeforeItemUseTick> BEFORE_ITEM_USE_TICK = EventFactory.createArrayBacked(BeforeItemUseTick.class, callbacks -> (player, stack, useItemRemaining) -> {
        for (BeforeItemUseTick callback : callbacks) {
            callback.beforeItemUsingTick(player, stack, useItemRemaining);
        }
    });

    /**
     * An event that is called during the LivingEntity is updatingUsingItem.
     *
     * @see net.minecraft.world.entity.LivingEntity#updatingUsingItem()
     */
    public static final Event<DuringItemUseTick> DURING_ITEM_USE_TICK = EventFactory.createArrayBacked(DuringItemUseTick.class, callbacks -> (player, stack, useItemRemaining) -> {
        for (DuringItemUseTick callback : callbacks) {
            callback.onUsingTick(player, stack, useItemRemaining);
        }
    });

    /**
     * An event that is called right after the LivingEntity is updatingUsingItem.
     *
     * @see net.minecraft.world.entity.LivingEntity#updatingUsingItem()
     */
    public static final Event<AfterItemUseTick> AFTER_ITEM_USE_TICK  = EventFactory.createArrayBacked(AfterItemUseTick.class, callbacks -> (player, stack, useItemRemaining) -> {
        for (AfterItemUseTick callback : callbacks) {
            callback.postItemUsingTick(player, stack, useItemRemaining);
        }
    });

    @FunctionalInterface
    public interface CanContinueUsing {
        /**
         * NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
         *
         * @param stack1
         * @param stack2
         */
        boolean canContinueUsing(ItemStack stack1, ItemStack stack2);
    }

    @FunctionalInterface
    public interface BeforeItemUseTick {
        /**
         * Called before the player tick.
         *
         * @param entity
         * @param stack
         * @param useItemRemaining
         */
        void beforeItemUsingTick(LivingEntity entity, ItemStack stack, int useItemRemaining);
    }

    @FunctionalInterface
    public interface DuringItemUseTick {
        /**
         * Called before the player tick.
         *
         * @param entity
         * @param stack
         * @param useItemRemaining
         */
        void onUsingTick(LivingEntity entity, ItemStack stack, int useItemRemaining);
    }

    @FunctionalInterface
    public interface AfterItemUseTick {
        /**
         * Called after the player tick.
         *
         * @param entity
         * @param stack
         * @param useItemRemaining
         */
        void postItemUsingTick(LivingEntity entity, ItemStack stack, int useItemRemaining);
    }
}
