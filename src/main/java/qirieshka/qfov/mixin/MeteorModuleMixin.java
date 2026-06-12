package qirieshka.qfov.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "meteordevelopment.meteorclient.systems.modules.Module", remap = false)
public class MeteorModuleMixin {

    @Inject(method = "isActive", at = @At("HEAD"), cancellable = true)
    private void onIsActive(CallbackInfoReturnable<Boolean> cir) {
        String name = this.getClass().getSimpleName();
        if ("Xray".equals(name) || "Search".equals(name) || "CaveFinder".equals(name)) {
            // Force return false so these modules are never active/enabled
            cir.setReturnValue(false);
        }
    }
}
