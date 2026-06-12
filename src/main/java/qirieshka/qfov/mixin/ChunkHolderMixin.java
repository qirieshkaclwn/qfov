package qirieshka.qfov.mixin;

import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qirieshka.qfov.AntiXrayLogic;

@Mixin(ChunkHolder.class)
public class ChunkHolderMixin {

    @Inject(method = "broadcastChanges", at = @At("HEAD"))
    private void onBroadcastChangesHead(LevelChunk chunk, CallbackInfo ci) {
        AntiXrayLogic.currentLevel.set(chunk.getLevel());
    }

    @Inject(method = "broadcastChanges", at = @At("RETURN"))
    private void onBroadcastChangesReturn(LevelChunk chunk, CallbackInfo ci) {
        AntiXrayLogic.currentLevel.remove();
    }
}
