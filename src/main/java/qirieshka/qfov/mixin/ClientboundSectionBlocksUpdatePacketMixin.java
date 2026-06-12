package qirieshka.qfov.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import qirieshka.qfov.AntiXrayLogic;

@Mixin(ClientboundSectionBlocksUpdatePacket.class)
public class ClientboundSectionBlocksUpdatePacketMixin {

    @Redirect(
        method = "<init>(Lnet/minecraft/core/SectionPos;Lit/unimi/dsi/fastutil/shorts/ShortSet;Lnet/minecraft/world/level/chunk/LevelChunkSection;)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;")
    )
    private BlockState redirectGetBlockState(LevelChunkSection section, int x, int y, int z, SectionPos sectionPos, it.unimi.dsi.fastutil.shorts.ShortSet shortSet, LevelChunkSection originalSection) {
        BlockState state = section.getBlockState(x, y, z);
        Level level = AntiXrayLogic.currentLevel.get();
        if (level != null && !level.isClientSide()) {
            BlockState replacement = AntiXrayLogic.getReplacementState(state);
            if (replacement != null) {
                int worldX = sectionPos.minBlockX() + x;
                int worldY = sectionPos.minBlockY() + y;
                int worldZ = sectionPos.minBlockZ() + z;
                BlockPos pos = new BlockPos(worldX, worldY, worldZ);
                if (!AntiXrayLogic.isExposed(level, pos)) {
                    return replacement;
                }
            }
        }
        return state;
    }
}
