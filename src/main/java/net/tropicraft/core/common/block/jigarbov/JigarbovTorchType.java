package net.tropicraft.core.common.block.jigarbov;

import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.tropicraft.core.common.block.TropicraftBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum JigarbovTorchType implements StringRepresentable {
    ACACIA("acacia", () -> Blocks.ACACIA_LOG),
    BIRCH("birch", () -> Blocks.BIRCH_LOG),
    DARK_OAK("dark_oak", () -> Blocks.DARK_OAK_LOG),
    JUNGLE("jungle", () -> Blocks.JUNGLE_LOG),
    OAK("oak", () -> Blocks.OAK_LOG),
    SPRUCE("spruce", () -> Blocks.SPRUCE_LOG),
    BLACK_MANGROVE("black_mangrove", () -> TropicraftBlocks.BLACK_MANGROVE_LOG),
    LIGHT_MANGROVE("light_mangrove", () -> TropicraftBlocks.LIGHT_MANGROVE_LOG),
    RED_MANGROVE("red_mangrove", () -> TropicraftBlocks.RED_MANGROVE_LOG);

    private static final JigarbovTorchType[] VALUES = values();

    private final String name;
    private final Supplier<? extends Block> log;

    JigarbovTorchType(String name, Supplier<? extends Block> log) {
        this.name = name;
        this.log = log;
    }

    @Nullable
    public static JigarbovTorchType byBlock(Block block) {
        if (BlockTags.LOGS.contains(block)) {
            for (JigarbovTorchType type : VALUES) {
                if (type.matchesLog(block)) {
                    return type;
                }
            }
            return OAK;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public boolean matchesLog(Block block) {
        return log.get() == block;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
