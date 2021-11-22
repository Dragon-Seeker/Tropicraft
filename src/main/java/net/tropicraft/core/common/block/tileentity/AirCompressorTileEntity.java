package net.tropicraft.core.common.block.tileentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tropicraft.core.common.block.AirCompressorBlock;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import org.jetbrains.annotations.NotNull;

public class AirCompressorTileEntity extends BlockEntity implements IMachineTile, BlockEntityClientSerializable {

    /** Is the compressor currently giving air */
    private boolean compressing;

    /** Number of ticks compressed so far */
    private int ticks;

    /** Amount of PSI (ticks of air time) to fill per tick */
    private static final int fillRate = 5; // fills 5x faster than it's used
    
    /** The stack that is currently being filled */
    @NotNull
    private ItemStack stack;
    
    private ScubaArmorItem tank;

    public AirCompressorTileEntity(BlockPos pos, BlockState blockState) {
        super(TropicraftTileEntityTypes.AIR_COMPRESSOR, pos, blockState);
        this.stack = ItemStack.EMPTY;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.compressing = nbt.getBoolean("Compressing");

        if (nbt.contains("Tank")) {
            setTank(ItemStack.of(nbt.getCompound("Tank")));
        } else {
            setTank(ItemStack.EMPTY);
        }
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        super.save(nbt);
        nbt.putBoolean("Compressing", compressing);

        CompoundTag var4 = new CompoundTag();
        this.stack.save(var4);
        nbt.put("Tank", var4);
        
        return nbt;
    }
    
    public void setTank(@NotNull ItemStack tankItemStack) {
        this.stack = tankItemStack;
        this.tank = !(stack.getItem() instanceof ScubaArmorItem) ? null : (ScubaArmorItem) stack.getItem();
    }
    
    @NotNull
    public ItemStack getTankStack() {
        return stack;
    }
    
    @NotNull
    public ScubaArmorItem getTank() {
        return tank;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, AirCompressorTileEntity blockEntity) {
        if (blockEntity.tank == null)
            return;

        int airContained = blockEntity.tank.getRemainingAir(blockEntity.getTankStack());
        int maxAir = blockEntity.tank.getMaxAir(blockEntity.getTankStack());

        if (blockEntity.compressing) {
            int overflow = blockEntity.tank.addAir(fillRate, blockEntity.getTankStack());
            blockEntity.ticks++;
            if (overflow > 0) {
                blockEntity.finishCompressing();
            }
        }
    }

    public boolean addTank(ItemStack stack) {
        if (tank == null && stack.getItem() instanceof ScubaArmorItem && ((ScubaArmorItem)stack.getItem()).providesAir()) {
            setTank(stack);
            this.compressing = true;
            setChanged();
            return true;
        }

        return false;
    }

    public void ejectTank() {
        if (!stack.isEmpty()) {
            if (!level.isClientSide) {
                ItemEntity tankItem = new ItemEntity(level, this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), stack);
                level.addFreshEntity(tankItem);
            }
        }

        setTank(ItemStack.EMPTY);
        setChanged();
        this.ticks = 0;
        this.compressing = false;
    }

    public boolean isDoneCompressing() {
        return this.ticks > 0 && !this.compressing;
    }

    public float getTickRatio(float partialTicks) {
        if (tank != null) {
            return (this.ticks + partialTicks) / (tank.getMaxAir(getTankStack()) * fillRate);
        }
        return 0;
    }

    public boolean isCompressing() {
        return this.compressing;
    }

    public void startCompressing() {
        this.compressing = true;
        setChanged();
    }

    public void finishCompressing() {
        this.compressing = false;
        this.ticks = 0;
        setChanged();
    }
    
    public float getBreatheProgress(float partialTicks) {
        if (isDoneCompressing()) {
            return 0;
        }
        return (float) (((((ticks + partialTicks) / 20) * Math.PI) + Math.PI) % (Math.PI * 2));
    }
    
    /* == IMachineTile == */
    
    @Override
    public boolean isActive() {
        return !getTankStack().isEmpty();
    }
    
    @Override
    public float getProgress(float partialTicks) {
        return getTickRatio(partialTicks);
    }
    
    @Override
    public Direction getDirection(BlockState state) {
        return state.getValue(AirCompressorBlock.FACING);
    }

    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
//    @Override
//    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
//        this.load(pkt.getTag());
//    }
//
//    protected void syncInventory() {
//        if (!level.isClientSide) {
//            PlayerLookup.world((ServerLevel) level).forEach((player) -> TropicraftPackets.CHANNEL.send(player, new MessageAirCompressorInventory(this)));
//        }
//    }

//    @Override
//    @Nullable
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        return new ClientboundBlockEntityDataPacket(this.worldPosition, 1, this.getUpdateTag());
//    }
//
//    @Override
//    public @NotNull CompoundTag getUpdateTag() {
//        CompoundTag nbttagcompound = this.save(new CompoundTag());
//        return nbttagcompound;
//    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return save(tag);
    }

    @Override
    public void sync() {
        BlockEntityClientSerializable.super.sync();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if(!level.isClientSide()){
            sync();
        }
    }
}
