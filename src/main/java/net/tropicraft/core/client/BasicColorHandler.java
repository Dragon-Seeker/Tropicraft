package net.tropicraft.core.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.ItemStack;
import net.tropicraft.core.common.item.IColoredItem;
import org.jetbrains.annotations.NotNull;

public class BasicColorHandler implements ItemColor {

    @Override
    public int getColor(@NotNull ItemStack stack, int tintIndex) {
        if (stack.getItem() instanceof IColoredItem) {
            return ((IColoredItem)stack.getItem()).getColor(stack, tintIndex);
        }

        return 0xffffff;
    }
}
