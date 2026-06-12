package qirieshka.qfov.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    private Minecraft minecraft;

    @Inject(
        method = "calculateFov",
        at = @At("RETURN"),
        cancellable = true
    )
    private void clampCalculateFov(float tickProgress, CallbackInfoReturnable<Float> cir) {
        if (qirieshka.qfov.QfovConfig.preventFovFlipping) {
            float finalFov = cir.getReturnValue();
            int baseFov = this.minecraft.options.fov().get();

            if (baseFov < 180) {
                if (finalFov > 180.0f) {
                    cir.setReturnValue(179.0f);
                }
            } else if (baseFov > 180) {
                if (finalFov < 180.0f) {
                    cir.setReturnValue(181.0f);
                }
            }
        }
    }
}
