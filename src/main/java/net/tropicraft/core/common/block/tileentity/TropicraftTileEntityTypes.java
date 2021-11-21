package net.tropicraft.core.common.block.tileentity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.tropicraft.Constants;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftTileEntityTypes {
    
    //public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MODID);

    public static final BlockEntityType<BambooChestTileEntity> BAMBOO_CHEST = registerBlockEntityType("bamboo_chest", FabricBlockEntityTypeBuilder.create(BambooChestTileEntity::new, TropicraftBlocks.BAMBOO_CHEST));
    public static final BlockEntityType<SifterTileEntity> SIFTER = registerBlockEntityType("sifter", FabricBlockEntityTypeBuilder.create(SifterTileEntity::new, TropicraftBlocks.SIFTER));
    public static final BlockEntityType<DrinkMixerTileEntity> DRINK_MIXER = registerBlockEntityType("drink_mixer", FabricBlockEntityTypeBuilder.create(DrinkMixerTileEntity::new, TropicraftBlocks.DRINK_MIXER));
    public static final BlockEntityType<AirCompressorTileEntity> AIR_COMPRESSOR = registerBlockEntityType("air_compressor", FabricBlockEntityTypeBuilder.create(AirCompressorTileEntity::new, TropicraftBlocks.AIR_COMPRESSOR));
    public static final BlockEntityType<VolcanoTileEntity> VOLCANO = registerBlockEntityType("tile_entity_volcano", FabricBlockEntityTypeBuilder.create(VolcanoTileEntity::new, TropicraftBlocks.VOLCANO));

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntityType(String id, FabricBlockEntityTypeBuilder<T> builder) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(Constants.MODID, id), builder.build(null));
    }

    public static void init(){}
}
