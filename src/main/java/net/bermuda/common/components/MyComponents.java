package net.bermuda.common.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.tropicraft.Constants;
import net.tropicraft.core.common.item.scuba.ScubaData;

public class MyComponents implements EntityComponentInitializer {

    public static final ComponentKey<ScubaData> SCUBADATA = ComponentRegistryV3.INSTANCE.getOrCreate(new ResourceLocation(Constants.MODID, "scuba_data"), ScubaData.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(SCUBADATA, player -> {
            return new ScubaData(player);
        }, RespawnCopyStrategy.ALWAYS_COPY);
//        registry.beginRegistration(Player.class, SCUBADATA).impl(ScubaData.class).end(ScubaData::new);
    }
}
