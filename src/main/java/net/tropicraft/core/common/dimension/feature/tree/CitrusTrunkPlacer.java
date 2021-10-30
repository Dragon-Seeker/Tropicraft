package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.tropicraft.core.common.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class CitrusTrunkPlacer extends TrunkPlacer {
    public static final Codec<CitrusTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return trunkPlacerParts(instance)
                .apply(instance, CitrusTrunkPlacer::new);
    });

    public CitrusTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TropicraftTrunkPlacers.CITRUS;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedRW world, Random random, int height, BlockPos origin, Set<BlockPos> logs, BoundingBox bounds, TreeConfiguration config) {
        ArrayList<FoliagePlacer.FoliageAttachment> leafNodes = new ArrayList<>();

        // Set grass to dirt
        setDirtAt(world, origin.below());

        // Place trunk
        for (int i = 0; i < height; ++i) {
            placeLog(world, random, origin.above(i), logs, bounds, config);
        }

        // Add center leaf cluster
        leafNodes.add(new FoliagePlacer.FoliageAttachment(origin.above(height - 1), 1, false));

        growBranches(world, random, origin.above(height - 4), logs, bounds, config, leafNodes);

        return leafNodes;
    }

    private void growBranches(LevelSimulatedRW world, Random random, BlockPos origin, Set<BlockPos> logs, BoundingBox bounds, TreeConfiguration config, List<FoliagePlacer.FoliageAttachment> leafNodes) {
        int count = random.nextInt(3) + 1;
        double thetaOffset = random.nextDouble() * 2 * Math.PI;

        // Place 1-3 branches
        for (int i = 0; i < count; i++) {
            // Get angle of this branch
            double theta = (((double) i / count) * 2 * Math.PI) + thetaOffset;

            // Add a random offset to the theta
            theta += random.nextDouble() * Math.PI * 0.15;

            // Make branches 3-4 blocks long
            int dist = random.nextInt(3) == 0 ? 4 : 3;

            for (int j = 1; j <= dist; j++) {
                int x = (int) (Math.cos(theta) * j);
                int y = j == dist ? 1 : 0; // Make branch go up
                int z = (int) (Math.sin(theta) * j);
                BlockPos local = origin.offset(x, y, z);

                // Get axis based on position
                Direction.Axis axis = Util.getAxisBetween(origin, local);

                // Place branch and add to logs
                setBlock(world, local, config.trunkProvider.getState(random, local).setValue(RotatedPillarBlock.AXIS, axis), bounds);
                logs.add(local);

                // Add leaves around the branch
                if (j == dist) {
                    leafNodes.add(new FoliagePlacer.FoliageAttachment(local, 0, false));
                }
            }
        }
    }
}
