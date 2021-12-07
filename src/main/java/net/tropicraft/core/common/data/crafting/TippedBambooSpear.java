package net.tropicraft.core.common.data.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.item.TropicraftItems;

public class TippedBambooSpear extends CustomRecipe {
    public TippedBambooSpear(ResourceLocation p_44503_) {
        super(p_44503_);
    }

    public boolean matches(CraftingContainer pInv, Level pLevel) {
        boolean isOneLingeringPotion = false;
        boolean isOneBambooSpear = false;

        for(int i = 0; i < pInv.getWidth(); ++i) {
            for(int j = 0; j < pInv.getHeight(); ++j) {
                ItemStack itemstack = pInv.getItem(i + j * pInv.getWidth());

                if(itemstack.is(Items.LINGERING_POTION)) {
                    isOneLingeringPotion = true;
                }
                else if(itemstack.is(TropicraftItems.BAMBOO_SPEAR.get())) {
                    isOneBambooSpear = true;
                }
                else if(isOneLingeringPotion && isOneBambooSpear && !itemstack.isEmpty()){
                    return false;
                }
            }
        }

        if(isOneLingeringPotion && isOneBambooSpear){
            return true;
        }
        else{
            return false;
        }
    }

    public ItemStack assemble(CraftingContainer pInv) {
        ItemStack itemstack = ItemStack.EMPTY;
        for(int i = 0; i <= 8; i++){
            if(pInv.getItem(1 + pInv.getWidth()).is(Items.LINGERING_POTION)){
                itemstack = pInv.getItem(1 + pInv.getWidth());
                break;
            }
        }
        ItemStack itemstack1 = new ItemStack(TropicraftItems.BAMBOO_SPEAR.get(), 1);
        PotionUtils.setPotion(itemstack1, PotionUtils.getPotion(itemstack));
        PotionUtils.setCustomEffects(itemstack1, PotionUtils.getCustomEffects(itemstack));
        return itemstack1;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public RecipeSerializer<?> getSerializer() {
        return TropicraftRecipeSerializer.TIPPED_SPEAR.get();
    }
}
