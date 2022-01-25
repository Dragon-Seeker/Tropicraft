package net.tropicraft.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import java.util.Random;

public class TestPortalWaterBlock extends LiquidBlock {

    public static final BooleanProperty IS_POI_POSITION = BooleanProperty.create("is_poi");

    public TestPortalWaterBlock(Properties builder) {
        super(() -> Fluids.WATER, builder);

        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0).setValue(IS_POI_POSITION, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(IS_POI_POSITION);
    }
}
