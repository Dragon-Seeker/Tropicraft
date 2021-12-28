package net.tropicraft.core.common.item.scuba;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.sync.PlayerSyncPredicate;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerSyncCallback;
import dev.onyxstudios.cca.internal.entity.CardinalComponentsEntity;
import net.bermuda.common.components.MyComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.dimension.TropicraftDimension;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class ScubaData implements PlayerComponent<ScubaData>, AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent{ // INBTSerializable<CompoundTag>, ICapabilityProvider {

    private static final Set<ServerPlayer> underwaterPlayers = Collections.newSetFromMap(new WeakHashMap<>());

    private Player owner;

    public ScubaData(Player owner){
        this.owner = owner;
    }

    @Override
    public void serverTick() {
        Level world = owner.level;
        // TODO support more than chest slot?
        ItemStack chestStack = owner.getItemBySlot(EquipmentSlot.CHEST);
        Item chestItem = chestStack.getItem();
        if (chestItem instanceof ScubaArmorItem) {

            if (!world.isClientSide) {
                underwaterPlayers.add((ServerPlayer) owner);
            }
            if (isUnderWater(owner)) {
                tick();
                ((ScubaArmorItem)chestItem).tickAir(owner, EquipmentSlot.CHEST, chestStack);
                if (!world.isClientSide && world.getGameTime() % 60 == 0) {
                    // TODO this effect could be better, custom packet?
                    Vec3 eyePos = owner.getEyePosition(0);
                    Vec3 motion = owner.getDeltaMovement();
                    Vec3 particlePos = eyePos.add(motion.reverse());
                    ((ServerLevel) world).sendParticles(ParticleTypes.BUBBLE,
                            particlePos.x(), particlePos.y(), particlePos.z(),
                            4 + world.random.nextInt(3),
                            0.25, 0.25, 0.25, motion.length());
                }
            } else if (!world.isClientSide && underwaterPlayers.remove(owner)) { // Update client state as they leave the water
                //data.ifPresent(d -> d.updateClient((ServerPlayer) owner, false));
                MyComponents.SCUBADATA.sync(this.owner);
            }
        }

    }

    private long diveTime;
    private double maxDepth;
    
    private boolean dirty;
    
    public static boolean isUnderWater(Player player) {
        BlockPos headPos = new BlockPos(player.getEyePosition(0));
        return player.level.getFluidState(headPos).is(FluidTags.WATER);
    }
    
    public static double getDepth(Player player) {
        if (isUnderWater(player)) {
            int surface = TropicraftDimension.getSeaLevel(player.level);
            double depth = surface - (player.getEyePosition(0).y());
            return depth;
        }
        return 0;
    }

    public void tick() {
        this.diveTime++;
        if (owner.level.getGameTime() % 100 == 0) {
            dirty = true;
        }
        updateMaxDepth(getDepth(owner));
    }

    public long getDiveTime() {
        return diveTime;
    }
    
    void updateMaxDepth(double depth) {
        if (depth > maxDepth) {
            this.maxDepth = depth;
        }
    }

    public double getMaxDepth() {
        return maxDepth;
    }

    @Override
    public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) {
        return PlayerComponent.super.shouldCopyForRespawn(lossless, keepInventory, sameCharacter);
    }

    @Override
    public void copyForRespawn(ScubaData original, boolean lossless, boolean keepInventory, boolean sameCharacter) {
        PlayerComponent.super.copyForRespawn(original, lossless, keepInventory, sameCharacter);
    }

    public void copyFrom(ScubaData data) {
        this.diveTime = data.getDiveTime();
        this.maxDepth = data.getMaxDepth();
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.diveTime = tag.getLong("diveTime");
        this.maxDepth = tag.getDouble("maxDepth");
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putLong("diveTime", diveTime);
        tag.putDouble("maxDepth", maxDepth);
    }

    public void serializeBuffer(FriendlyByteBuf buf) {
        buf.writeLong(diveTime);
        buf.writeDouble(maxDepth);
    }
    
    public void deserializeBuffer(FriendlyByteBuf buf) {
        this.diveTime = buf.readLong();
        this.maxDepth = buf.readDouble();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return player == this.owner;
    }

    @Override
    public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer recipient) {
        this.serializeBuffer(buf);
    }

    @Override
    public void applySyncPacket(FriendlyByteBuf buf) {
        this.deserializeBuffer(buf);
    }

    @Override
    public void clientTick() {
        ItemStack chestStack = owner.getItemBySlot(EquipmentSlot.CHEST);
        Item chestItem = chestStack.getItem();
        if (chestItem instanceof ScubaArmorItem) {
            if (isUnderWater(owner)) {
                tick();
            }
        }
    }
}
