package net.tropicraft.core.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tropicraft.core.common.config.TropicraftConfig;
import net.tropicraft.core.common.dimension.PortalTropics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.tropicraft.core.common.item.PortalEnchanterItem.EnchanterMode.*;

public class PortalEnchanterItem extends Item {

    private static final Logger LOGGER_WAND = LogManager.getLogger(PortalEnchanterItem.class);

    private EnchanterMode enchanterMode = PORTAL_CREATION;
    private BlockPos cachedClosestPOI = BlockPos.ZERO;

    public enum EnchanterMode {
        PORTAL_CREATION(0, "tropicraft.enchanterMode.creation", ChatFormatting.AQUA),
        PORTAL_SEARCH(1, "tropicraft.enchanterMode.search", ChatFormatting.GREEN),
        PORTAL_DESTRUCTION(2, "tropicraft.enchanterMode.destroy", ChatFormatting.RED),
        PORTAL_DEBUG(3, "Debug Mode", ChatFormatting.LIGHT_PURPLE);

        private String translationKey;
        private int id;
        private ChatFormatting chatFormatting;

        EnchanterMode(int id, String string, ChatFormatting chatFormatting){
            this.translationKey = string;
            this.id = id;
            this.chatFormatting = chatFormatting;
        }

        public MutableComponent getTextComponent(){
            return new TranslatableComponent(this.translationKey).withStyle(this.chatFormatting);
        }

        public static EnchanterMode getEnchanterMode(int i){
            return EnchanterMode.values()[i];
        }

        public int getId(){
            return this.id;
        }
    }

    public PortalEnchanterItem(Properties pProperties) {
        super(pProperties);
    }



    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        PortalEnchanterItem enchanterItem = (PortalEnchanterItem)stack.getItem();

        CompoundTag tag = super.getShareTag(stack);
        if(tag == null){
            tag = new CompoundTag();
        }

        tag.putInt("poiX", enchanterItem.cachedClosestPOI.getX());
        tag.putInt("poiY", enchanterItem.cachedClosestPOI.getX());
        tag.putInt("poiZ", enchanterItem.cachedClosestPOI.getX());
        tag.putInt("mode", enchanterMode.getId());

        return tag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);

        if(nbt != null){
            PortalEnchanterItem enchanterItem = (PortalEnchanterItem)stack.getItem();

            enchanterItem.setCachedClosestPOI(nbt.getInt("poiX"),nbt.getInt("poiY"),nbt.getInt("poiZ"));
            enchanterItem.setEnchanterMode(nbt.getInt("mode"));
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if(pStack.getItem() instanceof PortalEnchanterItem portalEnchanterItem){
            pTooltipComponents.add(portalEnchanterItem.getEnchanterMode().getTextComponent());
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(!pContext.getLevel().isClientSide && pContext.getPlayer() instanceof ServerPlayer serverPlayer) {
            final boolean hasPermission = serverPlayer.canUseGameMasterBlocks() ||
                    serverPlayer.getServer().isSingleplayerOwner(serverPlayer.getGameProfile()) ||
                    TropicraftConfig.portalEnchanterWhitelist.get().contains(serverPlayer.getUUID().toString());

            if (hasPermission){
                ServerLevel world = (ServerLevel) pContext.getLevel();
                BlockPos clickedPos = pContext.getClickedPos();

                if (((PortalEnchanterItem) pContext.getItemInHand().getItem()).getEnchanterMode() == PORTAL_CREATION) {
                    if (!isPortalTooClose(world, clickedPos)) {
                        boolean flag = PortalTropics.placePortalStructure(world, serverPlayer, clickedPos);

                        if (!FMLEnvironment.production) {
                            if (flag) {
                                serverPlayer.displayClientMessage(new TextComponent("Debug: Portal was created!"), false);

                                return InteractionResult.SUCCESS;
                            } else {
                                serverPlayer.displayClientMessage(new TextComponent("Debug: Portal was not created"), false);

                                return InteractionResult.FAIL;
                            }
                        }
                    } else {
                        serverPlayer.sendMessage(new TranslatableComponent("tropicraft.portalEnchanterProximityWarning"), ChatType.GAME_INFO, serverPlayer.getUUID());
                        return InteractionResult.FAIL;
                    }
                }else if(((PortalEnchanterItem) pContext.getItemInHand().getItem()).getEnchanterMode() == PORTAL_DEBUG && !FMLEnvironment.production){
                    PortalTropics.findSafePortalPos(world, serverPlayer);
                }
            }
            else{
                serverPlayer.sendMessage(new TranslatableComponent("tropicraft.portalEnchanterWarning"), ChatType.GAME_INFO, serverPlayer.getUUID());
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
        if(pIsSelected && pEntity instanceof Player && pLevel.isClientSide){
            if((this.cachedClosestPOI != BlockPos.ZERO && this.cachedClosestPOI != null) && (((PortalEnchanterItem) pStack.getItem()).getEnchanterMode() == PORTAL_SEARCH || ((PortalEnchanterItem) pStack.getItem()).getEnchanterMode() == PORTAL_DESTRUCTION)){
                //Create particle effects at this location???
                if(pLevel.getGameTime() % 5 == 0){
                    Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.LARGE_SMOKE, cachedClosestPOI.getX() + .5D, cachedClosestPOI.getY() + .5D, cachedClosestPOI.getZ() + .5D, 0,0,0);
                    Minecraft.getInstance().particleEngine.createParticle(ParticleTypes.FLAME, cachedClosestPOI.getX() + .5D, cachedClosestPOI.getY() + .5D, cachedClosestPOI.getZ() + .5D, 0,0,0);
                    LOGGER_WAND.info("[Enchanter]: A particel was created around: [" + cachedClosestPOI + "]");
                }
            }
        }

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pPlayer instanceof ServerPlayer serverPlayer && !pLevel.isClientSide){
            if(serverPlayer.isShiftKeyDown()){
                PortalEnchanterItem enchanter = (PortalEnchanterItem)pPlayer.getItemInHand(pUsedHand).getItem();

                int mode = enchanter.getEnchanterMode().getId();

                if(mode < 2 || (!FMLEnvironment.production && mode < 3)){
                    mode++;
                }
                else {
                    mode = 0;
                }

                if(mode == PORTAL_SEARCH.getId()){
                    this.setCachedClosestPOI(PortalTropics.searchForPortalPoi((ServerLevel) pLevel, serverPlayer.getOnPos()));
                }else if(mode == PORTAL_CREATION.getId()){
                    this.setCachedClosestPOI(BlockPos.ZERO);
                }

                this.setEnchanterMode(mode);

                serverPlayer.sendMessage(EnchanterMode.getEnchanterMode(mode).getTextComponent(), ChatType.GAME_INFO, serverPlayer.getUUID());

                return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
            }
            else if(((PortalEnchanterItem)pPlayer.getItemInHand(pUsedHand).getItem()).getEnchanterMode() == PORTAL_SEARCH){
                this.cachedClosestPOI = PortalTropics.searchForPortalPoi((ServerLevel) pLevel, serverPlayer.getOnPos());
            }
        }


        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }

    public void setEnchanterMode(int i){
        if(i >= 0 && i <= 3){
            this.enchanterMode = EnchanterMode.getEnchanterMode(i);
        }
    }

    public EnchanterMode getEnchanterMode(){
        return this.enchanterMode;
    }

    private boolean isPortalTooClose(ServerLevel world, BlockPos blockPos){
        BlockPos portalPos = PortalTropics.searchForPortalPoi(world, blockPos);

        return portalPos != null;
    }

    private void setCachedClosestPOI(int x, int y, int z){
        setCachedClosestPOI(new BlockPos(x,y,z));
    }

    private void setCachedClosestPOI(BlockPos blockPos){
        this.cachedClosestPOI = blockPos;
    }
}
