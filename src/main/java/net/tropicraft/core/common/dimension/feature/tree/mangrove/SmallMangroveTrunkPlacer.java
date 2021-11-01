package net.tropicraft.core.common.dimension.feature.tree.mangrove;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.Registry;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.tropicraft.core.common.dimension.feature.tree.TropicraftTrunkPlacers;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;

public class SmallMangroveTrunkPlacer extends TrunkPlacer {
    public static final Codec<SmallMangroveTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return trunkPlacerParts(instance)
                .and(Registry.BLOCK.fieldOf("roots_block").forGetter(c -> c.rootsBlock))
                .apply(instance, SmallMangroveTrunkPlacer::new);
    });

    private final Block rootsBlock;

    public SmallMangroveTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, Block rootsBlock) {
        super(baseHeight, heightRandA, heightRandB);
        this.rootsBlock = rootsBlock;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TropicraftTrunkPlacers.SMALL_MANGROVE;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> pBlockSetter, Random random, int height, BlockPos origin, TreeConfiguration config) {
        setDirtAt(world, pBlockSetter, random, origin.below(), config);

        for (int i = 0; i < height; ++i) {
            placeLog(world, pBlockSetter, random, origin.above(i), config);
        }

        generateRoots(world, pBlockSetter, random, origin, 0);

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(origin.above(height - 1), 1, false));
    }

    private void generateRoots(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> pBlockSetter, Random random, BlockPos origin, int depth) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos offset = origin.relative(direction);

            if (world.isStateAtPosition(offset, BlockBehaviour.BlockStateBase::isAir)) {
                if (world.isStateAtPosition(offset.below(), state -> state.getMaterial().isSolidBlocking())) {
                    pBlockSetter.accept(offset, this.rootsBlock.defaultBlockState());
                    //TreeFeature.setBlockKnownShape(world, offset, this.rootsBlock.defaultBlockState());

                    if (depth < 2 && random.nextInt(depth + 2) == 0) {
                        generateRoots(world, pBlockSetter, random, offset, depth + 1);
                    }
                }
            }
        }
    }

}
