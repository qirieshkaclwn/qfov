package qirieshka.qfov;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class AntiXrayLogic {

    public static final ThreadLocal<Level> currentLevel = new ThreadLocal<>();

    public static BlockState getReplacementState(BlockState state) {
        if (state.is(Blocks.DIAMOND_ORE) || state.is(Blocks.GOLD_ORE) || state.is(Blocks.IRON_ORE) ||
            state.is(Blocks.COAL_ORE) || state.is(Blocks.COPPER_ORE) || state.is(Blocks.LAPIS_ORE) ||
            state.is(Blocks.REDSTONE_ORE) || state.is(Blocks.EMERALD_ORE)) {
            return Blocks.STONE.defaultBlockState();
        }
        if (state.is(Blocks.DEEPSLATE_DIAMOND_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE) || state.is(Blocks.DEEPSLATE_IRON_ORE) ||
            state.is(Blocks.DEEPSLATE_COAL_ORE) || state.is(Blocks.DEEPSLATE_COPPER_ORE) || state.is(Blocks.DEEPSLATE_LAPIS_ORE) ||
            state.is(Blocks.DEEPSLATE_REDSTONE_ORE) || state.is(Blocks.DEEPSLATE_EMERALD_ORE)) {
            return Blocks.DEEPSLATE.defaultBlockState();
        }
        if (state.is(Blocks.NETHER_GOLD_ORE) || state.is(Blocks.NETHER_QUARTZ_ORE) || state.is(Blocks.ANCIENT_DEBRIS)) {
            return Blocks.NETHERRACK.defaultBlockState();
        }
        return null; // Not an ore that we obfuscate
    }

    public static boolean isOreBlock(BlockState state) {
        return getReplacementState(state) != null;
    }

    public static boolean isExposed(Level level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            if (level.isLoaded(neighborPos)) {
                BlockState neighborState = level.getBlockState(neighborPos);
                if (!neighborState.canOcclude()) {
                    return true;
                }
            }
        }
        return false;
    }
}
