package net.tropicraft.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.core.common.block.BlockTropicraftSand;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(CactusBlock.class)
public class CactusMixin {

    @Unique
    private BlockState possibleTropicraftSand;


    @Inject(method = "canSurvive", at = @At(value = "RETURN",ordinal = 1), cancellable = true)
    public void grabSurviveBlockstate(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir){
        this.possibleTropicraftSand = levelReader.getBlockState(blockPos.below());

        boolean sandTest = possibleTropicraftSand.getBlock() instanceof BlockTropicraftSand tropicraftSand && tropicraftSand.canSustainPlant(((CactusBlock)(Object)this).defaultBlockState());

        cir.setReturnValue((cir.getReturnValue() || sandTest) && !levelReader.getBlockState(blockPos.above()).getMaterial().isLiquid());

    }

}
