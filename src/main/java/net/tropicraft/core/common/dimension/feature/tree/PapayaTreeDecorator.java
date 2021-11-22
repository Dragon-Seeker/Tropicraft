package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CocoaBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.tropicraft.Constants;
import net.tropicraft.core.common.Util;
import net.tropicraft.core.common.block.PapayaBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public final class PapayaTreeDecorator extends TreeDecorator {
    private Logger logger = LogManager.getLogger(Constants.MODID);

    public static final Codec<PapayaTreeDecorator> CODEC = Codec.unit(new PapayaTreeDecorator());

    public PapayaTreeDecorator() {
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return TropicraftTreeDecorators.PAPAYA;
    }

    @Override
    public void place(LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> placed, Random random, List<BlockPos> logs, List<BlockPos> leaves) {
        int y;
        if(logs.size() - 1 < 0){
            logger.debug("It seems that there is no logs being put into the list causing a negative value and a out of index");
            return;
        }
        else{
            y = logs.get(logs.size() - 1).getY();
        }


        for (BlockPos log : logs) {
            if (log.getY() > y - 4) {
                if (random.nextInt(2) == 0) {
                    Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);

                    BlockPos pos = log.relative(direction);

                    if (pLevel.isStateAtPosition(pos, (blockState) -> blockState.is(Blocks.AIR))) {
                        BlockState blockstate = TropicraftBlocks.PAPAYA.defaultBlockState()
                                .setValue(PapayaBlock.AGE, random.nextInt(2))
                                .setValue(CocoaBlock.FACING, direction.getOpposite());

                        placed.accept(pos, blockstate);
                    }
                }
            }
        }

    }
}
