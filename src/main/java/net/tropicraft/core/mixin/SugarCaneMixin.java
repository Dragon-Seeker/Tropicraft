package net.tropicraft.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.tropicraft.core.common.block.BlockTropicraftSand;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Debug(export = true)
@Mixin(SugarCaneBlock.class)
public class SugarCaneMixin {

    @Unique
    private BlockState possibleTropicraftSand;

    @Inject(method = "canSurvive", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    public void grabSurviveBlockstate(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir){
        if(!cir.getReturnValue()) {
            this.possibleTropicraftSand = levelReader.getBlockState(blockPos.below());

            if (possibleTropicraftSand.getBlock() instanceof BlockTropicraftSand tropicraftSand && tropicraftSand.canSustainPlant(((SugarCaneBlock)(Object)this).defaultBlockState())) {
                BlockPos blockPos2 = blockPos.below();
                Iterator var6 = Direction.Plane.HORIZONTAL.iterator();


                while (var6.hasNext()) {
                    Direction direction = (Direction) var6.next();
                    BlockState blockState3 = levelReader.getBlockState(blockPos2.relative(direction));
                    FluidState fluidState = levelReader.getFluidState(blockPos2.relative(direction));
                    if (fluidState.is(FluidTags.WATER) || blockState3.is(Blocks.FROSTED_ICE)) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}
