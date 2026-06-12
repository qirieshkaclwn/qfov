package qirieshka.qfov.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Options.class)
public class GameOptionsMixin {

    @Redirect(
        method = "<init>",
        at = @At(
            value = "NEW",
            target = "Lnet/minecraft/client/OptionInstance$IntRange;"
        )
    )
    private OptionInstance.IntRange redirectIntRange(int minInclusive, int maxInclusive) {
        if (minInclusive == 30 && maxInclusive == 110) {
            return new OptionInstance.IntRange(30, 359);
        }
        return new OptionInstance.IntRange(minInclusive, maxInclusive);
    }
}
