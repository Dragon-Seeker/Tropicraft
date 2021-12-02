package net.tropicraft.core.mixin;

import net.bermuda.common.forge.entity.ExtEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin implements ExtEntity {
//    @Unique
//    private boolean TC_CancelSiting = true;
//
//    @Shadow @Nullable private Entity vehicle;
//
//    @Inject(method = "isPassenger", at = @At(value = "RETURN"), cancellable = true)
//    public void test(CallbackInfoReturnable<Boolean> cir) {
//        if(TC_CancelSiting){
//            cir.setReturnValue(false);
//        }
//    }
//
//    @Override
//    public void setCancelSitting(boolean var) {
//        TC_CancelSiting = var;
//    }
//
//    @Override
//    public boolean cancelSitting() {
//        return false;
//    }
}
