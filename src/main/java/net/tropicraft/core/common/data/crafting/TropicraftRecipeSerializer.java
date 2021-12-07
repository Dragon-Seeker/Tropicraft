package net.tropicraft.core.common.data.crafting;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.item.crafting.TippedArrowRecipe;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.core.common.item.Builder;

import java.util.function.Supplier;

public class TropicraftRecipeSerializer {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);

    public static final RegistryObject<SimpleRecipeSerializer<TippedBambooSpear>> TIPPED_SPEAR = register("crafting_special_tippedspear", () -> new SimpleRecipeSerializer<>(TippedBambooSpear::new));

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistryObject<S> register(String pKey, final Supplier<S> pRecipeSerializer) {
        return RECIPE_SERIALIZER.register(pKey, pRecipeSerializer);
    }
}
