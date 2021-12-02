package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootContext;
import net.bermuda.common.forge.ForgeConstants;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlock;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public final class GrowableDoublePlantBlock extends DoublePlantBlock implements BonemealableBlock {
    private final Supplier<HugePlantBlock> growInto;
    private Supplier<? extends ItemLike> pickItem;

    public GrowableDoublePlantBlock(Properties properties, Supplier<HugePlantBlock> growInto) {
        super(properties);
        this.growInto = growInto;
    }

    public GrowableDoublePlantBlock setPickItem(Supplier<? extends ItemLike> item) {
        this.pickItem = item;
        return this;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter world, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, Random random, BlockPos pos, BlockState state) {
        BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();

        HugePlantBlock growBlock = this.growInto.get();
        BlockState growState = growBlock.defaultBlockState();
        if (growState.canSurvive(world, lowerPos)) {
            growBlock.placeAt(world, lowerPos, ForgeConstants.BlockFlags.BLOCK_UPDATE);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        if (state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return super.getDrops(state, builder);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        if (this.pickItem != null) {
            return new ItemStack(this.pickItem.get());
        }
        return super.getCloneItemStack(blockGetter, blockPos, blockState);
    }
}
