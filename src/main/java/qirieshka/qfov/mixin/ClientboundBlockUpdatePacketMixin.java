package qirieshka.qfov.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qirieshka.qfov.AntiXrayLogic;

@Mixin(ClientboundBlockUpdatePacket.class)
public class ClientboundBlockUpdatePacketMixin {

    @Shadow
    @Final
    @Mutable
    private BlockState blockState;

    @Inject(method = "<init>(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)V", at = @At("RETURN"))
    private void onInit(BlockGetter level, BlockPos pos, CallbackInfo ci) {
        if (level instanceof Level && !((Level) level).isClientSide()) {
            BlockState originalState = this.blockState;
            BlockState replacement = AntiXrayLogic.getReplacementState(originalState);
            if (replacement != null) {
                if (!AntiXrayLogic.isExposed((Level) level, pos)) {
                    this.blockState = replacement;
                }
            }
        }
    }
}
