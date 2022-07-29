package net.tropicraft.core.common.dimension;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;
import net.tropicraft.core.common.item.TropicraftItems;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PortalTropics implements ITeleporter {
    private static final Logger LOGGER = LogUtils.getLogger(); //"Tropicraft Portal"

    private static final Block PORTAL_BLOCK = TropicraftBlocks.PORTAL_WATER.get();

    private static final ResourceLocation PORTAL_TEMPLATE = new ResourceLocation(Constants.MODID, "portal/tropicraft_portal");
    private static Vec3i portalStructureSize = new Vec3i(1, 1,1);

    private static final int OFFSET_Y = 7;

    private final ServerLevel level;

    public static final int SEARCH_AREA = 256;//128;

    public PortalTropics(ServerLevel level) {
        this.level = level;

        level.getStructureManager().get(PORTAL_TEMPLATE).ifPresent((template) -> {
            portalStructureSize = template.getSize();
        });
    }

    @Nullable
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destLevel, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        long startTime = System.currentTimeMillis();
        PortalInfo portalInfo = findPortalInfoPoi(entity);

        if (portalInfo == null) {
            BlockPos worldPos = findSafePortalPos(this.level, entity);
            placePortalStructure(this.level, worldPos);

            portalInfo = findPortalInfoPoi(entity);
        }

        long finishTime = System.currentTimeMillis();
        debugLog("It took {} seconds for TeleporterTropics.placeInPortal to complete", (finishTime - startTime) / 1000.0F);

        return portalInfo;
    }

    public PortalInfo findPortalInfoPoi(Entity entity) {
        BlockPos portalPosition = searchForPortalPoi(level, entity.getOnPos());
        if (portalPosition == null) {
            debugLog("No Portal was found within the search radius");
            return null;
        }

        double newLocX = portalPosition.getX() + 0.5D;
        double newLocY = portalPosition.getY() + 0.5D;
        double newLocZ = portalPosition.getZ() + 0.5D;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getBlockState(portalPosition.relative(direction)).is(PORTAL_BLOCK)) {
                newLocX += direction.getStepX() * 0.5;
                newLocZ += direction.getStepZ() * 0.5;
            }
        }

        entity.setDeltaMovement(0, 0, 0);

        //If the player is entering the tropics, spawn an Encyclopedia Tropica
        //in the spawn portal chest (if they don't already have one AND one isn't
        //already in the chest)

        if (this.level.dimension() == TropicraftDimension.WORLD && entity instanceof Player player && !player.getInventory().contains(new ItemStack(TropicraftItems.NIGEL_STACHE.get()))) { //TODO [1.17]: Replace Nigel Stache item with encyclopedia when reimplemented
            //TODO improve this logical check to an NBT tag or something?
            BlockPos chestPosition = portalPosition.north(2).above();

            BambooChestBlockEntity chest = getOrPlaceChest(chestPosition);

            boolean hasEncyclopedia = chest.hasAnyOf(Set.of(TropicraftItems.NIGEL_STACHE.get()));

            // Give out a new encyclopedia
            if (!hasEncyclopedia) {
                for (int inv = 0; inv < chest.getContainerSize(); inv++) {
                    ItemStack stack = chest.getItem(inv);
                    if (stack.isEmpty()) {
                        chest.setItem(inv, new ItemStack(TropicraftItems.NIGEL_STACHE.get(), 1));
                        break;
                    }
                }
            }
        }

        debugLog("Portal Information given to the player [x: {}, y: {}, z: {}]", newLocX, newLocY, newLocZ);

        return new PortalInfo(new Vec3(newLocX, newLocY, newLocZ), Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }

    private BambooChestBlockEntity getOrPlaceChest(BlockPos chestPosition) {
        if (level.getBlockState(chestPosition).getBlock() == TropicraftBlocks.BAMBOO_CHEST.get() && level.getBlockEntity(chestPosition) instanceof BambooChestBlockEntity bambooChestBlockEntity) {
            return bambooChestBlockEntity;
        }

        level.setBlock(chestPosition, TropicraftBlocks.BAMBOO_CHEST.get().defaultBlockState(), Block.UPDATE_ALL);
        return (BambooChestBlockEntity) level.getBlockEntity(chestPosition);
    }

    public static BlockPos searchForPortalPoi(ServerLevel level, BlockPos blockPos) {
        PoiManager poiManager = level.getPoiManager();
        poiManager.ensureLoadedAndValid(level, blockPos, SEARCH_AREA);

        List<PoiRecord> poiRecords = poiManager.getInSquare(type -> type == TropicraftPoiTypes.TROPICRAFT_PORTAL.get(), blockPos, SEARCH_AREA, PoiManager.Occupancy.ANY).toList();
        if (poiRecords.isEmpty()) return null;

        for (PoiRecord poiRecord : poiRecords) {
            BlockPos pos = poiRecord.getPos();
            if (canPortalPoiExistAt(level, pos)) {
                debugLog("Current block Pos Values that was found using poi finder [{}}]", poiRecord.getPos());
                return pos;
            } else {
                //Removes the poirecord as it doesn't seem there is any portal water at that position
                poiManager.remove(poiRecord.getPos());
                debugLog("Removing POI Record at: [{}]", pos);
            }
        }

        return null;
    }

    private static boolean canPortalPoiExistAt(ServerLevel level, BlockPos portalPoi) {
        for (BlockPos pos : BlockPos.betweenClosed(portalPoi.offset(-1, 0, -1), portalPoi.offset(1, 0, 0))) {
            if (!level.getBlockState(pos).is(PORTAL_BLOCK)) {
                return false;
            }
        }
        return true;
    }

    public static BlockPos findSafePortalPos(ServerLevel level, Entity entity) {
        return findSafePortalPos(level, entity.getOnPos());
    }

    public static BlockPos findSafePortalPos(ServerLevel level, BlockPos entityPosition) {
        debugLog("Start portal search");

        int searchArea = 16;
        double closestSpot = -1D;

        level.getStructureManager().get(PORTAL_TEMPLATE).ifPresent((template) -> portalStructureSize = template.getSize());

        debugLog("Starting position of search method is at: [{}]", entityPosition);

        int entityX = Mth.floor(entityPosition.getX());
        int entityZ = Mth.floor(entityPosition.getZ());

        int y = getTerrainHeightAt(level, entityX, entityZ);

        int foundX = entityX;
        int foundY = y;
        int foundZ = entityZ;

        debugLog("Sea level of {} is at a y of [{}]", level, TropicraftDimension.getSeaLevel(level));

        //Check if the entity's new adjusted position based off the world's terrain height is already a valid place
        if (!isPositionSafe(level, entityX, y, entityZ)) {
            for (int x = entityX - searchArea; x <= entityX + searchArea; x++) {
                double distX = (x + 0.5D) - entityPosition.getX();
                for (int z = entityZ - searchArea; z <= entityZ + searchArea; z++) {
                    double distZ = (z + 0.5D) - entityPosition.getZ();

                    // Find topmost solid block at this x,z location
                    y = getTerrainHeightAt(level, x, z);

                    if (isPositionSafe(level, x, y, z)) {
                        double distY = (y + 0.5D) - entityPosition.getY();
                        double distance = distX * distX + distY * distY + distZ * distZ;
                        if (closestSpot < 0.0D || distance < closestSpot) {
                            closestSpot = distance;
                            foundX = x;
                            foundY = y;
                            foundZ = z;

                            debugLog("Current closest spot is: {}", closestSpot);
                            debugLog("Valid x, y, z postion are [ X:{}, Y:{}, Z:{} ]", foundX, y, foundZ);
                        }
                    }

                    if (closestSpot >= 0.0D && closestSpot < 1.0D) {
                        break;
                    }
                }
                if (closestSpot >= 0.0D && closestSpot < 1.0D) {
                    break;
                }
            }
        }

        final BlockPos foundPos = new BlockPos(foundX, foundY, foundZ);
        debugLog("Portal will be generating from this blockPos: [{}]", foundPos);
        return foundPos;
    }

    private static boolean isPositionSafe(ServerLevel world, int x, int y, int z) {
        int seaLevel = TropicraftDimension.getSeaLevel(world);
        int baseHeight = world.getChunkSource().getGenerator().getBaseHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, world);

        int surfaceY = y + 1;

        debugLog("-----------------------------------------");
        debugLog("Surface Y is at: [Y:{}]", y + 1);
        debugLog("Base Height is at: [Y:{}]", baseHeight);
        debugLog("Sea Level is at: [Y:{}]", seaLevel);

        BlockPos pos = new BlockPos(x, surfaceY, z);
        while (surfaceY >= seaLevel - 1 && (world.isEmptyBlock(pos) || world.getBlockState(pos).is(BlockTags.IMPERMEABLE) || !world.getBlockState(pos).is(TropicraftTags.Blocks.PORTAL_SURFACE))) {
            surfaceY = pos.getY();
            pos = pos.below();
        }

        // Only generate portal between sea level and sea level + 20
        if (surfaceY < seaLevel) { //y > seaLevel + 20 ||
            debugLog("The Height wasn't above the given seaLevel Y:[{}]", seaLevel);
            return false;
        }

        // Remove positions that have a major difference between what i'm guessing is the top most position of the chunk before carvers and other such generation features
        if (baseHeight - surfaceY >= 3) {
            debugLog("It seems that there might be a cave or deep crevasse");
            return false;
        }

        //This section of code will attempt to make sure a portal is not placed on too steep of a position

        final var minimumFlatnessX = portalStructureSize.getX() / 2;
        final var minimumFlatnessZ = portalStructureSize.getZ() / 2;

        final var portalArea = portalStructureSize.getX() * portalStructureSize.getZ();

        final var mutableBlockPos = new BlockPos.MutableBlockPos().setWithOffset(pos, -minimumFlatnessX, 0, -minimumFlatnessZ);

        var blocksAbovePosition = 0;
        var blocksBelowPosition = 0;

        double failedBlockPercentage = (1.0 / 10.0);

        for(int xOff = 0; xOff < portalStructureSize.getX(); xOff++){
            for(int zOff = 0; zOff < portalStructureSize.getZ(); zOff++){
                if((blocksAbovePosition / ((float) portalArea) > failedBlockPercentage) || (blocksBelowPosition / ((float) portalArea) > failedBlockPercentage)){
                    debugLog("There area for where the portal will be placed doesn't seem that flat: {}", new BlockPos(x, y, z));
                    return false;
                }

                BlockPos above = mutableBlockPos.above();

                //Check if there is no solid ground above where the portal will go
                if(world.getBlockState(above).is(TropicraftTags.Blocks.PORTAL_SURFACE) /*|| world.getBlockState(above).is(BlockTags.LOGS) || world.getBlockState(above).is(BlockTags.FEATURES_CANNOT_REPLACE) */){
                    blocksAbovePosition++;
                }

                BlockPos below = mutableBlockPos.below();

                //Check for solid ground below where the portal will go
                if(!world.getBlockState(below).is(TropicraftTags.Blocks.PORTAL_SURFACE)){
                    blocksBelowPosition++;
                }

                mutableBlockPos.move(0, 0 , 1);
            }

            mutableBlockPos.move(1, 0 , -portalStructureSize.getZ());
        }

        debugLog("-----------------------------------------");

        return true;
    }

    private static void debugLog(String debugInfo, Object... variables){
        if(!FMLEnvironment.production){
            LOGGER.debug(debugInfo, variables);
        }
    }

    public static void placePortalStructure(ServerLevel level, BlockPos pos) {
        level.getStructureManager().get(PORTAL_TEMPLATE).ifPresent(template -> {
            Vec3i size = template.getSize();
            BlockPos origin = new BlockPos(pos.getX() - size.getX() / 2, pos.getY() - OFFSET_Y, pos.getZ() - size.getZ() / 2);

            StructurePlaceSettings settings = new StructurePlaceSettings()
                    .setMirror(Mirror.NONE)
                    .setRotation(Rotation.NONE)
                    .setIgnoreEntities(false);

            template.placeInWorld(level, origin, origin, settings, level.random, Block.UPDATE_ALL);
        });

        level.getPoiManager().add(pos, TropicraftPoiTypes.TROPICRAFT_PORTAL.get());
    }

    /**
     * Gets the terrain height at the specified coordinates
     *
     * @param x The x coordinate
     * @param z The z coordinate
     * @return The terrain height at the specified coordinates
     */

    public static int getTerrainHeightAt(ServerLevel world, int x, int z) {
        LevelChunk chunk = world.getChunk(x >> 4, z >> 4);
        int worldSpawnY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(x, 0, z);
        for (int y = worldSpawnY; y > 0; y--) {
            BlockState state = world.getBlockState(mutablePos.setY(y));

            //TODO [1.17]: Confirm that these tags are going to work with modded blocks
            if (state.is(TropicraftTags.Blocks.PORTAL_SURFACE)) {
                return y;
            }
        }
        return 0;
    }
}
