package net.tropicraft.core.common.dimension.feature.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.PianguasTreeDecorator;
import net.tropicraft.core.common.dimension.feature.tree.mangrove.PneumatophoresTreeDecorator;

public final class TropicraftTreeDecorators {
    //public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS = DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, Constants.MODID);
    public static TreeDecoratorType<PianguasTreeDecorator> PIANGUAS = register("pianguas", PianguasTreeDecorator.CODEC);
    public static TreeDecoratorType<PneumatophoresTreeDecorator> PNEUMATOPHORES = register("pneumatophores", PneumatophoresTreeDecorator.CODEC);
    public static TreeDecoratorType<PapayaTreeDecorator> PAPAYA = register("papaya", PapayaTreeDecorator.CODEC);

    private static <T extends TreeDecorator> TreeDecoratorType<T> register(String name, Codec<T> codec) {
        return Registry.register(Registry.TREE_DECORATOR_TYPES, name, new TreeDecoratorType<>(codec));
    }

    public static void init(){}
}
