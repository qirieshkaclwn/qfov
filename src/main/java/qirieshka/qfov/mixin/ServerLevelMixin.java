package qirieshka.qfov.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qirieshka.qfov.AntiXrayLogic;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    @Inject(method = "sendBlockUpdated", at = @At("HEAD"))
    private void onSendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags, CallbackInfo ci) {
        ServerLevel level = (ServerLevel) (Object) this;
        // If a block changed from occluding to non-occluding (e.g. broken or opened)
        if (oldState.canOcclude() && !newState.canOcclude()) {
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = pos.relative(dir);
                if (level.isLoaded(neighborPos)) {
                    BlockState neighborState = level.getBlockState(neighborPos);
                    if (AntiXrayLogic.isOreBlock(neighborState)) {
                        // Trigger update for neighbor block to reveal it
                        level.sendBlockUpdated(neighborPos, neighborState, neighborState, flags);
                    }
                }
            }
        }
    }
}
