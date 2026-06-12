package qirieshka.qfov.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qirieshka.qfov.AntiXrayLogic;

@Mixin(ClientboundLevelChunkPacketData.class)
public abstract class ClientboundLevelChunkPacketDataMixin {

    @Shadow
    @Final
    @Mutable
    private byte[] buffer;

    @Shadow
    private static int calculateChunkSize(LevelChunk chunk) {
        throw new AssertionError();
    }

    @Shadow
    private static void extractChunkData(FriendlyByteBuf buf, LevelChunk chunk) {
        throw new AssertionError();
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/world/level/chunk/LevelChunk;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundLevelChunkPacketData;calculateChunkSize(Lnet/minecraft/world/level/chunk/LevelChunk;)I")
    )
    private int redirectCalculateChunkSize(LevelChunk chunk) {
        // Add a 64KB safety margin to account for potential palette expansions due to obfuscation
        return calculateChunkSize(chunk) + 65536;
    }

    @Redirect(
        method = "<init>(Lnet/minecraft/world/level/chunk/LevelChunk;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/game/ClientboundLevelChunkPacketData;extractChunkData(Lnet/minecraft/network/FriendlyByteBuf;Lnet/minecraft/world/level/chunk/LevelChunk;)V")
    )
    private void redirectExtractChunkDataCall(FriendlyByteBuf buf, LevelChunk chunk) {
        extractChunkData(buf, chunk);

        // Resize the buffer back to the exact size of the serialized data
        int writtenBytes = buf.writerIndex();
        byte[] actualBuffer = new byte[writtenBytes];
        System.arraycopy(this.buffer, 0, actualBuffer, 0, writtenBytes);
        this.buffer = actualBuffer;
    }

    @Inject(method = "extractChunkData", at = @At("HEAD"))
    private static void onExtractChunkDataHead(FriendlyByteBuf buf, LevelChunk chunk, CallbackInfo ci) {
        AntiXrayLogic.currentLevel.set(chunk.getLevel());
    }

    @Inject(method = "extractChunkData", at = @At("RETURN"))
    private static void onExtractChunkDataReturn(FriendlyByteBuf buf, LevelChunk chunk, CallbackInfo ci) {
        AntiXrayLogic.currentLevel.remove();
    }

    @Redirect(
        method = "extractChunkData",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;write(Lnet/minecraft/network/FriendlyByteBuf;)V")
    )
    private static void redirectSectionWrite(LevelChunkSection section, FriendlyByteBuf buf, FriendlyByteBuf originalBuf, LevelChunk chunk) {
        net.minecraft.world.level.Level level = chunk.getLevel();
        if (level != null && !level.isClientSide()) {
            LevelChunkSection[] sections = chunk.getSections();
            int sectionIndex = -1;
            for (int i = 0; i < sections.length; i++) {
                if (sections[i] == section) {
                    sectionIndex = i;
                    break;
                }
            }

            if (sectionIndex != -1) {
                LevelChunkSection obfuscatedSection = section.copy();
                int sectionMinY = level.getMinY() + sectionIndex * 16;
                boolean modified = false;

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            net.minecraft.world.level.block.state.BlockState state = obfuscatedSection.getBlockState(x, y, z);
                            net.minecraft.world.level.block.state.BlockState replacement = AntiXrayLogic.getReplacementState(state);
                            if (replacement != null) {
                                int worldX = chunk.getPos().getMinBlockX() + x;
                                int worldY = sectionMinY + y;
                                int worldZ = chunk.getPos().getMinBlockZ() + z;
                                net.minecraft.core.BlockPos pos = new net.minecraft.core.BlockPos(worldX, worldY, worldZ);
                                if (!AntiXrayLogic.isExposed(level, pos)) {
                                    obfuscatedSection.setBlockState(x, y, z, replacement, false);
                                    modified = true;
                                }
                            }
                        }
                    }
                }

                if (modified) {
                    obfuscatedSection.write(buf);
                    return;
                }
            }
        }

        section.write(buf);
    }
}
